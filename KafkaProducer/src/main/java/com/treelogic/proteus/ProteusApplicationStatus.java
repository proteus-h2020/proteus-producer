package com.treelogic.proteus;

import com.treelogic.proteus.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class ProteusApplicationStatus {

    private static AppModel status = new AppModel();
    private static final Logger logger = LoggerFactory.getLogger(ProteusApplicationStatus.class);
    private static DelayCalculator delayCalculator;

    private ProteusApplicationStatus() {
    }


    public static boolean updateAliveCoil(int newCoil) {
        int currentCoil = status.getAliveCoil();
        logger.info("Current Coil: " + currentCoil + ". New Row coil ID: " + newCoil);
        if (currentCoil != newCoil) {
            double currentXmax = ProteusData.getXmax(newCoil);
            logger.info("Max X for coil  " + currentCoil + " = " + currentXmax);

            delayCalculator = new DelayCalculator(currentXmax, ProteusData.COIL_TIME);
            logger.info("New calculated delay for coil=" + currentCoil + " = " + delayCalculator.getDelay());

            logger.info("Current thread " + Thread.currentThread().getName());
            logger.info("Coil changes from " + currentCoil + " to " + newCoil + ". Current delay: " + delayCalculator);
            status.setAliveCoil(newCoil);
            return true;
        }
        return false;
    }

    public static double currentDelay() {
        return delayCalculator.getDelay();
    }
}
