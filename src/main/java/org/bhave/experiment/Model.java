package org.bhave.experiment;

import java.util.Collection;
import org.bhave.experiment.data.producer.DataProducer;

public interface Model extends Configurable, Prototype<Model>, Runnable {

    /**
     * Registers data producer with a given id to tag this producer for later
     * access. This is used to create a mapping between the data producers and
     * the data consumers.
     *
     * @param producer the producer to be added
     * @param id an id of this producer
     */
    public void registerDataProducer(DataProducer producer);

    public DataProducer getDataProducer(int id);

    public Collection<? extends DataProducer> getProducers();

    /**
     * Returns the starting time for this model. This is, when run was called on
     * the model.
     *
     * @return time
     */
    public long getStartTime();

    public int getRun();

    public long getStep();

}
