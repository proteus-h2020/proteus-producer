package com.treelogic.proteus;

import com.treelogic.proteus.hdfs.HDFS;
import com.treelogic.proteus.model.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class StreamingTask<T> extends SuspendableThread implements Callable<T> {
    /**
     * Path to the PROTEUS data
     */
    private String filePath;

    /**
     * A common logger
     */
    private static final Logger logger = LoggerFactory.getLogger(StreamingTask.class);

    /**
     * Current delay time to simulate streaming
     */
    private double delay;

    /**
     * POJO containing information about the last coil put in the stream
     */
    private Row lastCoil;

    /**
     * Constructor that receives an HDFS path that simulates streaming
     *
     * @param filePath HDF path
     */
    public StreamingTask(String filePath) {
        this.filePath = filePath;
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
                //.peek(this::waitWhileSuspended)
                .map(RowMapper::map) // Convert string into a Row
                .peek(this::calculateDelay) // Calculate delay time according with the current X position and the previous one
                .peek(this::applyDelay) // Apply delay previously calculated
                .peek(this::updateAppAliveCoil) // Update the alive coil, if needed
                .forEach(this::produceMessage);
        return null;
    }

    /**
     * Calculate a delay according to the current X position and the previous one
     *
     * @param row Current Row
     * @return
     */
    public Row calculateDelay(Row row) {
        if (this.lastCoil == null) {
            this.delay = 0;
        } else {
            this.delay = (row.getX() - lastCoil.getX()) * ProteusApplicationStatus.currentDelay();
        }

        if (this.delay < 0) {
            throw new IllegalArgumentException("Delay cannot be less than 0: " + this.delay);
        }
        this.lastCoil = row;
        return row;
    }

    /**
     * Update the "alive" coil identifier, if necessary
     *
     * @param row
     */
    public void updateAppAliveCoil(Row row) {
        boolean update = ProteusApplicationStatus.updateAliveCoil(row.getCoilId());
        logger.info("Is there and update in the current coil?: " + update);
        if (update) {
            logger.info("Coil " + row.getCoilId() + " finished. Suspending production for a while (" + ProteusData.TIME_BETWEEN_COILS + "s) before the next one");
            this.suspendForAWhile(ProteusData.TIME_BETWEEN_COILS);
        }
    }

    /**
     * Send the current row through a Kafka Producer
     *
     * @param row
     */
    public void produceMessage(Row row) {
        logger.info("Producing a new coil " + row);
        ProteusKafkaProducer.produce(row);
    }

    /**
     * Apply a specific delay
     *
     * @param row
     */
    public void applyDelay(Row row) {
        logger.info("Applying a delay of " + delay + "ms.");
        try {
            Thread.sleep((long) delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
