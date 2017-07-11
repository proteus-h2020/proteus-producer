package eu.proteus.producer.serialization;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import eu.proteus.producer.model.HSMMeasurement;
import eu.proteus.producer.model.Measurement;
import eu.proteus.producer.model.ProteusData;
import eu.proteus.producer.model.SensorMeasurement;
import eu.proteus.producer.model.SensorMeasurement1D;
import eu.proteus.producer.model.SensorMeasurement2D;

/** @author Treelogic */

public class ProteusSerializer implements Closeable, AutoCloseable,
        Serializer<Measurement>, Deserializer<Measurement> {

    /** Thread-safe kryo instance that handles, serializes and deserializes
     * PROTEUS POJOS. */
    private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            SensorMeasurementInternalSerializer sensorInternal;
            sensorInternal = new SensorMeasurementInternalSerializer();
            HSMMeasurementInternalSerializer hsmInternal;
            hsmInternal = new HSMMeasurementInternalSerializer();

            kryo.addDefaultSerializer(HSMMeasurement.class, hsmInternal);

            kryo.addDefaultSerializer(SensorMeasurement.class, sensorInternal);
            kryo.addDefaultSerializer(SensorMeasurement1D.class,
                    sensorInternal);
            kryo.addDefaultSerializer(SensorMeasurement2D.class,
                    sensorInternal);
            return kryo;
        };
    };

    /** The MAGIC_NUMBER, with the value of the PROTEUS project
     * identificator. */
    private static final int MAGIC_NUMBER = 0x00687691; // PROTEUS EU id

    @Override
    public void configure(final Map<String, ?> map, final boolean b) {
    }

    @Override
    public final byte[] serialize(final String topic,
            final Measurement record) {
        int byteBufferLength = 50;
        if (record instanceof HSMMeasurement) {
            /** "TODO: Improve" */
            byteBufferLength = 7600 * 2 * 100;

        }
        ByteBufferOutput output = new ByteBufferOutput(byteBufferLength);
        kryos.get().writeObject(output, record);
        return output.toBytes();
    }

    @Override
    public final Measurement deserialize(final String topic,
            final byte[] bytes) {
        if (topic.equals(ProteusData.get("kafka.topicName"))) {
            return kryos.get().readObject(new ByteBufferInput(bytes),
                    SensorMeasurement.class);
        } else if (topic.equals(ProteusData.get("kafka.flatnessTopicName"))) {
            return kryos.get().readObject(new ByteBufferInput(bytes),
                    SensorMeasurement.class);
        } else if (topic.equals(ProteusData.get("kafka.hsmTopicName"))) {
            return kryos.get().readObject(new ByteBufferInput(bytes),
                    HSMMeasurement.class);
        } else {
            throw new IllegalArgumentException("Illegal argument: " + topic);
        }
    }

    @Override
    public void close() {

    }

    /** @author Treelogic */
    private static class SensorMeasurementInternalSerializer
            extends com.esotericsoftware.kryo.Serializer<SensorMeasurement> {
        @Override
        public void write(final Kryo kryo, final Output output,
                final SensorMeasurement row) {
            if (row instanceof SensorMeasurement1D) {
                SensorMeasurement1D cast = (SensorMeasurement1D) row;
                output.writeInt(MAGIC_NUMBER);
                output.writeByte(row.getType());
                output.writeInt(cast.getCoilId());
                output.writeDouble(cast.getX());
                output.writeInt(cast.getVarName());
                output.writeDouble(cast.getValue());
            } else {
                SensorMeasurement2D cast = (SensorMeasurement2D) row;
                output.writeInt(MAGIC_NUMBER);
                output.writeByte(row.getType());
                output.writeInt(cast.getCoilId());
                output.writeDouble(cast.getX());
                output.writeDouble(cast.getY());
                output.writeInt(cast.getVarName());
                output.writeDouble(cast.getValue());
            }
        }

        @Override
        public SensorMeasurement read(final Kryo kryo, final Input input,
                final Class<SensorMeasurement> clazz) {
            int magicNumber = input.readInt();
            assert (magicNumber == MAGIC_NUMBER);

            boolean is2D = (input.readByte() == 0x0001) ? true : false;
            int coilId = input.readInt();
            double x = input.readDouble();
            double y = (is2D) ? input.readDouble() : 0;
            int varId = input.readInt();
            double value = input.readDouble();

            if (is2D) {
                return new SensorMeasurement2D(coilId, x, y, varId, value);
            } else {
                return new SensorMeasurement1D(coilId, x, varId, value);
            }

        }
    }

    /** @author Treelogic */
    private static class HSMMeasurementInternalSerializer
            extends com.esotericsoftware.kryo.Serializer<HSMMeasurement> {
        @Override
        public void write(final Kryo kryo, final Output output,
                final HSMMeasurement hsmRecord) {
            output.writeInt(hsmRecord.getCoil());
            kryo.writeObject(output, hsmRecord.getVariables());
        }

        @Override
        public HSMMeasurement read(final Kryo kryo, final Input input,
                final Class<HSMMeasurement> clazz) {
            int coil = input.readInt();
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = kryo.readObject(input,
                    HashMap.class);
            HSMMeasurement hsmRecord = new HSMMeasurement(coil);
            hsmRecord.setVariables(variables);
            return hsmRecord;
        }
    }
}
