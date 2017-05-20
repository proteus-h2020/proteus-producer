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

public class HDFSSourceStrategy implements SourceStrategy {

	/**
	 * HDFS Base URI
	 */
	public static String HDFS_URI;

	/**
	 * Hadoop configuration instance
	 */
	private static Configuration conf = new Configuration();

	/**
	 * Hadoop filesystem pointer
	 */
	private static FileSystem fs;

	static {
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
		HDFS_URI = (String) ProteusData.get("hdfs.baseUrl");
		try {
			fs = FileSystem.get(URI.create(HDFS_URI), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stream<String> readFilesInParallel(String[] pathFiles) {
		Stream<String> stream = Stream.empty();

		for (String pathFile : pathFiles) {
			Stream<String> s = this.readFile(pathFile);
			stream = Stream.concat(stream, s);
		}
		return stream;
	}

	public Stream<String> readFile(String pathToFile) {
		Path path = new Path(HDFS_URI + pathToFile);
		FSDataInputStream inputStream = null;
		try {
			inputStream = fs.open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
		return buffer.lines();
	}

}
