package org.bhave.experiment.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bhave.experiment.Model;
import org.bhave.experiment.data.posthoc.AbstractPostHocStatistics;
import org.bhave.experiment.data.posthoc.PostHocStatistics;

public class DummyPostHocStats extends AbstractPostHocStatistics {

	@Override
	public String getName() {
		return "Dummy Post Hoc";
	}

	@Override
	public Class<? extends Model> getTargetModelClass() {
		return DummyModel.class;
	}

	@Override
	public List<Properties> measure(Model model) {
		List<Properties> props = new ArrayList<>();
		Properties p = new Properties();
		p.setProperty("dummy-stat", "0");
		props.add(p);

		return props;
	}

	@Override
	public List<String> getDataColumns() {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("dummy-stat");
		return columns;
	}

	@Override
	protected PostHocStatistics createPrototype() {
		return new DummyPostHocStats();
	}

}
