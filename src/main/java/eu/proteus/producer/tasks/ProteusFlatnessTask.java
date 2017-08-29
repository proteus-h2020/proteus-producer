package eu.proteus.producer.tasks;

import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.SensorMeasurement;

import java.util.List;


public class ProteusFlatnessTask extends ProteusTask{
	
    /**
     * List of "coiltimeseries" records containing flatness values
     */
    private List<SensorMeasurement> flatnessRows;

    public ProteusFlatnessTask(List<SensorMeasurement> flatnessRows) {
        super();
        this.flatnessRows = flatnessRows;
    }

    @Override
    public void run() {
        this.flatnessRows
                .stream()
                .forEach(ProteusKafkaProducer::produceFlatness);
    }
}