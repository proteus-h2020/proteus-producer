package com.treelogic.proteus.model;

public class HSMRecordMapper {

	private static final String SPLITTER = (String) ProteusData.get("model.hsm.splitter");

	public static HSMRecord map(String record) {
		String[] lineSplit = record.split(SPLITTER);
		int coilID = Integer.parseInt(lineSplit[0]);

		HSMRecord hsmRecord = new HSMRecord(coilID);

		for (String line : lineSplit) {
			hsmRecord.put(line);
		}

		return hsmRecord;
	}
}
