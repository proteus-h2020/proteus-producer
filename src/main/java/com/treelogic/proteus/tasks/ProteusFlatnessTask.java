package com.treelogic.proteus.tasks;

import com.treelogic.proteus.kafka.ProteusKafkaProducer;
import com.treelogic.proteus.model.Row;

import java.util.List;


public class ProteusFlatnessTask extends ProteusTask{
	
    /**
     * List of "coiltimeseries" records containg flatness values
     */
    private List<Row> flatnessRows;

    public ProteusFlatnessTask(List<Row> flatnessRows) {
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