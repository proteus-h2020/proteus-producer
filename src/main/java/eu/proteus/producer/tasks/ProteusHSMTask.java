package eu.proteus.producer.tasks;

import eu.proteus.producer.hdfs.HDFS;
import eu.proteus.producer.kafka.ProteusKafkaProducer;
import eu.proteus.producer.model.*;

import java.io.IOException;
import java.util.stream.Stream;

public class ProteusHSMTask extends ProteusTask {
	/**
	 * Path to the PROTEUS HSM data
	 */
	private String hsmFilePath;

	/**
	 * Coil ID for the current HSM
	 */
	private int coilId;

	public ProteusHSMTask(String hsmFilePath, int coilId) {
		super();
		this.hsmFilePath = hsmFilePath;
		this.coilId = coilId;
	}

	@Override
	public void run() {
		Stream<String> stream = null;
		String fullpath = String.format(this.hsmFilePath, this.coilId);
		try {
			stream = HDFS.readFile(fullpath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		stream
			.map(HSMMeasurementMapper::map)
			//.filter(this::filterByCoil)
			.forEach(ProteusKafkaProducer::produceHSMRecord); //Only one record
	}
/**
	private boolean filterByCoil(HSMMeasurement record) {
		return record.getCoil() == coilId;
	}
**/
}
