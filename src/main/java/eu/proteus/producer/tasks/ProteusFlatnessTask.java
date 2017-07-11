package eu.proteus.producer.tasks;

import java.util.List;

import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.SensorMeasurement;

/** @author Treelogic */
public class ProteusFlatnessTask extends ProteusTask {

    /** List of "coiltimeseries" records containing flatness values. */
    private List<SensorMeasurement> flatnessRows;

    /** Method ProteusFlatnessTask().
     *
     * @param flatnessRows
     *            List with flatness rows. */
    public ProteusFlatnessTask(final List<SensorMeasurement> flatnessRowsList) {
        super();
        flatnessRows = flatnessRowsList;
    }

    @Override
    public final Void call() throws Exception {
        this.flatnessRows.stream()
                .forEach(ProteusKafkaProducer::produceFlatness);
        return null;
    }
}
