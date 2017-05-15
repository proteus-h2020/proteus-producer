package com.treelogic.proteus.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.treelogic.proteus.model.Row;
import com.treelogic.proteus.model.Row1D;
import com.treelogic.proteus.model.Row2D;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Closeable;
import java.util.Map;

public class ProteusSerializer implements Closeable, AutoCloseable, Serializer<Row>, Deserializer<Row> {
	private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			KryoInternalSerializer kryoInternal = new KryoInternalSerializer();
			kryo.addDefaultSerializer(Row.class, kryoInternal);
			kryo.addDefaultSerializer(Row1D.class, kryoInternal);
			kryo.addDefaultSerializer(Row2D.class, kryoInternal);
			return kryo;
		};
	};

	private static final int MAGIC_NUMBER = 0x00687691; // PROTEUS EU id

	@Override
	public void configure(Map<String, ?> map, boolean b) {
	}

	@Override
	public byte[] serialize(String s, Row row) {
		ByteBufferOutput output = new ByteBufferOutput(50); // TODO Max size of
															// the buffer.
															// Optimise it.
		kryos.get().writeObject(output, row);
		return output.toBytes();
	}

	@Override
	public Row deserialize(String topic, byte[] bytes) {
		try {
			return kryos.get().readObject(new ByteBufferInput(bytes), Row.class);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error reading bytes", e);
		}
	}

	@Override
	public void close() {

	}

	private static class KryoInternalSerializer extends com.esotericsoftware.kryo.Serializer<Row> {
		@Override
		public void write(Kryo kryo, Output output, Row row) {

			if (row instanceof Row1D) {
				Row1D cast = (Row1D) row;
				output.writeInt(MAGIC_NUMBER);
				output.writeByte(row.getType());
				output.writeInt(cast.getCoilId());
				output.writeDouble(cast.getX());
				output.writeInt(cast.getVarName());
				output.writeDouble(cast.getValue());
			} else {
				Row2D cast = (Row2D) row;
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
		public Row read(Kryo kryo, Input input, Class<Row> clazz) {
			int magicNumber = input.readInt();
			assert (magicNumber == MAGIC_NUMBER);

			boolean is2D = (input.readByte() == 0x0001f) ? true : false;
			int coilId = input.readInt();
			double x = input.readDouble();
			double y = (is2D) ? input.readDouble() : 0;
			int varId = input.readInt();
			double value = input.readDouble();

			if (is2D) {
				return new Row2D(coilId, x, y, varId, value);
			} else {
				return new Row1D(coilId, x, varId, value);
			}

		}
	}
}