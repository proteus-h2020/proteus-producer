package com.treelogic.proteus;

import com.treelogic.proteus.model.Row;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by ignacio.g.fernandez on 2/05/17.
 */
public class ProteusKafkaProducer {

    private static Properties kafkaProperties = new Properties();
    private static KafkaProducer producer;
    private static String KAFKA_TOPIC;

    static {
        kafkaProperties.put("bootstrap.servers", "localhost:9092");
        kafkaProperties.put("acks", "all");
        kafkaProperties.put("retries", 0);
        kafkaProperties.put("batch.size", 16384);
        kafkaProperties.put("linger.ms", 1);
        kafkaProperties.put("buffer.memory", 33554432);
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(kafkaProperties);
        KAFKA_TOPIC = (String) ProteusData.get("kafka.topicName");

    }

    private ProteusKafkaProducer() {
    }

    public static void produce(Row row) {
        producer.send(new ProducerRecord(KAFKA_TOPIC, row.toJson()));
    }
}
