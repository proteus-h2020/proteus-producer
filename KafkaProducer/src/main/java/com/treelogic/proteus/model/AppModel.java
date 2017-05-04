package com.treelogic.proteus.model;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class AppModel {

    private int aliveCoil;
    private ProductionStatus status;


    public int getAliveCoil() {
        return aliveCoil;
    }

    public void setAliveCoil(int aliveCoil) {
        this.aliveCoil = aliveCoil;
    }

    public enum ProductionStatus{
        PRODUCING, AWAITING
    }
}
