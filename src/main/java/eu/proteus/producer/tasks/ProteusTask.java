package eu.proteus.producer.tasks;

import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Treelogic */
public abstract class ProteusTask implements Callable<Void> {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProteusTask.class);

    /** Date. */
    private Date taskStart;

    /** Method setTaskStart().
     *
     * @param taskInit */
    public final void setTaskStart(final Date taskInit) {
        taskStart = taskInit;
    }

    /** Method: getTaskStart().
     *
     * @return */
    public final Date getTaskStart() {
        return taskStart;
    }

    /** Method. ProteusTask(). */
    public ProteusTask() {
        taskStart = new Date();
        LOGGER.info("Starting a new PROTEUS task: " + this.getClass().getName()
                + " at " + getTaskStart());
    }

}
