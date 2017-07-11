package eu.proteus.producer.model;

import java.util.Arrays;
import java.util.List;

/** @author Trelogic */
public final class IgnorableCoilIdentifiers {

    /** Constructor: IgnorableCoilIdentifiers(). */
    private IgnorableCoilIdentifiers() {
    }

    /** Values to discard for the simulation streaming. */
    private static List<Integer> discardValues = Arrays
            .asList(new Integer[] { 9, 19, 20, 22, 27, 31, 32, 36, 38, 39, 41,
                    45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56 });

    /** Method: get(). */
    public static List<Integer> get() {
        return discardValues;

    }
}
