package com.treelogic.proteus.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Pablo on 22/02/2017.
 */
public class KafkaConsumer {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private static Properties kafkaProperties;
    public static String PROTEUS_KAFKA_TOPIC = "proteus-test";
    public static long DELAY = 10;
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

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
            System.out.println("While");

            ConsumerRecords<String, String> records = consumer.poll(1);
            System.out.println("Records: " + records.isEmpty());

            for (ConsumerRecord<String, String> record : records) {

                String message_formatted = record.value();

                String[] fields = message_formatted.split(",");


                if (fields[1] == null || fields[1].equals("null")) {
                    fields[1] = "0";
                }

                Coil pojo = new Coil(Integer.parseInt(fields[0]),
                        Double.parseDouble(fields[1]),
                        Double.parseDouble(fields[2]),
                        Double.parseDouble(fields[3]),
                        Double.parseDouble(fields[4]),
                        Double.parseDouble(fields[5]),
                        Double.parseDouble(fields[6]),
                        Double.parseDouble(fields[7]),
                        Double.parseDouble(fields[8]),
                        Double.parseDouble(fields[9]),
                        Double.parseDouble(fields[10]),
                        Double.parseDouble(fields[11]),
                        Double.parseDouble(fields[12]),
                        Double.parseDouble(fields[13]),
                        Double.parseDouble(fields[14]),
                        Double.parseDouble(fields[15]),
                        Double.parseDouble(fields[16]),
                        Double.parseDouble(fields[17]),
                        Double.parseDouble(fields[18]),
                        Double.parseDouble(fields[19]),
                        Double.parseDouble(fields[20]),
                        Double.parseDouble(fields[21]),
                        Double.parseDouble(fields[22]),
                        Double.parseDouble(fields[23]),
                        Double.parseDouble(fields[24]),
                        Double.parseDouble(fields[25]),
                        Double.parseDouble(fields[26]),
                        Double.parseDouble(fields[27]),
                        Double.parseDouble(fields[28]),
                        Double.parseDouble(fields[29]),
                        Double.parseDouble(fields[30]),
                        Double.parseDouble(fields[31]),
                        Double.parseDouble(fields[32]),
                        Double.parseDouble(fields[33]),
                        Double.parseDouble(fields[34]),
                        Double.parseDouble(fields[35]),
                        Double.parseDouble(fields[36]),
                        Double.parseDouble(fields[37]),
                        Double.parseDouble(fields[38]),
                        Double.parseDouble(fields[39]),
                        Double.parseDouble(fields[40]),
                        Double.parseDouble(fields[41]),
                        Double.parseDouble(fields[42]),
                        Double.parseDouble(fields[43]),
                        Double.parseDouble(fields[44]),
                        Double.parseDouble(fields[45]),
                        Double.parseDouble(fields[46]),
                        Double.parseDouble(fields[47]),
                        Double.parseDouble(fields[48]),
                        Double.parseDouble(fields[49]),
                        Double.parseDouble(fields[50]),
                        Double.parseDouble(fields[51]),
                        Double.parseDouble(fields[52]),
                        Double.parseDouble(fields[53]),
                        Double.parseDouble(fields[54]),
                        Double.parseDouble(fields[55]),
                        Double.parseDouble(fields[56]),
                        Double.parseDouble(fields[57]),
                        Double.parseDouble(fields[58]));

                String message = null;
                try {
                    message = mapper.writeValueAsString(pojo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                System.out.println("Mensaje: " + message);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}