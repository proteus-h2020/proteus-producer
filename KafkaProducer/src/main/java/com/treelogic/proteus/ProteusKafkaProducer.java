package com.treelogic.proteus;

import com.treelogic.proteus.model.HSMRecord;
import com.treelogic.proteus.model.Row;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class ProteusKafkaProducer {

    private static Properties kafkaProperties = new Properties();
    private static KafkaProducer producer;
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
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(kafkaProperties);
        KAFKA_TOPIC = (String) ProteusData.get("kafka.topicName");
        KAKFA_FLATNESS_TOPIC = (String) ProteusData.get("kafka.flatnessTopicName");
        KAKFA_HSM_TOPIC = (String) ProteusData.get("kafka.hsmTopicName");
    }

    private ProteusKafkaProducer() {
    }

    public static void produce(Row row) {
        logger.debug("Producing stream record : " + row);
        producer.send(new ProducerRecord(KAFKA_TOPIC, row.toJson()));
    }

    public static void produceFlatness(Row row) {
        logger.debug("Producing flatness : " + row);
        producer.send(new ProducerRecord(KAKFA_FLATNESS_TOPIC, row.getCoilId(), row.toJson()));
    }

    public static void produceHSMRecord(HSMRecord record) {
        logger.debug("Producing hsm : " + record);
        producer.send(new ProducerRecord(KAKFA_HSM_TOPIC, record.getCoil(), record.toJson()));
    }
}
