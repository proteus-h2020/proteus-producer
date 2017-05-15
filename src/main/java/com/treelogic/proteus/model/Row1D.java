package com.treelogic.proteus.model;

public class Row1D extends Row {


	public Row1D(){}
	
    public Row1D(int coilId, double x, int variableIdentifier, double value){
        super();
        this.coilId = coilId;
        this.x = x;
        this.varName = variableIdentifier;
        this.value = value;
    }
   

}

