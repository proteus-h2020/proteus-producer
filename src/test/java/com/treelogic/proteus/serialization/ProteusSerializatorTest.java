package com.treelogic.proteus.serialization;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.treelogic.proteus.model.Row1D;
import com.treelogic.proteus.model.Row2D;

public class ProteusSerializatorTest {

	private ProteusSerializer kryo;

	@Before
	public void initialize() {
		this.kryo = new ProteusSerializer();
	}

	@Test
	public void test1DSerializationAndDeserialization() {
		Row1D row = new Row1D(1, 1, 56, 2121.31);
		byte[] bytes = this.kryo.serialize(null, row);

		Row1D deserialized = (Row1D) this.kryo.deserialize("1D", bytes);
		assertEquals(row, deserialized);
	}

	@Test
	public void test2DSerializationAndDeserialization() {
		Row2D row = new Row2D(13, 31, 6, 54, 12.21);
		byte[] bytes = this.kryo.serialize(null, row);

		Row2D deserialized = (Row2D) this.kryo.deserialize("2D", bytes);
		assertEquals(row, deserialized);
	}

}
