package com.treelogic.proteus.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class RowMapper {

    private static final Logger logger = LoggerFactory.getLogger(RowMapper.class);


    public static Row map(String rowText) {
        String[] columns = rowText.split(",");
        columns[0] = fixCoilName(columns[0]); //BUG - Some coilId are " ". Replace it by -1
        Row row = null;
        switch (columns.length) {
            case 5:
                row = map2d(columns);
                break;
            case 4:
                row = map1d(columns);
                break;
            default:
                logger.warn("Unkown number of columns: " + columns.length);
                return null;
        }
        logger.debug("Current row: " + row);
        return row;
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
