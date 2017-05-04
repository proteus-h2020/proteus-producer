package com.treelogic.proteus.model;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class Row2D extends Row {

    private double y;

    public Row2D(int coilId, double x, double y, String varName, double value){
        super();
        this.coilId = coilId;
        this.x = x;
        this.y = y;
        this.varName = varName;
        this.value = value;
    }


}

