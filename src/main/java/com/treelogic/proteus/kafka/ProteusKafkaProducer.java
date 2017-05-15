package com.treelogic.proteus.kafka;

import com.treelogic.proteus.model.HSMMeasurement;
import com.treelogic.proteus.model.Measurement;
import com.treelogic.proteus.model.ProteusData;
import com.treelogic.proteus.model.SensorMeasurement;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class ProteusKafkaProducer {

	private static Properties kafkaProperties = new Properties();
	private static KafkaProducer<Integer, Measurement> producer;
	private static String KAFKA_TOPIC;
	private static String KAKFA_FLATNESS_TOPIC;
	private static String KAKFA_HSM_TOPIC;
	private static final Logger logger = LoggerFactory.getLogger(ProteusKafkaProducer.class);

	static {
		kafkaProperties.put("bootstrap.servers", ProteusData.get("kafka.bootstrapServers"));
		kafkaProperties.put("acks", "all");
		kafkaProperties.put("retries", 0);
		kafkaProperties.put("batch.size", 16384);
		kafkaProperties.put("linger.ms", 1);
		kafkaProperties.put("buffer.memory", 33554432);
		kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		kafkaProperties.put("value.serializer", "com.treelogic.proteus.serialization.ProteusSerializer");
		producer = new KafkaProducer<Integer, Measurement>(kafkaProperties);
		KAFKA_TOPIC = (String) ProteusData.get("kafka.topicName");
		KAKFA_FLATNESS_TOPIC = (String) ProteusData.get("kafka.flatnessTopicName");
		KAKFA_HSM_TOPIC = (String) ProteusData.get("kafka.hsmTopicName");
	}

	private ProteusKafkaProducer() {
	}

	public static void produce(SensorMeasurement row) {
		logger.debug("Producing stream record : " + row);
		producer.send(new ProducerRecord<Integer, Measurement>(KAFKA_TOPIC, row.getCoilId(), row));
	}

	public static void produceFlatness(SensorMeasurement row) {
		logger.debug("Producing flatness : " + row);
		producer.send(new ProducerRecord<Integer, Measurement>(KAKFA_FLATNESS_TOPIC, row.getCoilId(), row));
	}

	public static void produceHSMRecord(HSMMeasurement record) {
		logger.debug("Producing hsm : " + record);
		producer.send(new ProducerRecord<Integer, Measurement>(KAKFA_HSM_TOPIC, record.getCoil(), record));
	}
}
