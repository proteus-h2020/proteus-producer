package com.treelogic.proteus.kafka;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.fasterxml.jackson.databind.ObjectMapper;


public class EventSource implements  ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher = null;
	private Properties kafkaProperties;
	public static String PROTEUS_KAFKA_TOPIC = "proteus-test";
	private static ObjectMapper mapper = new ObjectMapper();

	public EventSource() {
		this.kafkaProperties = new Properties();
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
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
		//Run in a separated thread  after DI
		Executors.newSingleThreadExecutor().execute(new Runnable() {
		    @Override
		    public void run() {
                try {
                    execute();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
		});
		
	}

	public void execute() throws JsonProcessingException {
		System.out.println();
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.kafkaProperties);
		ArrayList<String> topics = new ArrayList<>();
		topics.add(PROTEUS_KAFKA_TOPIC);
		consumer.subscribe(topics);

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1);
			for (ConsumerRecord<String, String> record : records) {

				String message_formatted = record.value();

				String[] fields = message_formatted.split(",");

				Coil coil = new Coil().generateCoilObject(fields);

				String message = mapper.writeValueAsString(coil);

				this.applicationEventPublisher.publishEvent(new KafkaMessageEvent(this, message));
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}