package com.treelogic.proteus.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Pablo on 22/02/2017.
 */
public class KafkaConsumer {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private static Properties kafkaProperties;
    public static String PROTEUS_KAFKA_TOPIC = "test-timestamp";
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "192.168.4.246:6667,192.168.4.247:6667,192.168.4.248:6667");
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        kafkaProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        kafkaProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        kafkaProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");


        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(kafkaProperties);
        ArrayList<String> topics = new ArrayList<>();
        topics.add(PROTEUS_KAFKA_TOPIC);
        consumer.subscribe(topics);


        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(1);

            for (ConsumerRecord<String, String> record : records) {

                String message_formatted = record.value();

                Coil coil = mapper.readValue(message_formatted, Coil.class);

                String message = mapper.writeValueAsString(coil);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}