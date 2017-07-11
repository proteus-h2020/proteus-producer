package eu.proteus.producer.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Stream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import eu.proteus.producer.model.ProteusData;

/** @author Nacho <ignacio.g.fernandez@treelogic.com> */

public class HDFSSourceStrategy implements SourceStrategy {

    /** HDFS Base URI. */
    private static String hdfsURI;

    /** HDFS Setter.
     *
     * @param hdfsPath
     *            Path to the HDFS location. */

    public static void setHDFS(final String hdfsPath) {
        hdfsURI = hdfsPath;
    }

    /** HDFS Getter.
     *
     * @return */
    public static String getHDFS() {
        return hdfsURI;
    }

    /** Hadoop configuration instance. */
    private static Configuration conf = new Configuration();

    /** Hadoop filesystem pointer. */

    private static FileSystem fs;

    static {
        conf.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",
                org.apache.hadoop.fs.LocalFileSystem.class.getName());
        setHDFS((String) ProteusData.get("hdfs.baseUrl"));
        try {
            fs = FileSystem.get(URI.create(getHDFS()), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method: readFilesInParallel.
     *
     * @param pathFiles
     *            String Array with paths.
     * @return */
    public final Stream<String> readFilesInParallel(final String[] pathFiles) {
        Stream<String> stream = Stream.empty();

        for (String pathFile : pathFiles) {
            Stream<String> s = this.readFile(pathFile);
            stream = Stream.concat(stream, s);
        }
        return stream;
    }

    @Override
    public final Stream<String> readFile(final String pathToFile) {
        BufferedReader buffer;
        Path path = new Path(getHDFS() + pathToFile);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = new BufferedReader(new InputStreamReader(inputStream));
        return buffer.lines();
    }

}
