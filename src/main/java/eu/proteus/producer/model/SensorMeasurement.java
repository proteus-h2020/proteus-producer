package eu.proteus.producer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** @author Nacho <ignacio.g.fernandez@treelogic.com> */

public abstract class SensorMeasurement extends Measurement {

    /** Coil identifier. */
    private int coilId;
    /** Variable name. */
    private int varName;
    /** Vale for sensor. */
    private double value;
    /** Type of sensor. */
    private byte type;
    /** Position x. */
    private double x;

    /** Constructor: SensorMeasurement(). */
    public SensorMeasurement() {
        if (this.getClass() == SensorMeasurement2D.class) {
            this.type = 0x1;
        } else {
            this.type = 0x0;
        }
    }

    /** Method: toJson().
     *
     * @return */
    public final String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Method: getCoilId().
     *
     * @return */
    public final int getCoilId() {
        return coilId;
    }

    /** Method: setCoilId().
     *
     * @param coilidentifier
     *            Coil identifier. */
    public final void setCoilId(final int coilidentifier) {
        coilId = coilidentifier;
    }

    /** Method: getVarName().
     *
     * @return */
    public final int getVarName() {
        return varName;
    }

    /** Method: setVarName().
     *
     * @param variablevalue
     *            Variable name. */
    public final void setVarName(final int variablevalue) {
        varName = variablevalue;
    }

    /** Method: getValue().
     *
     * @return */
    public final double getValue() {
        return value;
    }

    /** Method: getX().
     *
     * @return X position. */
    public final double getX() {
        return x;
    }

    /** Method: setX().
     *
     * @param positionx
     *            X position. */
    public final void setX(final double positionx) {
        x = positionx;
    }

    /** Method: setValue(double).
     *
     * @param positionvalue
     *            Value for the specific position. */
    public final void setValue(final double positionvalue) {
        value = positionvalue;
    }

    /** Method: getType().
     *
     * @return byte which identifies the row as 1D or 2D. */
    public final byte getType() {
        return type;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + coilId;
        result = prime * result + type;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + varName;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SensorMeasurement other = (SensorMeasurement) obj;
        if (coilId != other.coilId) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value)) {
            return false;
        }
        if (varName != other.varName) {
            return false;
        }
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "Row [coilId=" + coilId + ", varName=" + varName + ", value="
                + value + ", type=" + type + ", x=" + x + "]";
    }

}
