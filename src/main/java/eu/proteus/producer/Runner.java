package eu.proteus.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.tasks.ProteusHSMTask;
import eu.proteus.producer.tasks.ProteusStreamingTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    
    //public static ExecutorService service = Executors.newCachedThreadPool(); //Max parallelism = stream, hsm and flatness at once.

    public static void main(String[] args) throws Exception {
        //ExecutorService service = Runner.service;
        String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");

        ProteusData.loadData();
        logger.info("Initial data loaded. Starting program.");

		Thread thread = new Thread(new ProteusStreamingTask(proteusHDFSFile));
		thread.start();
        //service.submit(new ProteusStreamingTask(proteusHDFSFile));
        logger.info("Streaming service task has been submitted");

        // service.submit();
        //service.shutdown(); //Don't allow more submits
        //service.awaitTermination(1 , TimeUnit.SECONDS);
    }
}
