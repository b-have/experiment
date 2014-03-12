package org.bhave.experiment.data.posthoc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.bhave.experiment.AbstractConfigurablePrototype;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.DataExporter;

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

	@Override
	public void report(Model model) {
		// all good, export the data
		if (exporters != null) {
			for (DataExporter exporter : exporters) {
				// add column names to exporters
				exporter.loadColumns(getDataColumns());
				for (Properties dataRecord : this.measure(model)) {
					List<String> columns = this.getDataColumns();
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
}
