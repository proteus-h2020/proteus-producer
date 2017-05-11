package com.treelogic.proteus.model;

import com.treelogic.proteus.ProteusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ignacio.g.fernandez on 9/05/17.
 */
public class HSMRecordMapper {

    private static final Logger logger = LoggerFactory.getLogger(HSMRecordMapper.class);
    private static final String SPLITTER = (String) ProteusData.get("model.hsm.splitter");


    public static HSMRecord map(String line){
        String[] lineSplit = line.split(SPLITTER);
        int coilID = Integer.parseInt(lineSplit[0]); //TODO: Handle CLasscastexception

        HSMRecord record = new HSMRecord(coilID);

        for(int i = 1; i < lineSplit.length; i++){
            record.put(lineSplit[i]);
        }

        return record;
    }
}
