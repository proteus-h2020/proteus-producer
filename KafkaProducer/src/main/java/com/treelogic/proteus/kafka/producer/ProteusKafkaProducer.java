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
import java.util.Properties;

public class ProteusKafkaProducer {

	public static String PROTEUS_TABLE = "/developeridi/formacion/materiales/mifichero.txt";
	public static String HDFS_URI = "hdfs://192.168.4.245:8020";
	public static String PROTEUS_KAFKA_TOPIC = "proteus-test";
	public static boolean TEST = true;
	public static float SEND_SPEED = 1;
	public static int LOOPS = 1;
	public static String PROTEUS_HSM = "/proteus/originaldataset/hsm_data/part-00000.csv";
	public static String PROTEUS_COILTIMESERIES = "/proteus/originaldataset/coiltimeseries/C0001/42252001-C0001.csv";
	public static String PROTEUS_MERGED_TABLE = "/proteus/kafkainput/coiltimeseries.csv/000000_0";
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

		// Read line by line HDFS

		while (TEST == true) {
			logger.info("Starting a new kafka iteration over the HDFS: ", (loopIteration++));
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(fs.open(new Path(HDFS_URI + PROTEUS_MERGED_TABLE))));

			try {
				// La primera línea del CSV es una cabecera
				String line = br.readLine();

				// Primera linea a procesar
				line = br.readLine();
				while (line != null) {
					String[] fields = line.split(",");

					CoilTimeSeriesPojo<String> pojo_message = new CoilTimeSeriesPojo<String>(fields[0], fields[1],
							fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], fields[8], fields[9],
							fields[10], fields[11], fields[12], fields[13], fields[14], fields[15], fields[16],
							fields[17], fields[18], fields[19], fields[20], fields[21], fields[22], fields[23],
							fields[24], fields[25], fields[26], fields[27], fields[28], fields[29], fields[30],
							fields[31], fields[32], fields[33], fields[34], fields[35], fields[36], fields[37],
							fields[38], fields[39], fields[40], fields[41], fields[42], fields[43], fields[44],
							fields[45]);

					String message = mapper.writeValueAsString(pojo_message);
					producer.send(new ProducerRecord<String, String>(PROTEUS_KAFKA_TOPIC, message));

					line = br.readLine();

				}
			} catch (Exception e) {
				logger.error("Error in the Proteus Kafka producer", e);
			}
		}
	}
}