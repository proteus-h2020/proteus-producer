package com.treelogic.proteus.kafka.producer;

public class ProteusPojo {

	
	private Double x;
	private Double y;
	private double value;
	private String variableName;
	private String coilId;
	
	
	public ProteusPojo(Double x, Double y, double value, String variableName, String coilId){
		this.x = x;
		this.y = y;
		this.value = value;
		this.variableName = variableName;
		this.coilId = coilId;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getCoilId() {
		return coilId;
	}
	public void setCoilId(String colId) {
		this.coilId = colId;
	}
	
	
}
