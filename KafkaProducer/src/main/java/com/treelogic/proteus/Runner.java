package com.treelogic.proteus;

import com.treelogic.proteus.hdfs.HDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws Exception {
        ProteusData.loadData();
        logger.info("Initial data loaded. Starting program.");

        ExecutorService service = Executors.newFixedThreadPool(1);

        String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");

        //new ProteusStreamingTask<String>(proteusHDFSFile).call();

        new ProteusHSMTask<String>((String) ProteusData.get("hdfs.hsmPath")).call();

       // service.submit();
        //service.shutdown(); //Don't allow more submits
        //service.awaitTermination(1 , TimeUnit.SECONDS);
    }
}
