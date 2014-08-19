/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.AbstractConfigurablePrototype;
import org.bhave.experiment.data.DataExporter;
import org.bhave.experiment.data.producer.DataProducer;

/**
 * 
 * @author Davide Nunes
 */
public abstract class AbstractDataConsumer extends
		AbstractConfigurablePrototype<DataConsumer> implements DataConsumer {

	List<DataExporter> exporters;
	public static final String P_PRODUCER_ID = "producer";

	public AbstractDataConsumer() {

	}

	@Override
	public void addExporter(DataExporter exporter) {
		if (exporters == null) {
			exporters = new LinkedList<>();
		}
		exporters.add(exporter);
	}

	public List<DataExporter> getExporters() {
		List<DataExporter> rExporters = new ArrayList<>(exporters);
		return rExporters;
	}

	@Override
	public void finish() {
		if (exporters != null) {
			for (DataExporter exporter : exporters) {
				exporter.finish();
			}
		}
	}

	@Override
	public Map<String, Class<? extends Object>> getConfigurableParameters() {
		Map<String, Class<? extends Object>> params = super
				.getConfigurableParameters();
		params.put(P_PRODUCER_ID, Integer.class);
		return params;
	}

	protected DataProducer producer;

	@Override
	public void loadConfiguration(Configuration config) {
		if (!config.containsKey(P_PRODUCER_ID)) {
			throw new RuntimeException(
					"Invalid configuration for data consumer. Please supply a value for the property:"
							+ P_PRODUCER_ID);
		}
		super.loadConfiguration(config);
	}

	/**
	 * Load the data producer object into this consumer this might be useful to
	 * configure the consumer and pass on the data column names in order to
	 * match incoming data with existing property names.
	 * 
	 * @param producer
	 */
	@Override
	public void loadDataProducer(DataProducer producer) {
		this.producer = producer;
	}

	@Override
	public DataProducer getProducer() {
		return producer;
	}

	@Override
	public DataConsumer create() {
		DataConsumer consumer = super.create();
		for (DataExporter exporter : this.exporters) {
			consumer.addExporter(exporter.create());
		}
		return consumer;
	}

	@Override
	public int getProducerID() {
		return config.getInt(P_PRODUCER_ID);
	}

	protected int id;

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}
}
