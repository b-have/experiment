package org.bhave.experiment;

import static org.junit.Assert.*;

import java.io.File;
import junit.framework.TestCase;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.bhave.experiment.dummy.DummyModel;
import org.bhave.experiment.run.MultiThreadedRunner;

public class TestPostHocExperiment extends TestCase {

    @Test
    public void testLoadExperiment() {
        String filename = "experiment_posthoc.config";
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

        assertNotNull(experiment.getModel());

        assertNotNull(experiment.getRunner());

    }

    @Test
    public void testPostHocOutput() {
    	String filename = "experiment_posthoc.config";
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
        assertNotNull(runner);

        runner.load(experiment);

        try {
            runner.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
