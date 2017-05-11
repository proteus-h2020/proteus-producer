package com.treelogic.proteus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    public static ExecutorService service = Executors.newFixedThreadPool(15); //Max parallelism = stream, hsm and flatness at once.

    public static void main(String[] args) throws Exception {
        ExecutorService service = Runner.service;
        String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");

        ProteusData.loadData();
        logger.info("Initial data loaded. Starting program.");

        service.submit(new ProteusStreamingTask(proteusHDFSFile));
        logger.info("Streaming service task has been submitted");

        // service.submit();
        //service.shutdown(); //Don't allow more submits
        //service.awaitTermination(1 , TimeUnit.SECONDS);
    }
}
