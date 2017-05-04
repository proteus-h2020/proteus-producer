package com.treelogic.proteus;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class DelayCalculator {

    private double xMax;
    private double delay;

    public DelayCalculator(double xMax, double totalTime){
        this.xMax = xMax;
        this.delay = totalTime / xMax;
    }

    public double getDelay(){
        return this.delay;
    }


}
