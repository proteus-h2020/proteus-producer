package eu.proteus.producer.model;

/** @author Treelogic */

public class SensorMeasurement1D extends SensorMeasurement {

    /** Constructor: SensorMeasurement1D(). */
    public SensorMeasurement1D() {
    }

    /*** Constructor: SensorMeasurement1D(int, double, int, double).
     *
     * @param coilId
     *            Coil identifier
     * @param x
     *            X position
     * @param variableIdentifier
     *            Variable identifier
     * @param value
     *            Value for the position. */

    public SensorMeasurement1D(final int coilId, final double x,
            final int variableIdentifier, final double value) {
        super();
        this.coilId = coilId;
        this.x = x;
        this.varName = variableIdentifier;
        this.value = value;
    }

}
