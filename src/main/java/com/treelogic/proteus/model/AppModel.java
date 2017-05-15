package com.treelogic.proteus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppModel {

    private Integer aliveCoil;
    private Row lastCoilRow;
    private Date lastCoilStart;
    private ProductionStatus status;

    private List<Row> currentFlatnessRows;

    private double delay;

    public AppModel() {
        this.aliveCoil = null;
        this.status = ProductionStatus.AWAITING;
        this.delay = 0.0D;
        this.lastCoilRow = null;
        this.currentFlatnessRows = new ArrayList<Row>();
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

    public List<Row> getCurrentFlatnessRows() {
        return currentFlatnessRows;
    }

    public void setCurrentFlatnessRows(List<Row> currentFlatnessRows) {
        this.currentFlatnessRows = currentFlatnessRows;
    }

    public enum ProductionStatus {
        PRODUCING, AWAITING
    }
}
