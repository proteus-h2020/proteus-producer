package com.treelogic.proteus.kafka.producer;

import com.treelogic.proteus.kafka.offsets.KafkaProducersFactory;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class ProteusKafkaProducer {

	public static String HDFS_URI = "hdfs://192.168.4.245:8020";
	public static String PROTEUS_KAFKA_TOPIC = "proteus";
	public static String PROTEUS_MERGED_TABLE = "/proteus/final/sorted/000000_0";
	public static Double COIL_SPEED = 120000.0;
	public static Integer KAFKA_PRODUCERS = 10;
	public static ArrayList<Integer> coilsIdentifiers;
	public static Integer coilsbyproducer;

	public static void main(String[] args) throws IOException, InterruptedException {

		try {
			if ( !args[0].isEmpty() ) PROTEUS_KAFKA_TOPIC = args[0];
			if ( !args[1].isEmpty() ) COIL_SPEED = Double.parseDouble(args[1]) * 1000;
		} catch (Exception e){
		}


		/* HDFS Configuration */

		Configuration conf = new Configuration();

		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

		FileSystem fs = FileSystem.get(URI.create(HDFS_URI), conf);

		/* END - HDFS Configuration */

		/* Load Coils IDs */

		coilsIdentifiers = getCoilsIDs(fs);

		/* END - Load Coils */

		/* Divide DataSet by KafkaProducers */

		coilsbyproducer =  coilsIdentifiers.size() /  KAFKA_PRODUCERS;


		/* END - Divide DataSet KafkaProducers */

		/* KafkaFactory */

		KafkaProducersFactory kafkafactory = new KafkaProducersFactory();
		kafkafactory.setConfiguration(fs, HDFS_URI, PROTEUS_MERGED_TABLE, conf, COIL_SPEED);
		kafkafactory.createProducers(10, coilsIdentifiers, coilsbyproducer, COIL_SPEED);

		/* END - KafkaFactory */

	}

	public static ArrayList<Integer> getCoilsIDs(FileSystem fs) throws IOException {

		ArrayList<Integer> coilsIdentifiers = new ArrayList<>();

		BufferedReader coilsgetter = new BufferedReader(
				new InputStreamReader(fs.open(new Path(HDFS_URI + "/proteus/final/coilbytable/000000_0"))));

		String linea = coilsgetter.readLine();


		while ( linea != null ){
			linea = coilsgetter.readLine();
			if ( linea != null) { coilsIdentifiers.add(Integer.parseInt(linea)); }
		}

		return coilsIdentifiers;

	}

}
