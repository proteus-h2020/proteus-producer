package com.treelogic.proteus.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class HSMMeasurement extends Measurement{

	private int coil;
	private Map<String, Object> variables = new HashMap<String, Object>();
	private int varCounter;

	public HSMMeasurement(int coilID) {
		this.coil = coilID;
		this.varCounter = 1;
	}

	public HSMMeasurement(int coilID, Map<String, Object> variables){
		this.coil = coilID;
		this.varCounter = variables.size();
		this.variables = variables;
	}
	public void put(Object value) {
		String key = String.format("V%d", varCounter++);
		this.variables.put(key, value);
	}

	public int getCoil() {
		return coil;
	}

	public void setCoil(int coil) {
		this.coil = coil;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + coil;
		result = prime * result + varCounter;
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HSMMeasurement other = (HSMMeasurement) obj;
		if (coil != other.coil)
			return false;
		if (varCounter != other.varCounter)
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HSMMeasurement [coil=" + coil + ", variables=" + variables + ", varCounter=" + varCounter + "]";
	}
		
}
