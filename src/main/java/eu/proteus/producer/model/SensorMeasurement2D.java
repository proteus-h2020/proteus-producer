package eu.proteus.producer.model;

/** @author Treelogic */
public class SensorMeasurement2D extends SensorMeasurement {

    /** Position Y. */
    private double y;

    /** Constructor: SensorMeasuremente2D(). */
    public SensorMeasurement2D() {

    }

    /** Constructur: SensorMeasurement2D(int, double, double, int, double).
     *
     * @param coilidentifier
     *            Coil identifier.
     * @param positionx
     *            Position X.
     * @param positiony
     *            Position Y.
     * @param variableid
     *            Variable identifier.
     * @param variablevalue
     *            Vale for the tuple (x,y). */
    public SensorMeasurement2D(final int coilidentifier, final double positionx,
            final double positiony, final int variableid,
            final double variablevalue) {
        super();
        coilId = coilidentifier;
        x = positionx;
        y = positiony;
        varName = variableid;
        value = variablevalue;
    }

    /** Method: getY().
     *
     * @return */
    public final double getY() {
        return y;
    }

    /** Method: setY().
     *
     * @param positiony
     *            Position Y */
    public final void setY(final double positiony) {
        y = positiony;
    }

    @Override
    public final String toString() {
        return super.toString() + " ----- Row2D [y=" + y + "]";
    }

    @Override
    public final double getX() {
        return x;
    }

    @Override
    public final void setX(final double positionx) {
        x = positionx;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SensorMeasurement2D)) {
            return false;
        }
        SensorMeasurement2D other = (SensorMeasurement2D) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

}
