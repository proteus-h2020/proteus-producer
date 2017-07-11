package eu.proteus.producer.exceptions;

/** @author Treelogic */
public final class IllegalNumberOfColumnsException extends Exception {

    /** Constructor. */
    private IllegalNumberOfColumnsException() {
    }

    /** serialVersionUID. */
    private static final long serialVersionUID = -1022961137880815777L;

    /** Method: IllegalNumberOfcolumnsExcpetion().
     *
     * @param message */
    public IllegalNumberOfColumnsException(final String message) {
        super(message);
    }
}
