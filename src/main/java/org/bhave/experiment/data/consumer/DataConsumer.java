/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data.consumer;

import java.util.List;

import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.experiment.Configurable;
import org.bhave.experiment.data.DataExporter;

/**
 * @author Davide Nunes
 */
public interface DataConsumer extends Configurable {

	public void consume();

	public Class<? extends DataProducer> getTargetDataProducer();

	public void addExporter(DataExporter exporter);

	public List<DataExporter> getExporters();

	void loadDataProducer(DataProducer producer);

	DataProducer getProducer();

	int getProducerID();

	/**
	 * Cleanup code goes here
	 */
	public void finish();

	public void setID(int id);

	public int getID();
}
