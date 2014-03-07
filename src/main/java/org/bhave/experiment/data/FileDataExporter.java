/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 
 * @author Davide Nunes
 */
public class FileDataExporter extends AbstractDataExporter {

	private List<String> columns;

	// the name of the file receiving the records
	public static final String P_FILENAME = "file.name";
	// boolean determines of the data is to be appended to an existing file
	public static final String P_APPEND = "file.append";

	public static final String DEFAULT_FILE_PREFIX = "experiment-data";

	private File currentFile;
	private BufferedWriter writer;

	@Override
	public void loadColumns(List<String> columns) {
		this.columns = new ArrayList<>(columns);
	}

	private int numExported;

	public FileDataExporter() {
		numExported = 0;
	}

	@Override
	public void exportRecord(List<String> data) {
		if (config == null) {
			// create the default configuration
			loadConfiguration(new PropertiesConfiguration());
		}

		if (currentFile == null) {
			// init the file descriptor
			currentFile = new File(config.getString(P_FILENAME));
			try {
				writer = new BufferedWriter(new FileWriter(currentFile,
						config.getBoolean(P_APPEND)));
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		// export the record
		// write the file header
		if (numExported == 0 && columns != null) {
			try {

				for (String column : columns) {

					writer.write(column);
					writer.write(';');

				}
				writer.newLine();
			} catch (IOException ex) {
				Logger.getLogger(FileDataExporter.class.getName()).log(
						Level.SEVERE, "could not write the file header", ex);
			}
		}

		// write the actuall record line
		try {
			for (String dataColumn : data) {

				writer.write(dataColumn);
				writer.write(';');

			}

			writer.newLine();
		} catch (IOException ex) {
			Logger.getLogger(FileDataExporter.class.getName()).log(
					Level.SEVERE,
					"could not write the data record " + numExported, ex);
		}

		numExported++;
	}

	@Override
	public void exportRecord(String data) {
		if (config == null) {
			// create the default configuration
			loadConfiguration(new PropertiesConfiguration());
		}

		if (currentFile == null) {
			// init the file descriptor
			currentFile = new File(config.getString(P_FILENAME));
			try {
				writer = new BufferedWriter(new FileWriter(currentFile,
						config.getBoolean(P_APPEND)));
			} catch (IOException ex) {
				Logger.getLogger(FileDataExporter.class.getName())
						.log(Level.SEVERE,
								"File data exporter failled to create the file writter",
								ex);
			}
		}

		// export the record
		// write the file header
		if (numExported == 0 && columns != null) {
			try {

				for (String column : columns) {

					writer.write(column);
					writer.write(';');

				}
				writer.newLine();
			} catch (IOException ex) {
				Logger.getLogger(FileDataExporter.class.getName()).log(
						Level.SEVERE, "could not write the file header", ex);
			}
		}

		// write the actuall record line
		try {

			writer.write(data);
			writer.newLine();
			// writer.flush();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		numExported++;
	}

	/**
	 * Loads a configuration file into the file data exporter, if the values
	 * necessary to configure the file data exporter are not found in the
	 * supplied configuration a default configuration is created instead
	 * 
	 * @param config
	 *            a configuration object to be used for this file data exporter
	 */
	@Override
	public void loadConfiguration(Configuration config) {
		// create a deep copy of the current configuration
		super.loadConfiguration(config);
		// if a filename was not given, use the default file name
		if (!config.containsKey(P_FILENAME)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Calendar cal = Calendar.getInstance();

			StringBuilder sb = new StringBuilder(DEFAULT_FILE_PREFIX);
			sb.append('_').append(dateFormat.format(cal.getTime()));
			sb.append(".csv");
			this.config.setProperty(P_FILENAME, sb.toString());
		}
		if (!config.containsKey(P_APPEND)) {
			this.config.setProperty(P_APPEND, true);
		}
		System.out.println(this.config.containsKey(P_APPEND));
	}

	@Override
	public Map<String, Class<? extends Object>> getConfigurableParameters() {
		Map<String, Class<? extends Object>> params = new HashMap<>();
		params.put(P_FILENAME, String.class);
		params.put(P_APPEND, Boolean.class);
		return params;
	}

	@Override
	public void finish() {
		if (writer != null) {
			try {
				writer.close();
				currentFile = null;
				writer = null;
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Returns the current file descriptor
	 * 
	 * @return a file descriptor
	 */
	public File getCurrentFile() {
		return currentFile;
	}

	@Override
	protected DataExporter createPrototype() {
		return new FileDataExporter();
	}

	@Override
	public Configuration getConfiguration() {
		if (config == null) {
			this.loadConfiguration(new PropertiesConfiguration());
		}
		return config;
	}

}
