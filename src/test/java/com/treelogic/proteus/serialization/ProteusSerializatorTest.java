package com.treelogic.proteus.serialization;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.treelogic.proteus.model.SensorMeasurement1D;
import com.treelogic.proteus.model.SensorMeasurement2D;

public class ProteusSerializatorTest {

	private ProteusSerializer kryo;

	@Before
	public void initialize() {
		this.kryo = new ProteusSerializer();
	}

	@Test
	public void test1DSerializationAndDeserialization() {
		SensorMeasurement1D row = new SensorMeasurement1D(1, 1, 56, 2121.31);
		byte[] bytes = this.kryo.serialize(null, row);

		SensorMeasurement1D deserialized = (SensorMeasurement1D) this.kryo.deserialize("1D", bytes);
		assertEquals(row, deserialized);
	}

	@Test
	public void test2DSerializationAndDeserialization() {
		SensorMeasurement2D row = new SensorMeasurement2D(13, 31, 6, 54, 12.21);
		byte[] bytes = this.kryo.serialize(null, row);

		SensorMeasurement2D deserialized = (SensorMeasurement2D) this.kryo.deserialize(null, bytes);
		assertEquals(row, deserialized);
	}

}
