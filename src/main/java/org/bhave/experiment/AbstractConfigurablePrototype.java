/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bhave.experiment.Configurable;
import org.bhave.experiment.Prototype;

/**
 *
 * @author Davide Nunes
 * @param <C> configurable Object
 */
public abstract class AbstractConfigurablePrototype<C extends Configurable> implements Configurable, Prototype<C> {

    protected Configuration config;

    @Override
    public C create() {
        C proto = createPrototype();

        //if this is overriden properly, it creates a deep copy of the configuration object
        proto.loadConfiguration(this.getConfiguration());
        return proto;
    }

    /**
     * Creates a first level prototype for the object (no deep copy)
     *
     * @return
     */
    protected abstract C createPrototype();

    /**
     * Creates a deep copy for the supplied configuration object
     *
     * @param config a configuration object
     */
    @Override
    public void loadConfiguration(Configuration config) {
        Configuration newConfig = new PropertiesConfiguration();

        Iterator<String> keyIt = config.getKeys();

        while (keyIt.hasNext()) {
            String key = keyIt.next();
            newConfig.setProperty(key, config.getProperty(key));
        }
        this.config = newConfig;
    }

    /**
     * The default behaviour is to return an empty configuration object, if this
     * object hasn't beed configured yet.
     *
     * @return an empty configuration if the object was not configured or the
     * current configuration otherwise.
     */
    @Override
    public Configuration getConfiguration() {
        if (config == null) {
            config = new PropertiesConfiguration();
        }
        return config;
    }

    /**
     * The default behaviour is to return an empty configurable parameter map
     *
     * @return a parameter map
     */
    @Override
    public Map<String, Class<? extends Object>> getConfigurableParameters() {
        Map<String, Class<? extends Object>> properties = new HashMap<>();
        return properties;
    }
}
