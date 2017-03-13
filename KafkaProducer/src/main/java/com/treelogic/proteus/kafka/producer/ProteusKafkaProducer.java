package com.treelogic.proteus.kafka.producer;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treelogic.proteus.kafka.model.CoilTimeSeriesPojo;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
//[developeridi@clusterIDI kafka]$ bin/kafka-topics.sh --create --zookeeper 192.168.4.247:2181 --topic proteus --partition 3 --config retention.bytes=10737418240 --replication-factor 1

public class ProteusKafkaProducer {

	public static String HDFS_URI = "hdfs://192.168.4.245:8020";
	public static String PROTEUS_KAFKA_TOPIC = "test-timestamp";
	public static String PROTEUS_MERGED_TABLE = "/proteus/final/sorted/000000_0";

	private static final Logger logger = LoggerFactory.getLogger(ProteusKafkaProducer.class);
	private static Producer<String, String> producer;

	public static void main(String[] args) throws IOException {
		
		logger.info("Starting Proteus Kafka producer...");
		
		int loopIteration = 1;
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				"192.168.4.246:6667,192.168.4.247:6667,192.168.4.248:6667");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 100);

		producer = new KafkaProducer<>(properties);

		// Configuracion HDFS

		Configuration conf = new Configuration();

		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

		FileSystem fs = FileSystem.get(URI.create(HDFS_URI), conf);

		ObjectMapper mapper = new ObjectMapper();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
		mapper.setDateFormat(df);

		ProducerLogic logic = new ProducerLogic();

		// Read line by line HDFS

		while (true) {
			logger.info("Starting a new kafka iteration over the HDFS: ", (loopIteration++));
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(fs.open(new Path(HDFS_URI + PROTEUS_MERGED_TABLE))));

			try {
				// La primera línea del CSV es una cabecera
				String line = br.readLine();

				// Primera linea a procesar
				line = br.readLine();
				while (line != null) {

					String [] mensaje = line.split(",");
					Coil coil = new Coil().generateCoilObject(mensaje);
					logic.buffer(coil, producer, PROTEUS_KAFKA_TOPIC);
					line = br.readLine();

				}
			} catch (Exception e) {
				logger.error("Error in the Proteus Kafka producer", e);
			}
		}
	}



}
