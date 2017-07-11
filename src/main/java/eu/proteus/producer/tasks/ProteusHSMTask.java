package eu.proteus.producer.tasks;

import java.util.stream.Stream;

import eu.proteus.producer.hdfs.HDFS;
import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.HSMMeasurement;
import eu.proteus.producer.model.HSMMeasurementMapper;

/** @author Treelogic */
public class ProteusHSMTask extends ProteusTask {
    /** Path to the PROTEUS HSM data. */
    private final String hsmFilePath;

    /** Coil ID for the current HSM. */
    private final int coilId;

    /** Constructor. ProteusHSMTask(String, int).
     *
     * @param hsmFilePath
     * @param coilId */
    public ProteusHSMTask(final String hsmFileLocation,
            final int coilidentifier) {
        super();
        hsmFilePath = hsmFileLocation;
        coilId = coilidentifier;
    }

    @Override
    public final Void call() throws Exception {
        Stream<String> stream = HDFS.readFile(hsmFilePath);

        stream.map(HSMMeasurementMapper::map).filter(this::filterByCoil)
                .forEach(ProteusKafkaProducer::produceHSMRecord);
        return null;
    }

    /** Method: filterByCoil(HSMMeasurement).
     *
     * @param record
     * @return */
    private boolean filterByCoil(final HSMMeasurement record) {
        return record.getCoil() == coilId;
    }
}
