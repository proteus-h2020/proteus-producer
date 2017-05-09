package com.treelogic.proteus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by ignacio.g.fernandez on 9/05/17.
 */
public abstract class ProteusTask <T> implements Callable<T> {

    private static final Logger logger = LoggerFactory.getLogger(ProteusTask.class);

    protected Date taskStart;

    public ProteusTask(){
        this.taskStart = new Date();
        logger.info("Starting a new PROTEUS task: " + this.getClass().getName()+" at " +  this.taskStart);
    }


}
