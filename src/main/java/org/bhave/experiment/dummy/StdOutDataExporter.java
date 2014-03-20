/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.dummy;

import java.util.List;
import org.bhave.experiment.data.AbstractDataExporter;
import org.bhave.experiment.data.DataExporter;

/**
 * 
 * @author Davide Nunes
 */
public class StdOutDataExporter extends AbstractDataExporter {

	@Override
	public void loadColumns(List<String> columns) {
		System.out.println("Data Columns: " + columns.toString());
	}

	@Override
	public void exportRecord(List<String> data) {
		System.out.println(data.toString());
	}

	@Override
	public void exportRecord(String data) {
		System.out.println("Record: " + data);
	}

	@Override
	public void finish() {
		System.out.println("Dummy Exporter Finished");
	}

	@Override
	protected DataExporter createPrototype() {
		return new StdOutDataExporter();
	}

}
