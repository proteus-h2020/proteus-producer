package com.treelogic.proteus;

import com.treelogic.proteus.hdfs.HDFS;
import com.treelogic.proteus.model.AppModel;
import com.treelogic.proteus.model.Row;
import com.treelogic.proteus.model.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class ProteusStreamingTask<T> extends SuspendableThread implements Callable<T> {
    /**
     * Path to the PROTEUS data
     */
    private String filePath;

    /**
     * A common logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ProteusStreamingTask.class);


    private AppModel model;

    /**
     * Constructor that receives an HDFS path that simulates streaming
     *
     * @param filePath HDF path
     */
    public ProteusStreamingTask(String filePath) {
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
    public T call() throws Exception {
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
     * @param row A given coil row
     * @return
     */
    private boolean filterFlatness(Row row){
        String varname = row.getVarName();

        if(ProteusData.FLATNESS_VARNAMES.contains(varname)){
            this.model.getCurrentFlatnessRows().add(row); // Store flatness row
            return false;
        }
        return true;
    }

    /**
     * Processes the given coil row, by calculating its delay time according to its X position
     * @param row A given coil row
     * @return
     */
    private Row processCoilRow(Row row) {
        Row lastCoil = this.model.getLastCoilRow();
        logger.debug("Last coil id: " + (lastCoil!= null ? lastCoil.getCoilId() : "null") + ". Current coil: " + row.getCoilId());

        double delay = 0.0D;

        if (lastCoil == null) {
            delay = 0.0D;
            this.updateCoilTimestampStart();
        } else if (row.getCoilId() == lastCoil.getCoilId()) {
            delay = this.calculateDelayBetweenCurrentAndLastRow(row);
        } else {
            long timeTaken =  (new Date().getTime() - this.model.getLastCoilStart().getTime());
            logger.debug("Changing COIL FROM " + lastCoil.getCoilId() + " to " + row.getCoilId());
            logger.debug("Last: " +  this.model.getLastCoilStart());
            logger.debug("Now: " + new Date());
            delay = ProteusData.TIME_BETWEEN_COILS;

            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(new Date().getTime() + (long)delay);
            this.model.setLastCoilStart(date.getTime());

            ProteusKafkaProducer.produceFlatness(this.model.getCurrentFlatnessRows());
            this.model.getCurrentFlatnessRows().clear();
            //todo: update app status to AWAITING
        }

        this.applyDelay(delay);
        this.updateStatus(row);

        return row;
    }

    /**
     * Calculates a delay time for the current row, based on the X value difference between current and previous row
     * @param currentRow current row
     * @return
     */
    private double calculateDelayBetweenCurrentAndLastRow(Row currentRow){
        return (currentRow.getX() - this.model.getLastCoilRow().getX())
               * (ProteusData.COIL_TIME / ProteusData.getXmax(currentRow.getCoilId()));
    }

    private void updateCoilTimestampStart(){
        this.model.setLastCoilStart(new Date());
    }

    /**
     * Updates the program status after each iteration
     * @param row Current row
     */
    private void updateStatus(Row row) {
        this.model.setLastCoilRow(row);
    }

    /**
     * Produces a new message containing the current row. It uses
     * @param row
     */
    private void produceMessage(Row row){
        logger.debug("Producing row: " + row);
        ProteusKafkaProducer.produce(row);
    }

    private boolean discardWrongCoilRows(Row row) {
        return row != null && row.getX() > 0 && row.getCoilId() > 0;
    }

    /**
     * Apply a specific delay
     *
     * @param row
     */
    public void applyDelay(double delay) {
        logger.debug("Applying a delay of " + delay + "ms.");
        try {
            Thread.sleep((long) delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
