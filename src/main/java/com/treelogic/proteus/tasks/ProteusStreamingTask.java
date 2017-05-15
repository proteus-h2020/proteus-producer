package com.treelogic.proteus.tasks;

import com.treelogic.proteus.Runner;
import com.treelogic.proteus.hdfs.HDFS;
import com.treelogic.proteus.kafka.ProteusKafkaProducer;
import com.treelogic.proteus.model.AppModel;
import com.treelogic.proteus.model.ProteusData;
import com.treelogic.proteus.model.Row;
import com.treelogic.proteus.model.RowMapper;
import com.treelogic.proteus.utils.ListsUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class ProteusStreamingTask extends ProteusTask {
    /**
     * Path to the PROTEUS data
     */
    private String filePath;

    /**
     * A common logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ProteusStreamingTask.class);

    private static final ExecutorService service = Runner.service;

    private AppModel model;

    /**
     * Constructor that receives an HDFS path that simulates streaming
     *
     * @param filePath HDF path
     */
    public ProteusStreamingTask(String filePath) {
        super();
        this.filePath = filePath;
        this.model = new AppModel();
    }

    /**
     * Start the streaming process reading the PROTEUS data from the HDFS
     *
     * @return
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        Stream<String> stream = HDFS.readFile(this.filePath);

        stream
                .map(RowMapper::map) // Convert string into a Row
                .filter(this::discardWrongCoilRows) //Discard wrong values
                .filter(this::filterFlatness)
                .peek(this::processCoilRow)
                .forEachOrdered(this::produceMessage);
        return null;
    }

    /**
     * Excludes flatness variables from the data stream. This method stores such flatness vars in an internal list.
     * When a coil finalises, these flatness values are emitted into another kafka topic
     *
     * @param row A given coil row
     * @return
     */
    private boolean filterFlatness(Row row) {
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
     * @return
     */
    private Row processCoilRow(Row row) {
        this.model.setStatus(AppModel.ProductionStatus.PRODUCING);

        Row lastCoil = this.model.getLastCoilRow();
        double delay = 0.0D;

        if (lastCoil == null) {
            delay = 0.0D;
            this.updateCoilTimestampStart();
        } else if (row.getCoilId() == lastCoil.getCoilId()) {
            delay = this.calculateDelayBetweenCurrentAndLastRow(row);
        } else {
            logger.info("COIL " + lastCoil.getCoilId() + " has finished. New coil: " + row.getCoilId());
            logger.info("----------------------------------------------------------");
            logger.info("Previous coil started at: " + this.model.getLastCoilStart());
            logger.info("Now: " + new Date());
            logger.info("----------------------------------------------------------");

            delay = ProteusData.TIME_BETWEEN_COILS;

            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(new Date().getTime() + (long) delay);
            this.model.setLastCoilStart(date.getTime());

            logger.info("Current flatness rows: " + this.model.getCurrentFlatnessRows().size());

            this.handleFlatness();
            this.handleHSM(lastCoil.getCoilId());

            this.model.getCurrentFlatnessRows().clear();
            this.model.setStatus(AppModel.ProductionStatus.AWAITING);
        }

        this.applyDelay(delay);
        this.updateStatus(row);

        return row;
    }
    
    
    private void handleHSM(int coilId){
        String hsmFilePath = (String) ProteusData.get("hdfs.hsmPath");
        service.submit(new ProteusHSMTask(hsmFilePath, coilId));
    }
    
    private void handleFlatness(){
        List<Row> flatnessCopy = ListsUtils.copy(this.model.getCurrentFlatnessRows());

        //Produce Flatness variables
        if(flatnessCopy.size() > 0) {
        	long flatnessDelay = Long.parseLong(ProteusData.get("model.flatnessDelay").toString());
        	
        	Timer timer = new Timer();
        	TimerTask task = new TimerTask() {
				@Override
				public void run() {
		            service.submit(new ProteusFlatnessTask(flatnessCopy));					
				}
			};
			timer.schedule(task, flatnessDelay);
        }
    }

    /**
     * Calculates a delay time for the current row, based on the X value difference between current and previous row
     *
     * @param currentRow current row
     * @return
     */
    private double calculateDelayBetweenCurrentAndLastRow(Row currentRow) {
        return (currentRow.getX() - this.model.getLastCoilRow().getX())
                * (ProteusData.COIL_TIME / ProteusData.getXmax(currentRow.getCoilId()));
    }

    private void updateCoilTimestampStart() {
        this.model.setLastCoilStart(new Date());
    }

    /**
     * Updates the program status after each iteration
     *
     * @param row Current row
     */
    private void updateStatus(Row row) {
        this.model.setLastCoilRow(row);
    }

    /**
     * Produces a new message containing the current row. It uses
     *
     * @param row
     */
    private void produceMessage(Row row) {
        logger.debug("Producing row: " + row);
        ProteusKafkaProducer.produce(row);
    }

    private boolean discardWrongCoilRows(Row row) {
        return row != null && row.getX() > 0 && row.getCoilId() > 0;
    }

    /**
     * Apply a specific delay
     *
     * @param delay delay time
     */
    public void applyDelay(double delay) {
        if(delay > 7000D) { //avoid to much logs
            logger.debug("Sleeping " + this.getClass().getName()+" for " + delay + "ms");
        }
        try {
            Thread.sleep((long) delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(delay > 7000D) { //avoid to much logs
            logger.debug(this.getClass().getName() + " is alive again");
        }
    }


}
