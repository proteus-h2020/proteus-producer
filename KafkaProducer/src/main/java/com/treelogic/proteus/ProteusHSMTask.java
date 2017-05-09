package com.treelogic.proteus;

import com.treelogic.proteus.hdfs.HDFS;
import com.treelogic.proteus.model.AppModel;
import com.treelogic.proteus.model.HSMRecordMapper;
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
public class ProteusHSMTask<T> implements Callable<T> {
    /**
     * Path to the PROTEUS HSM data
     */
    private String hsmFilePath;

    public ProteusHSMTask(String hsmFilePath) {
        this.hsmFilePath = hsmFilePath;
    }

    @Override
    public T call() throws Exception {
        Stream<String> stream = HDFS.readFile(this.hsmFilePath);

        stream
                .map(HSMRecordMapper::map)
                .forEach(ProteusKafkaProducer::produceHSMRecord);
        return null;
    }
}
