package eu.proteus.producer.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class LocalSourceStrategy implements SourceStrategy<String> {

	@Override
	public Stream<String> readFile(String filePath) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
		return buffer.lines();
	}

}
