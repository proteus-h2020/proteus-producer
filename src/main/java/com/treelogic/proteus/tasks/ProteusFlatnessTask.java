package com.treelogic.proteus.tasks;

import com.treelogic.proteus.kafka.ProteusKafkaProducer;
import com.treelogic.proteus.model.SensorMeasurement;

import java.util.List;


public class ProteusFlatnessTask extends ProteusTask{
	
    /**
     * List of "coiltimeseries" records containg flatness values
     */
    private List<SensorMeasurement> flatnessRows;

    public ProteusFlatnessTask(List<SensorMeasurement> flatnessRows) {
        super();
        this.flatnessRows = flatnessRows;
    }

    @Override
    public Void call() throws Exception {
        this.flatnessRows
                .stream()
                .forEach(ProteusKafkaProducer::produceFlatness);
        return null;
    }
}