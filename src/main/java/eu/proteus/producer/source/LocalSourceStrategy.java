package eu.proteus.producer.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/** @author Nacho <ignacio.g.fernandez@treelogic.com> */

public class LocalSourceStrategy implements SourceStrategy<String> {

    @Override
    public final Stream<String> readFile(final String filePath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(inputStream));
        return buffer.lines();
    }

}
