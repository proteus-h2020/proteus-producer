package eu.proteus.producer.tasks;

import eu.proteus.producer.hdfs.HDFS;
import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.*;

import java.util.stream.Stream;


public class ProteusHSMTask extends ProteusTask {
    /**
     * Path to the PROTEUS HSM data
     */
    private String hsmFilePath;
    
    /**
     * Coil ID for the current HSM
     */
    private int coilId;

    public ProteusHSMTask(String hsmFilePath, int coilId) {
        super();
        this.hsmFilePath = hsmFilePath;
        this.coilId = coilId;
    }

    @Override
    public Void call() throws Exception {
        Stream<String> stream = HDFS.readFile(this.hsmFilePath);

        stream
                .map(HSMMeasurementMapper::map)
                .filter(this::filterByCoil)
                .forEach(ProteusKafkaProducer::produceHSMRecord);
        return null;
    }

    private boolean filterByCoil(HSMMeasurement record) {
        return record.getCoil() == coilId;
    }
}
