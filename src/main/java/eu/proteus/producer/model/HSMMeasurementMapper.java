package eu.proteus.producer.model;

/** @author Treelogic */

public final class HSMMeasurementMapper {

    /** Constructor. */
    private HSMMeasurementMapper() {
    }

    /** Splitter symbol. */
    private static final String SPLITTER = (String) ProteusData
            .get("model.hsm.splitter");

    /** @param coilRecord
     * @return */
    public static HSMMeasurement map(final String coilRecord) {
        String[] lineSplit = coilRecord.split(SPLITTER);
        int coilID = Integer.parseInt(lineSplit[0]);

        HSMMeasurement hsmRecord = new HSMMeasurement(coilID);

        for (String line : lineSplit) {
            hsmRecord.put(line);
        }

        return hsmRecord;
    }
}
