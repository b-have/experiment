package org.bhave.experiment;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;

public class ExperimentTest extends TestCase {

	@Test
	public void testLoadExperiment() {
		String filename = "experiment.config";
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource(filename).getPath().toString());

		assertTrue(file.exists());

		Configuration configuration = null;
		try {
			configuration = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			fail("Configuration file failled to load");
		}

		assertTrue(configuration.containsKey(Experiment.P_EUID));

		Experiment experiment = Experiment.fromFile(file);
		// we don't need the parameters in a file for the test
		experiment.getRunner().setPrintParamSpace(false);

		assertNotNull(experiment.getModel());

		assertNotNull(experiment.getRunner());

	}

	@Test
	public void testInMemoryRun() {
		String filename = "experiment.config";
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource(filename).getPath().toString());

		assertTrue(file.exists());

		Configuration configuration = null;
		try {
			configuration = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			fail("Configuration file failled to load");
		}

		Experiment experiment = Experiment.fromFile(file);

		ExperimentRunner runner = experiment.getRunner();
		experiment.getRunner().setPrintParamSpace(false);

		assertNotNull(runner);

		runner.load(experiment);

		try {
			runner.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
