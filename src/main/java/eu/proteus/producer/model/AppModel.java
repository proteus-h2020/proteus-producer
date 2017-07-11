package eu.proteus.producer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** @author Nacho <ignacio.g.fernandez@treelogic.com> */

public class AppModel {

    /** Current coil identifier. */
    private Integer aliveCoil;

    /** Last coil row. */
    private SensorMeasurement lastCoilRow;

    /** Start timestamp of the last coil. */
    private Date lastCoilStart;

    /** Production status. */
    private ProductionStatus status;

    /** Current rows. */
    private List<SensorMeasurement> currentFlatnessRows;

    /** Delay between coils production start. */
    private double delay;

    /** Constructor. */
    public AppModel() {
        this.aliveCoil = null;
        this.status = ProductionStatus.AWAITING;
        this.delay = 0.0D;
        this.lastCoilRow = null;
        this.currentFlatnessRows = new ArrayList<SensorMeasurement>();
    }

    /** Method: getAliveCoil().
     *
     * @return */
    public final Integer getAliveCoil() {
        return aliveCoil;
    }

    /** Method: setAliveCoil().
     *
     * @param currentCoilID
     *            Coil identifier. */
    public final void setAliveCoil(final Integer currentCoilID) {
        aliveCoil = currentCoilID;
    }

    /** Method: getStatus().
     *
     * @return */
    public final ProductionStatus getStatus() {
        return status;
    }

    /** Method setStatus().
     *
     * @param productionStatus
     *            Production status. */
    public final void setStatus(final ProductionStatus productionStatus) {
        status = productionStatus;
    }

    /** Method: getDelay().
     *
     * @return */
    public final double getDelay() {
        return delay;
    }

    /** Method: setDelay().
     *
     * @param delayTime
     *            Set delay time. */
    public final void setDelay(final double delayTime) {
        delay = delayTime;
    }

    /** Method: getLastCoilRow().
     *
     * @return */
    public final SensorMeasurement getLastCoilRow() {
        return lastCoilRow;
    }

    /** Method setLastCoilRow().
     *
     * @param lastRow
     *            Last coil row received. */
    public final void setLastCoilRow(final SensorMeasurement lastRow) {
        lastCoilRow = lastRow;
    }

    /** Method: getLastCoilStart().
     *
     * @return */
    public final Date getLastCoilStart() {
        return lastCoilStart;
    }

    /** Method setLastCoilStart().
     *
     * @param lastCoilBegin
     *            Date of the last coil production start. */
    public final void setLastCoilStart(final Date lastCoilBegin) {
        lastCoilStart = lastCoilBegin;
    }

    /** Method: getCurrentFlatnessRows().
     *
     * @return */
    public final List<SensorMeasurement> getCurrentFlatnessRows() {
        return currentFlatnessRows;
    }

    /** Method: setCurrentFlatnessRow.
     *
     * @param currentFlatness
     *            List with the current flatness rows. */
    public final void setCurrentFlatnessRows(
            final List<SensorMeasurement> currentFlatness) {
        currentFlatnessRows = currentFlatness;
    }

    /** @author Nacho <ignacio.g.fernandez@treelogic.com> */
    public enum ProductionStatus {
        /** Status available: PRODUCTION: Coil in production. AWAITING: Coil
         * awaiting to be on production. */
        PRODUCING, AWAITING
    }
}
