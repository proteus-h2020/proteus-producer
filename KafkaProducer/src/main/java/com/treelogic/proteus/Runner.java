package com.treelogic.proteus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        ProteusData.loadData();
        logger.info("Initial data loaded. Starting program.");

        ExecutorService service = Executors.newFixedThreadPool(1);

        String proteusHDFSFile = (String) ProteusData.get("hdfs.filePath");

        service.submit(new StreamingTask<String>(proteusHDFSFile));
        //service.shutdown(); //Don't allow more submits
        //service.awaitTermination(1 , TimeUnit.SECONDS);
    }
}
