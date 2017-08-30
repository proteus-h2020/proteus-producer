package eu.proteus.producer.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public abstract class ProteusTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ProteusTask.class);

    protected Date taskStart;

    public ProteusTask(){
        this.taskStart = new Date();
        logger.info("Starting a new PROTEUS task: " + this.getClass().getName()+" at " +  this.taskStart);
    }


}
