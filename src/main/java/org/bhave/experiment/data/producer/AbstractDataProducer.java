/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data.producer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.Model;
import org.bhave.experiment.AbstractConfigurablePrototype;
import org.bhave.experiment.data.Statistics;
import org.bhave.sweeper.CombinedParameterSweep;

/**
 * 
 * @author Davide Nunes
 */
public abstract class AbstractDataProducer extends
		AbstractConfigurablePrototype<DataProducer> implements DataProducer {

	protected int id;

	public static final String P_STATS = "stats";
	
	protected List<Statistics> statList;


	/**
	 * Add a statistics object to record certain properties of the model
	 * 
	 * @param stats
	 *            a {@link Statistics} object
	 */
	@Override
	public void addStatistics(Statistics stats) {
		if (statList == null) {
			statList = new LinkedList<>();
		}
		statList.add(stats);
	}

	/**
	 * 
	 * @return a set of statistics objects
	 */
	@Override
	public List<Statistics> getStatistics() {
		return new LinkedList<>(statList);
	}

	@Override
	public void produce(Model model) {
		Properties data = new Properties();
		for (Statistics stat : statList) {
			Properties statProps = stat.measure(model);
			mergeProperties(data, statProps);
		}
		forwardData(model, data);

	}

	private void mergeProperties(Properties base, Properties newProps) {
		for (Object key : newProps.keySet()) {
			base.put(key, newProps.get(key));
		}
	}

	/**
	 * This is to ensure that the data and the columns are written in the proper
	 * order
	 * 
	 * @return
	 */
	@Override
	public List<String> getDataColumnNames() {
		List<String> columnNames = new LinkedList<>();
		for (Statistics statistic : statList) {
			for (String colName : statistic.getColumnNames()) {
				columnNames.add(colName);
			}
		}
		return columnNames;
	}

	@Override
	public List<String> getFullDataColumnNames() {
		List<String> columns = new LinkedList<>();
		columns.add(CombinedParameterSweep.CFG_ID_PARAM);
		columns.add(CombinedParameterSweep.RUN_PARAM);
		columns.add("step");
		columns.addAll(getDataColumnNames());
		return columns;
	}

	@Override
	public String getDataHeader() {
		StringBuilder sb = new StringBuilder();

		for (String column : getFullDataColumnNames()) {
			sb.append(column).append(';');
		}
		return sb.toString();
	}

	/**
	 * Produces the data using the same order of the
	 * {@link Statistics#getColumnNames()
     * }.
	 * 
	 * The order of the statistics objects is also taken into account.
	 * 
	 * @param model
	 *            the model from which the properties were measured
	 * @param props
	 *            a set of properties with statistics measured from the model
	 * 
	 * @return a string using a semi-column separation for the data values
	 */
	protected String produceCSVData(Model model, Properties props) {
		Configuration currentConfig = model.getConfiguration();

		// assumes we use CombinedParameterSweep to construct the experiment
		// parameter space
		// the runs are also embeded in the configuration
		long step = model.getStep();
		int cfgid = currentConfig.getInt(CombinedParameterSweep.CFG_ID_PARAM);
		int run = currentConfig.getInt(CombinedParameterSweep.RUN_PARAM);

		StringBuilder data = new StringBuilder();

		// add first three properties
		data.append(cfgid).append(';');
		data.append(run).append(';');
		data.append(step).append(';');

		// add the rest of the properties
		for (String key : this.getDataColumnNames()) {
			data.append(props.getProperty(key)).append(';');
		}
		return data.toString();
	}

	/**
	 * This method should be implemented by the data producer specific instances
	 * and establishes how the produced data is handled. Different data
	 * producers handle the data from statistics differently. For instance an
	 * InMemoryProducer, stores the data internally only to be consumed by a
	 * SynchronousDataConsumer later.
	 * 
	 * @param model
	 *            the model from which the properties were generated
	 * @param props
	 *            the properties generated
	 * 
	 *            Note a good approach could be to record the current run or
	 *            step and associate it with the current measures being
	 *            performed by the various statistics objects
	 * 
	 * 
	 * 
	 * @see Statistics
	 */
	protected abstract void forwardData(Model model, Properties props);

	/**
	 * Creates a {@link DataProducer} prototype object using the
	 * {@link AbstractConfigurablePrototype} abstract class, this class just
	 * provides a create method that copies the configuration objects from the
	 * current object
	 * 
	 * @return
	 */
	@Override
	public DataProducer create() {
		DataProducer producer = super.create();
		for (Statistics stats : this.statList) {
			producer.addStatistics(stats.create());
		}
		return producer;
	}

	@Override
	protected abstract DataProducer createPrototype();

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

}
