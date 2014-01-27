/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.sweeper.CombinedParameterSweep;

/**
 *
 * @author Davide Nunes
 */
public abstract class AbstractModel implements Model {

    protected long startTime;

    protected Map<Integer, DataProducer> producers;
    Configuration config;

    public AbstractModel() {
        producers = new HashMap<>();
    }

    /**
     * The default behaviour is to store the configuration in a field for access
     * if needed, if you need some extra verification or other behaviour,
     * override this method;
     *
     * @param config a configuration object
     */
    @Override
    public void loadConfiguration(Configuration config) {
        this.config = config;
    }

    @Override
    public void registerDataProducer(DataProducer producer) {
        producers.put(producer.getID(), producer);
    }

    @Override
    public DataProducer getDataProducer(int id) {
        return producers.get(id);
    }

    /**
     * Returns the instant this model started its execution
     *
     * @return start time
     */
    @Override
    public long getStartTime() {
        return startTime;
    }

    /**
     * This is specific from Model instances that use the parameter sweeper
     * library as the run is associated with the configuration parameter space.
     *
     * @return number of the run for the current configuration
     */
    @Override
    public int getRun() {
        if (config != null) {
            if (config.containsKey(CombinedParameterSweep.RUN_PARAM)) {
                return config.getInt(CombinedParameterSweep.RUN_PARAM);
            }
        }
        return 1;
    }

    /**
     * Returns the current configuration object for this model
     *
     * @return
     */
    @Override
    public Configuration getConfiguration() {
        return this.config;
    }

    @Override
    public Collection<? extends DataProducer> getProducers() {
        return producers.values();
    }
}
