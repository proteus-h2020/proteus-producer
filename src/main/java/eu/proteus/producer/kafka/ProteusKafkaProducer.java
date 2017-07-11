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

/** @author Treelogic */

public final class ProteusKafkaProducer {

    /** Kafka Properties for Proteus Producer. */
    private static final Properties KAFKA_PROPERTIES = new Properties();
    /** Kakfa Producer for Proteus. */
    private static final KafkaProducer<Integer, Measurement> PRODUCER;
    /** Kakfa Topic name for realtime simulation. */
    private static final String KAFKA_TOPIC;
    /** Kakfa Topic name for flatness simulation. */
    private static final String KAKFA_FLATNESS_TOPIC;
    /** Kakfa Topic name for hot strip mill simulation. */
    private static final String KAKFA_HSM_TOPIC;
    /** Logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProteusKafkaProducer.class);

    static {
        KAFKA_PROPERTIES.put("bootstrap.servers",
                ProteusData.get("kafka.bootstrapServers"));
        KAFKA_PROPERTIES.put("acks", "all");
        KAFKA_PROPERTIES.put("retries", 0);
        KAFKA_PROPERTIES.put("batch.size", 16384);
        KAFKA_PROPERTIES.put("linger.ms", 1);
        KAFKA_PROPERTIES.put("buffer.memory", 33554432);
        KAFKA_PROPERTIES.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        KAFKA_PROPERTIES.put("value.serializer",
                "eu.proteus.producer.serialization.ProteusSerializer");
        PRODUCER = new KafkaProducer<Integer, Measurement>(KAFKA_PROPERTIES);
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
        LOGGER.debug("Producing stream record : " + row);
        PRODUCER.send(new ProducerRecord<Integer, Measurement>(KAFKA_TOPIC,
                row.getCoilId(), row));
    }

    /** Method: produceFlatness().
     *
     * @param row
     *            Row of data to produce on Kafka. */
    public static void produceFlatness(final SensorMeasurement row) {
        LOGGER.debug("Producing flatness : " + row);
        PRODUCER.send(new ProducerRecord<Integer, Measurement>(
                KAKFA_FLATNESS_TOPIC, row.getCoilId(), row));
    }

    /** Method: produceHSMRecord().
     *
     * @param record
     *            Record of HSM data to produce of Kafka. */
    public static void produceHSMRecord(final HSMMeasurement record) {
        LOGGER.debug("Producing hsm : " + record);
        PRODUCER.send(new ProducerRecord<Integer, Measurement>(KAKFA_HSM_TOPIC,
                record.getCoil(), record));
    }
}
