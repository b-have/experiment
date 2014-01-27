package org.bhave.experiment.dummy;

import java.util.Map;

import org.bhave.experiment.AbstractModel;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.producer.DataProducer;

/**
 * A dummy model that does nothing but call {@link Thread#sleep(long) } This can
 * be useful to test out new Experiment Runners.
 *
 * @author Davide Nunes
 */
public class DummyModel extends AbstractModel {

    public DummyModel() {
        super();
    }

    @Override
    public Map<String, Class<? extends Object>> getConfigurableParameters() {
        // NOTHING TO DO HERE 
        return null;
    }

    /**
     * Running this model takes 10 milliseconds
     */
    @Override
    public void run() {
        startTime = System.currentTimeMillis();

        try {
            for (DataProducer p : getProducers()) {
                p.produce(this);
            }
            Thread.sleep(1);

        } catch (InterruptedException ex) {

        }

    }

    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public Model create() {
        return new DummyModel();
    }

    /**
     * Return a dummy value to be observed by the statistics class
     *
     * @return
     */
    public int dummyStatValue() {
        return 123;
    }

}
