package eu.proteus.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.tasks.ProteusHSMTask;
import eu.proteus.producer.tasks.ProteusStreamingTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	public static ExecutorService service = Executors.newCachedThreadPool();

	public static void main(String[] args) throws Exception {
		String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");

		ProteusData.loadData();
		logger.info("Initial data loaded. Starting program.");

		service.submit(new ProteusStreamingTask(proteusHDFSFile));
		logger.info("Streaming service task has been submitted");
	}
}
