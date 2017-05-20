package eu.proteus.producer.source;

import java.util.stream.Stream;
/**
 * 
 * @author nach0
 *
 * @param <T>
 */
public interface SourceStrategy <T>{

	
	public Stream<T> readFile(String filePath);
}
