package com.treelogic.proteus.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ignacio.g.fernandez on 9/05/17.
 */
public class HSMRecord {

    private Date timestamp;
    private int coil;
    private Map<String, Object> variables = new HashMap();
    private int varCounter;


    public HSMRecord(int coilID){
        this.timestamp = new Date();
        this.coil = coilID;
        this.varCounter = 1;
    }


    public void put(Object value){
        String key = String.format("V%d", varCounter++);
        this.variables.put(key, value);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "HSMRecord{" +
                "timestamp=" + timestamp +
                ", coil=" + coil +
                ", variables=" + variables +
                ", varCounter=" + varCounter +
                '}';
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
}
