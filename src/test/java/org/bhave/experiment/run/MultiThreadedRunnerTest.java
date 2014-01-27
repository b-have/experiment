/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.run;

import java.io.File;
import org.bhave.experiment.Experiment;
import org.bhave.experiment.ExperimentRunner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davide
 */
public class MultiThreadedRunnerTest {

    public MultiThreadedRunnerTest() {
    }

    @Test
    public void testSomeMethod() {
        String filename = "experiment.config";
        File file = new File(Thread.currentThread().getContextClassLoader()
                .getResource(filename).getPath().toString());
        Experiment experiment = Experiment.fromFile(file);

        assertEquals(MultiThreadedRunner.class, experiment.getRunner().getClass());

        ExperimentRunner runner = experiment.getRunner();
        runner.load(experiment);
        runner.start();
    }

}
