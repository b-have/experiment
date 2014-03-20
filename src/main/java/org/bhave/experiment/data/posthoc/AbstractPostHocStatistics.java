package org.bhave.experiment.data.posthoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.AbstractConfigurablePrototype;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.DataExporter;
import org.bhave.sweeper.CombinedParameterSweep;

/**
 * A simple implementation that provides facilities to deal with Configurable
 * Prototypes
 * 
 * TODO refactor this framework to add export formats this is, data consumers
 * should be able to receive data and deliver data to exporters just like
 * posthoc statistics, data is generated in a table format usually but should
 * allow for different formats without reimplmementing file data exporters, etc.
 * This could and should probably be decoupled from the rest and added as
 * another configurable property of certain exporters.
 * 
 * @author Davide Nunes
 * 
 */
public abstract class AbstractPostHocStatistics extends
		AbstractConfigurablePrototype<PostHocStatistics> implements
		PostHocStatistics {
	List<DataExporter> exporters;

	@Override
	public void addExporter(DataExporter exporter) {
		if (exporters == null) {
			exporters = new LinkedList<>();
		}
		exporters.add(exporter);
	}

	public List<DataExporter> getExporters() {
		if (exporters == null)
			return new ArrayList<>();
		return exporters;
	}

	@Override
	public void report(Model model) {
		// all good, export the data
		if (exporters != null) {
			for (DataExporter exporter : exporters) {
				// add column names to exporters
				List<String> columns = this.getFullDataColumns();
				exporter.loadColumns(columns);
				for (Properties dataRecord : this.getFullData(model)) {
					List<String> data = new ArrayList<>(columns.size());
					for (String key : columns) {
						data.add(dataRecord.getProperty(key));
					}
					exporter.exportRecord(data);
				}
			}
		}
		// calls finish on all the exporters
		finish();
	}

	/**
	 * if the recorder
	 */
	private void finish() {
		if (exporters != null) {
			for (DataExporter exporter : exporters) {
				exporter.finish();
			}
		}
	}

	private static final String CFG_STEP = "step";

	public List<Properties> getFullData(Model model) {
		List<Properties> data = this.measure(model);

		// add aditional usefull model information such as step cfg_id and run
		// to all the columns in the table this is usefull if you have multiple
		// runs and want to track the statistics measured for each independent
		// run
		Configuration cfg = model.getConfiguration();
		for (Properties props : data) {
			long step = model.getStep();
			int cfgid = cfg.getInt(CombinedParameterSweep.CFG_ID_PARAM);
			int run = cfg.getInt(CombinedParameterSweep.RUN_PARAM);

			props.setProperty(CFG_STEP, Long.toString(step));
			props.setProperty(CombinedParameterSweep.CFG_ID_PARAM,
					Integer.toString(cfgid));
			props.setProperty(CombinedParameterSweep.RUN_PARAM,
					Integer.toString(run));
		}
		return data;
	}

	public List<String> getFullDataColumns() {

		LinkedList<String> columns = new LinkedList<>(this.getDataColumns());

		columns.addFirst(CombinedParameterSweep.CFG_ID_PARAM);
		columns.addFirst(CombinedParameterSweep.RUN_PARAM);
		columns.addFirst(CFG_STEP);

		return columns;
	}
}
