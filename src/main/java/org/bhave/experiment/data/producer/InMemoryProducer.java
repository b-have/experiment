package org.bhave.experiment.data.producer;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.bhave.experiment.Model;

/**
 * The default implementation stores the data in CSV format. If one consumer
 * needs to access the information about the column names. After the
 * DataProducers are configured one can access such information using
 * {@link DataProducer#getDataHeader()} which returns the information using a
 * CSV format.
 *
 * Note: later different encoding mechanisms can be added to the data producers
 * I'm not worried about that for now.
 *
 * @author Davide Nunes
 */
public class InMemoryProducer extends AbstractDataProducer implements InMemoryDataProducer {

    private final List<String> data;

    public InMemoryProducer() {
        data = new LinkedList<>();
    }

    /**
     * This just stores the data in a list in memory
     *
     * @param model
     * @param props
     */
    @Override
    protected void forwardData(Model model, Properties props) {
        String csvData = produceCSVData(model, props);
        data.add(csvData);
    }

    @Override
    public String getName() {
        return "In Memory Producer";
    }

    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    protected DataProducer createPrototype() {
        return new InMemoryProducer();
    }

}
