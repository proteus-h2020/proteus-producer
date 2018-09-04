package eu.proteus.producer.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.Runner;
import eu.proteus.producer.db.HiveDB;
import eu.proteus.producer.db.ResultSetHandler;
import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.AppModel;
import eu.proteus.producer.model.IgnorableCoilIdentifiers;
import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.model.SensorMeasurement;
import eu.proteus.producer.model.SensorMeasurementMapper;
import eu.proteus.producer.utils.ListsUtils;

public class ProteusHiveStreamingTask extends ProteusTask {
	
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProteusHiveStreamingTask.class);
	
	/**
	 * Executor service
	 */
	private static ExecutorService service = Runner.service; 
	
	/**
	 * Result handler
	 */
	private ResultSetHandler<List<String>> handler = new ResultSetHandler<List<String>>() {
	    public List<String> handle(ResultSet rs) throws SQLException {
	    	List<String> result = new ArrayList<>();
	    	if (rs != null) {
				while (rs.next()) {
					int coil = -1;
					try {
						coil = rs.getInt(1);
						float x = rs.getFloat(2);
						if (rs.getString(5) == null) {  // Row with 4 columns
							String varname = rs.getString(3);
							float value = rs.getFloat(4);
							result.add(coil + "," + x + "," + varname + "," + value);
						} else {  // Row with 5 columns									
							float y = rs.getFloat(3);
							String varname = rs.getString(4);
							float value = rs.getFloat(5);
							result.add(coil + "," + x + "," + y + "," + varname + "," + value);
						}								
					} catch (Exception e) {
						logger.error("Row failed [Coil: " + coil + "]", e);
					}
				}	
	    	}
	        return result;
	    }
	};
	
	/**
	 * Model
	 */
	private AppModel model;
	
	/**
	 * Hive connection URL 
	 */
	private String url;
	
	/**
	 * Hive user 
	 */
	private String user;
	
	/**
	 * Hive user password 
	 */
	private String password;
	
	/**
	 * Coil identifier
	 */
	private Long coil;

	/**
	 * Constructor
	 *
	 * @param connectionUrl Hive connection URL
	 * @param query Hive query
	 */
	public ProteusHiveStreamingTask(String url, String user, String password, Long coil) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("Hive connection URL no valid");
		} else {
			this.model = new AppModel();
			this.url = url;
			this.user = user != null ? user : "";
			this.password = password != null ? password : "";
			this.coil = coil;
		}
	}

	/**
	 * Start the streaming process reading the PROTEUS data from Hive
	 */
	@Override
	public void run() {
		List<String> result = null;
		String query = null;
		try {
			// Range of coils
			List<Long> coils = this.getCoilId();
			logger.info("Coils found [Total: " + coils.size() + "]");
			
			// Coil processing
			for (Long current : coils) {
				// Coil data
				query = ProteusData.get("hive.query").toString().replace("$1", current.toString());							
				result = HiveDB.query(url, user, password, query, this.handler);
				if (result.isEmpty()) {
					this.model = new AppModel();
				}
				
				// Coil processing
				logger.info("NEW COIL: " + current + " [Rows: " + result.size() + "][Query: " + query + "]");
				result.stream().map(SensorMeasurementMapper::map) // Convert string into a Row
					  .filter(this::discardWrongCoilRows) // Discard wrong values
					  .filter(this::filterFlatness)
					  .peek(this::processCoilRow)
					  .forEachOrdered(this::produceMessage);
				this.completeCoil(current);
			}
		} catch (SQLException e) {
			logger.error("Hive query failed [Query: " + query + "]", e);
		}
	}
	
	/**
	 * Returns the list of coils
	 * 
	 * @return object {@link Pair}
	 * @throws SQLException
	 */
	private List<Long> getCoilId() throws SQLException {
		Long current = this.coil;
		String query = ProteusData.get("hive.partitions").toString();		
		ResultSetHandler<List<Long>> rsh = new ResultSetHandler<List<Long>>() {
		    public List<Long> handle(ResultSet rs) throws SQLException {
		    	List<Long> id = new LinkedList<>();
		    	while (rs.next()) {
		    		try {
		    			String[] result = rs.getString(1).split("=");
		    			if (result.length > 1) {
		    				Long partition = Long.parseLong(result[1]);
		    				if (current == null || partition >= current) {
		    					id.add(partition);	
		    				}
		    			}
		    		} catch (NumberFormatException e) {
		    			logger.error("Partition no numerical [" + rs.getString(1) + "]");
					}
		    	}
		        return id;
		    }
		};
		return HiveDB.query(url, user, password, query, rsh);
	}
	
	/**
	 * Discard wrong coil rows
	 * 
	 * @param row
	 * @return Boolean
	 */
	private boolean discardWrongCoilRows(SensorMeasurement row) {
		return row != null && row.getX() > 0 && row.getCoilId() > 0 && !IgnorableCoilIdentifiers.get().contains(row.getVarName());
	}
	
	/**
	 * Excludes flatness variables from the data stream. This method stores such
	 * flatness vars in an internal list. When a coil finalises, these flatness
	 * values are emitted into another kafka topic
	 *
	 * @param row A given coil row
	 * @return Boolean
	 */
	private boolean filterFlatness(SensorMeasurement row) {
		int varname = row.getVarName();
		if (ProteusData.FLATNESS_VARNAMES.contains(varname)) {
			this.model.getCurrentFlatnessRows().add(row); // Store flatness row
			return false;
		}
		return true;
	}

	/**
	 * Processes the given coil row, by calculating its delay time according to its X position
	 *
	 * @param row A given coil row
	 * @return object {@link SensorMeasurement}
	 */
	private SensorMeasurement processCoilRow(SensorMeasurement row) {
		this.model.setStatus(AppModel.ProductionStatus.PRODUCING);
		SensorMeasurement lastCoil = this.model.getLastCoilRow();
		double delay = 0.0D;
		if (lastCoil == null) {
			delay = 0.0D;
			this.model.setLastCoilStart(new Date());
		} else {
			delay = this.calculateDelayBetweenCurrentAndLastRow(row);
		}
		this.applyDelay(delay, row);
		this.model.setLastCoilRow(row);
		return row;
	}
	
	/**
	 * Complete the coil processing and returns the next coil identifier
	 */
	private void completeCoil(Long coil) {
		if (this.model.getLastCoilRow() != null) {
			// Stats
			Date time = new Date(), start = this.model.getLastCoilStart();
			double minutes = (double) (time.getTime() - start.getTime()) / (double) (60 * 1000) % 60;
			int expectedMinutes = (ProteusData.COIL_TIME / 1000) / 60;
			if (minutes > (expectedMinutes + 3)) {
				logger.warn("COIL ( " + this.model.getLastCoilRow().getCoilId() + " ) has taken " + minutes + " minutes");
			}
			logger.info("COIL " + this.model.getLastCoilRow().getCoilId() + " has finished "
					            + "[Started at: " + this.model.getLastCoilStart() + "]"
					            + "[Time: " + (time.getTime() - start.getTime()) + " ms]");
			
			// Produce flatness variables
			List<SensorMeasurement> flatnessCopy = ListsUtils.copy(this.model.getCurrentFlatnessRows());
			logger.info("Current flatness rows: " + flatnessCopy.size());
			if (flatnessCopy.size() > 0) { 
				long flatnessDelay = Long.parseLong(ProteusData.get("model.flatnessDelay").toString());
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						service.submit(new ProteusFlatnessTask(flatnessCopy));
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, flatnessDelay);
				this.model.getCurrentFlatnessRows().clear();	
			}			
			
			// HSM
			String hsmFilePath = (String) ProteusData.get("hdfs.hsmPath");
			service.submit(new ProteusHSMTask(hsmFilePath, this.model.getLastCoilRow().getCoilId()));
			
			// Status
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(time.getTime() + new Long(ProteusData.TIME_BETWEEN_COILS));
			this.model.setLastCoilStart(date.getTime());
			this.model.setStatus(AppModel.ProductionStatus.AWAITING);
		}
	}
	
	/**
	 * Produces a new message containing the current row
	 *
	 * @param row object {@link SensorMeasurement}
	 */
	private void produceMessage(SensorMeasurement row) {
		logger.debug("Producing row: " + row);
		ProteusKafkaProducer.produce(row);
	}
	
	/**
	 * Calculates a delay time for the current row, based on the X value
	 * difference between current and previous row
	 *
	 * @param row Current row
	 * @return delay time
	 */
	private double calculateDelayBetweenCurrentAndLastRow(SensorMeasurement row) {
		return (row.getX() - this.model.getLastCoilRow().getX()) * 
			   (ProteusData.COIL_TIME / ProteusData.getXmax(row.getCoilId()));
	}

	/**
	 * Apply a specific delay
	 *
	 * @param delay Delay time
	 */
	private void applyDelay(double delay, SensorMeasurement row) {
		try {
			if (delay > 7000D) { // avoid to much logs
				logger.debug("Sleeping " + this.getClass().getName() + " for " + delay + "ms");
			}
			Thread.sleep((long) delay);
		} catch (InterruptedException e) {
			logger.error("Apply delay interrupted", e);		
		} catch (Exception e) {
			logger.error("Apply delay failed", e);			
		} finally {
			if (delay > 7000D) { // avoid to much logs
				logger.debug(this.getClass().getName() + " is alive again");
			}
		}		
	}
	
}
