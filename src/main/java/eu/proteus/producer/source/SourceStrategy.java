package eu.proteus.producer.source;

import java.util.stream.Stream;

/** @author Nacho <ignacio.g.fernandez@treelogic.com>
 *
 * @param <T> */

public interface SourceStrategy<T> {

    /** Method: readFile.
     *
     * @param filePath
     * @return */
    Stream<T> readFile(String filePath);
}
