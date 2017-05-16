package com.treelogic.proteus.serialization;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.treelogic.proteus.model.HSMMeasurement;
import com.treelogic.proteus.model.SensorMeasurement1D;
import com.treelogic.proteus.model.SensorMeasurement2D;

import scala.util.Random;

public class ProteusSerializatorTest {

	private ProteusSerializer kryo;

	@Before
	public void initialize() {
		this.kryo = new ProteusSerializer();
	}

	@Test
	public void test1DSerializationAndDeserialization() {
		SensorMeasurement1D row = new SensorMeasurement1D(1, 1, 56, 2121.31);
		byte[] bytes = this.kryo.serialize("proteus-realtime", row);

		SensorMeasurement1D deserialized = (SensorMeasurement1D) this.kryo.deserialize("proteus-realtime", bytes);
		assertEquals(row, deserialized);
	}

	@Test
	public void test2DSerializationAndDeserialization() {
		SensorMeasurement2D row = new SensorMeasurement2D(13, 31, 6, 54, 12.21);
		byte[] bytes = this.kryo.serialize("proteus-realtime", row);

		SensorMeasurement2D deserialized = (SensorMeasurement2D) this.kryo.deserialize("proteus-realtime", bytes);
		assertEquals(row, deserialized);
	}
	
	@Test
	public void testHSMSerializationAndDeserialization() {
		int coilId = ThreadLocalRandom.current().nextInt(3000, 8000);
		HSMMeasurement record = new HSMMeasurement(coilId);
		
		Map<String, Object> map = createFakeHSMValues();
		record.setVariables(map);		
		
		byte[] bytes = this.kryo.serialize("proteus-hsm", record);

		HSMMeasurement deserialized = (HSMMeasurement) this.kryo.deserialize("proteus-hsm", bytes);
		
		assertEquals(record, deserialized);
	}
	
	private Map<String, Object> createFakeHSMValues(){
		int size = ThreadLocalRandom.current().nextInt(3000, 8000);
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(int i = 0; i < size; i++){
			String varname = "V"+i;
			map.put(varname, new Integer(i));
		}
		return map;
	}

}
