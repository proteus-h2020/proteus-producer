package eu.proteus.producer.hdfs;

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

/** @author Treelogic */

public final class HDFS {

    /** Constructor. */
    private HDFS() {
    }

    /** HDFS Base URI. */
    private static String hdfsURI;

    /** Method: getHDFSURI().
     *
     * @return */
    public static String getHDFSURI() {
        return hdfsURI;
    }

    /** Method: setDFSURI().
     *
     * @param hdfsdirection */
    public static void setHDFSURI(final String hdfsdirection) {
        hdfsURI = hdfsdirection;
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
        setHDFSURI((String) ProteusData.get("hdfs.baseUrl"));
        try {
            fs = FileSystem.get(URI.create(getHDFSURI()), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method: readFileInParallel().
     *
     * @param pathFiles
     * @return */
    public static Stream<String> readFilesInParallel(final String[] pathFiles) {
        Stream<String> stream = Stream.empty();

        for (String pathFile : pathFiles) {
            try {
                Stream<String> s = HDFS.readFile(pathFile);
                stream = Stream.concat(stream, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stream;
    }

    /** Method: readFile().
     *
     * @param pathToFile
     * @return
     * @throws IOException */
    public static Stream<String> readFile(final String pathToFile)
            throws IOException {
        Path path = new Path(getHDFSURI() + pathToFile);
        FSDataInputStream inputStream = fs.open(path);
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(inputStream));
        return buffer.lines().skip(1);
    }
}
