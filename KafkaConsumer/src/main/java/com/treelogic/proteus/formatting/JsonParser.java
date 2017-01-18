package com.treelogic.proteus.formatting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser<T> implements Parser<T>{

	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String parse(T o) {
		try {
			return this.mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
