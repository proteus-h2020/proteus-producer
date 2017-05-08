package com.treelogic.proteus.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public abstract class Row {

    protected Date timestamp;
    protected int coilId;
    protected double x;
    protected String varName;
    protected double value;
    protected String type;

    public Row() {
        this.timestamp = new Date();
        this.type = this.getClass().getSimpleName();
    }


    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCoilId() {
        return coilId;
    }

    public void setCoilId(int coilId) {
        this.coilId = coilId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        if (coilId != row.coilId) return false;
        if (Double.compare(row.x, x) != 0) return false;
        if (Double.compare(row.value, value) != 0) return false;
        if (timestamp != null ? !timestamp.equals(row.timestamp) : row.timestamp != null) return false;
        return varName != null ? varName.equals(row.varName) : row.varName == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + coilId;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (varName != null ? varName.hashCode() : 0);
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


	@Override
	public String toString() {
		return "Row [timestamp=" + timestamp + ", coilId=" + coilId + ", x=" + x + ", varName=" + varName + ", value="
				+ value + ", type=" + type + "]";
	}


}
