package com.treelogic.proteus;

import com.treelogic.proteus.model.Row;
import com.treelogic.proteus.model.Row1D;
import com.treelogic.proteus.model.Row2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class RowMapper {

    private static final Logger logger = LoggerFactory.getLogger(RowMapper.class);


    public static Row map(String rowText) {
        String[] columns = rowText.split(",");

        columns[0] = fixCoilName(columns[0]); //BUG - Some coilId are " ". Replace it by -1
        switch (columns.length) {
            case 5:
                return map2d(columns);
            case 4:
                return map1d(columns);
            default:
                logger.warn("Unkown number of columns: " + columns.length);
                return null;
        }
    }

    private static String fixCoilName(String coilName) {
        if (coilName.trim().equals("")) {
            coilName = "-1";
        }
        return coilName;
    }

    private static Row map1d(String[] columns) {
        return new Row1D(
                Integer.parseInt(columns[0]),
                Double.parseDouble(columns[1]),
                columns[2],
                Double.parseDouble(columns[3])
        );
    }

    private static Row map2d(String[] columns) {
        return new Row2D(
                Integer.parseInt(columns[0]),
                Double.parseDouble(columns[1]),
                Double.parseDouble(columns[2]),
                columns[3],
                Double.parseDouble(columns[4])
        );
    }


}
