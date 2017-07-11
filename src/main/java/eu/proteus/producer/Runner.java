package eu.proteus.producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.tasks.ProteusStreamingTask;

/** @author Treelogic */
public class Runner {

    /** Max parallelism allowed. */
    static final int MAX_PARALLELISM = 15;
    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    /** Method: newFixedThreadPool(). */
    private static ExecutorService service = Executors
            .newFixedThreadPool(MAX_PARALLELISM);

    /** Method: getExecutorService().
     *
     * @return */
    public static ExecutorService getExecutorService() {
        return service;
    }

    /** Method: setExecutorService().
     *
     * @param executorservice */
    public final void setExecutorService(
            final ExecutorService executorservice) {
        service = executorservice;
    }

    /** Method: Main().
     *
     * @param args
     * @throws Exception */
    public static void main(final String[] args) throws Exception {
        ExecutorService service = getExecutorService();
        String proteusHDFSFile = (String) ProteusData.get("hdfs.streamingPath");

        ProteusData.loadData();
        LOGGER.info("Initial data loaded. Starting program.");

        service.submit(new ProteusStreamingTask(proteusHDFSFile));
        LOGGER.info("Streaming service task has been submitted");
    }
}
