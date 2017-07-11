package eu.proteus.producer.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Treelogic. */

public final class SensorMeasurementMapper {

    /** Constructor. */
    private SensorMeasurementMapper() {
    }

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SensorMeasurementMapper.class);

    /** Method: map().
     *
     * @param rowText
     * @return */
    public static SensorMeasurement map(final String rowText) {
        String[] columns = rowText.split(",");
        columns[0] = fixCoilName(columns[0]); // BUG - Some coilId are " ".
                                              // Replace it by -1
        SensorMeasurement row = null;
        switch (columns.length) {
        case 5:
            row = map2d(columns);
            break;
        case 4:
            row = map1d(columns);
            break;
        default:
            LOGGER.warn("Unkown number of columns: " + columns.length);
            return null;
        }
        LOGGER.debug("Current row: " + row);
        return row;
    }

    /** Method: fixCoilName().
     *
     * @param coilName
     * @return */
    private static String fixCoilName(final String coilName) {
        if (coilName.trim().equals("")) {
            coilName = "-1";
        }
        return coilName;
    }

    /** Method: map1d().
     *
     * @param columns
     *            String array with columns values.
     * @return SensorMeasurement1D object. */
    private static SensorMeasurement map1d(final String[] columns) {
        return new SensorMeasurement1D(Integer.parseInt(columns[0]),
                Double.parseDouble(columns[1]), parseVarIdentifier(columns[2]),
                Double.parseDouble(columns[3]));
    }

    /** Method: map2d().
     *
     * @param columns
     *            String array with the columns values.
     * @return SensorMeasurement2D object. */
    private static SensorMeasurement map2d(final String[] columns) {
        return new SensorMeasurement2D(Integer.parseInt(columns[0]),
                Double.parseDouble(columns[1]), Double.parseDouble(columns[2]),
                parseVarIdentifier(columns[3]), Double.parseDouble(columns[4]));
    }

    /** Method: parseVarIdentifier().
     *
     * @param varName
     *            Variable name.
     * @return Variable name correctly formatted. */
    private static int parseVarIdentifier(final String varName) {
        return Integer.parseInt(varName.split("C")[1]);
    }

}
