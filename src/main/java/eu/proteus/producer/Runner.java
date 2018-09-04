package eu.proteus.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.tasks.ProteusHiveStreamingTask;
import eu.proteus.producer.tasks.ProteusStreamingTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	public static ExecutorService service = Executors.newCachedThreadPool();

	public static void main(String[] args) throws Exception {
		// Configuration HDFS
		String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");
		
		// Configuration Hive
		String proteusHiveUrl = (String) ProteusData.get("hive.connection");
		String proteusHiveUser = (String) ProteusData.get("hive.user");
		String proteusHivePassword = (String) ProteusData.get("hive.password");
		
		// Parameters
		Long coil = args != null && args.length > 0 ? Long.parseLong(args[0]) : null;
		ProteusData.loadData();
		logger.info("Initial data loaded. Starting program.");

		// Simulation mode
		String proteusMode = (String) ProteusData.get("mode");
		if (proteusMode != null && proteusMode.toLowerCase().equals("hdfs")) {
			service.submit(new ProteusStreamingTask(proteusHDFSFile));
			logger.info("Streaming service task has been submitted [Mode: HDFS]");
		} else if (proteusMode != null && proteusMode.toLowerCase().equals("hive")) {
			service.submit(new ProteusHiveStreamingTask(proteusHiveUrl, proteusHiveUser, proteusHivePassword, coil));
			logger.info("Streaming service task has been submitted [Mode: HIVE]");
		} else {
			logger.warn("Simulation mode no valid!");
			logger.info("Streaming service task hasn't been submitted");
		}
	}
}
