package eu.proteus.producer.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** @author Treelogic */

public class HSMMeasurement extends Measurement {

    /** Coil identifier. */
    private int coil;
    /** Map with the variables. */
    private Map<String, Object> variables = new HashMap<String, Object>();
    /** Counter of processed variables. */
    private int varCounter;

    /** Constructor.
     *
     * @param coilID
     *            Coil identifier. */
    public HSMMeasurement(final int coilID) {
        this.coil = coilID;
        this.varCounter = 1;
    }

    /** Constructor.
     *
     * @param coilID
     *            Coil identifier.
     * @param mapVariables
     *            Variables to consider. */
    public HSMMeasurement(final int coilID,
            final Map<String, Object> mapVariables) {
        coil = coilID;
        varCounter = variables.size();
        variables = mapVariables;
    }

    /** Method: put().
     *
     * @param value
     *            Variable to include. */
    public final void put(final Object value) {
        String key = String.format("V%d", varCounter++);
        variables.put(key, value);
    }

    /** Method: getCoil().
     *
     * @return */
    public final int getCoil() {
        return coil;
    }

    /** Method. setCoil().
     *
     * @param coilid
     *            Coil identifier. */
    public final void setCoil(final int coilid) {
        coil = coilid;
    }

    /** Method: getVariables().
     *
     * @return Map with all the variables of the coil. */
    public final Map<String, Object> getVariables() {
        return variables;
    }

    /** Method: setVariables().
     *
     * @param mapVariables
     *            Map with all the variables of the coil. */
    public final void setVariables(final Map<String, Object> mapVariables) {
        variables = mapVariables;
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

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + coil;
        result = prime * result + varCounter;
        result = prime * result
                + ((variables == null) ? 0 : variables.hashCode());
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
        HSMMeasurement other = (HSMMeasurement) obj;
        if (coil != other.coil) {
            return false;
        }
        if (varCounter != other.varCounter) {
            return false;
        }
        if (variables == null) {
            if (other.variables != null) {
                return false;
            }
        } else if (!variables.equals(other.variables)) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "HSMMeasurement [coil=" + coil + ", variables=" + variables
                + ", varCounter=" + varCounter + "]";
    }

}
