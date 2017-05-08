package com.treelogic.proteus.model;

import java.util.Date;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class AppModel {

    private Integer aliveCoil;
    private Row lastCoilRow;
    private Date lastCoilStart;
    private ProductionStatus status;
    private double delay;

    public AppModel() {
        this.aliveCoil = null;
        this.status = ProductionStatus.AWAITING;
        this.delay = 0.0D;
        this.lastCoilRow = null;
    }


    public Integer getAliveCoil() {
        return aliveCoil;
    }

    public void setAliveCoil(Integer aliveCoil) {
        this.aliveCoil = aliveCoil;
    }


    public ProductionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public Row getLastCoilRow() {
        return lastCoilRow;
    }

    public void setLastCoilRow(Row lastCoilRow) {
        this.lastCoilRow = lastCoilRow;
    }

    public Date getLastCoilStart() {
        return lastCoilStart;
    }

    public void setLastCoilStart(Date lastCoilStart) {
        this.lastCoilStart = lastCoilStart;
    }

    public enum ProductionStatus {
        PRODUCING, AWAITING
    }
}
