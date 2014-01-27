package org.bhave.experiment.data.producer;

import java.util.List;
import java.util.Set;
import org.bhave.experiment.Configurable;
import org.bhave.experiment.Model;
import org.bhave.experiment.Prototype;
import org.bhave.experiment.data.Statistics;

/**
 * A DataProducer serves as an observer for {@link Model} objects. Models
 * register data producers
 *
 * @author Davide Nunes
 *
 */
public interface DataProducer extends Configurable, Prototype<DataProducer> {

    /**
     * A human readable name for this producer
     *
     * @return data producer name
     */
    public String getName();

    /**
     * Add a statistics object to record certain properties of the model
     *
     * @param stats a {@link Statistics} object
     */
    public void addStatistics(Statistics stats);

    public List<Statistics> getStatistics();

    public void produce(Model model);

    public List<String> getDataColumnNames();

    public String getDataHeader();

    public List<String> getFullDataColumnNames();

    public void setID(int id);

    public int getID();

}
