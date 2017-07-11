package eu.proteus.producer.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.proteus.producer.model.HSMMeasurement;
import eu.proteus.producer.model.Measurement;
import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.model.SensorMeasurement;

/** @author Nacho <ignacio.g.fernandez@treelogic.com> */

public class ProteusKafkaProducer {

    /** Kafka Properties for Proteus Producer. */
    private static final Properties kafkaProperties = new Properties();
    /** Kakfa Producer for Proteus. */
    private static final KafkaProducer<Integer, Measurement> producer;
    /** Kakfa Topic name for realtime simulation. */
    private static final String KAFKA_TOPIC;
    /** Kakfa Topic name for flatness simulation. */
    private static final String KAKFA_FLATNESS_TOPIC;
    /** Kakfa Topic name for hot strip mill simulation. */
    private static final String KAKFA_HSM_TOPIC;
    /** Logger */
    private static final Logger logger = LoggerFactory
            .getLogger(ProteusKafkaProducer.class);

    static {
        kafkaProperties.put("bootstrap.servers",
                ProteusData.get("kafka.bootstrapServers"));
        kafkaProperties.put("acks", "all");
        kafkaProperties.put("retries", 0);
        kafkaProperties.put("batch.size", 16384);
        kafkaProperties.put("linger.ms", 1);
        kafkaProperties.put("buffer.memory", 33554432);
        kafkaProperties.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        kafkaProperties.put("value.serializer",
                "eu.proteus.producer.serialization.ProteusSerializer");
        producer = new KafkaProducer<Integer, Measurement>(kafkaProperties);
        KAFKA_TOPIC = (String) ProteusData.get("kafka.topicName");
        KAKFA_FLATNESS_TOPIC = (String) ProteusData
                .get("kafka.flatnessTopicName");
        KAKFA_HSM_TOPIC = (String) ProteusData.get("kafka.hsmTopicName");
    }

    /** Consctructor: ProteusKakfaProducer. */
    private ProteusKafkaProducer() {
    }

    /** Method: produce().
     *
     * @param row
     *            Row of data to produce on Kafka. */
    public static void produce(final SensorMeasurement row) {
        logger.debug("Producing stream record : " + row);
        producer.send(new ProducerRecord<Integer, Measurement>(KAFKA_TOPIC,
                row.getCoilId(), row));
    }

    /** Method: produceFlatness().
     *
     * @param row
     *            Row of data to produce on Kafka. */
    public static void produceFlatness(final SensorMeasurement row) {
        logger.debug("Producing flatness : " + row);
        producer.send(new ProducerRecord<Integer, Measurement>(
                KAKFA_FLATNESS_TOPIC, row.getCoilId(), row));
    }

    /** Method: produceHSMRecord().
     *
     * @param record
     *            Record of HSM data to produce of Kafka. */
    public static void produceHSMRecord(final HSMMeasurement record) {
        logger.debug("Producing hsm : " + record);
        producer.send(new ProducerRecord<Integer, Measurement>(KAKFA_HSM_TOPIC,
                record.getCoil(), record));
    }
}
