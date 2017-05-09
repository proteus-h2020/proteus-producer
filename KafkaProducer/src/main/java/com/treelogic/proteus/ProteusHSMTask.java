package com.treelogic.proteus;

import com.treelogic.proteus.hdfs.HDFS;
import com.treelogic.proteus.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class ProteusHSMTask<T> extends ProteusTask {
    /**
     * Path to the PROTEUS HSM data
     */
    private String hsmFilePath;
    private int coilId;

    public ProteusHSMTask(String hsmFilePath, int coilId) {
        super();
        this.hsmFilePath = hsmFilePath;
        this.coilId = coilId;
    }

    @Override
    public T call() throws Exception {
        Stream<String> stream = HDFS.readFile(this.hsmFilePath);

        stream
                .map(HSMRecordMapper::map)
                .filter(this::filterByCoil)
                .forEach(ProteusKafkaProducer::produceHSMRecord);
        return null;
    }

    private boolean filterByCoil(HSMRecord record) {
        return record.getCoil() == coilId;
    }
}
