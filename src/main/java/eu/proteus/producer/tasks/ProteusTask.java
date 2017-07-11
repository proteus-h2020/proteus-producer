package eu.proteus.producer.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Callable;

public abstract class ProteusTask implements Callable<Void> {

    private static final Logger logger = LoggerFactory
            .getLogger(ProteusTask.class);

    protected Date taskStart;

    public ProteusTask() {
        this.taskStart = new Date();
        logger.info("Starting a new PROTEUS task: " + this.getClass().getName()
                + " at " + this.taskStart);
    }

}
