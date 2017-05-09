package com.treelogic.proteus;

import com.treelogic.proteus.model.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by ignacio.g.fernandez on 9/05/17.
 */
public class ProteusFlatnessTask<T> extends ProteusTask{
    /**
     * Path to the PROTEUS HSM data
     */
    private List<Row> flatnessRows;

    private static final Logger logger = LoggerFactory.getLogger(ProteusFlatnessTask.class);

    public ProteusFlatnessTask(List<Row> flatnessRows) {
        super();
        this.flatnessRows = flatnessRows;
    }

    @Override
    public T call() throws Exception {
        this.flatnessRows
                .stream()
                .forEach(ProteusKafkaProducer::produceFlatness);
        return null;
    }
}