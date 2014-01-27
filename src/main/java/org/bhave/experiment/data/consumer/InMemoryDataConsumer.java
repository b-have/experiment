package org.bhave.experiment.data.consumer;

import org.bhave.experiment.data.DataExporter;
import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.experiment.data.producer.InMemoryDataProducer;

/**
 * Stores the data in memory using the default encoding
 *
 * @author Davide Nunes
 */
public class InMemoryDataConsumer extends AbstractDataConsumer {

    /**
     * An in memory data consumer is a synchronous data consumer that should be
     * called after the model is executed and the results are stored inside the
     * data producer, typically using an {@link InMemoryDataProducer}. To access
     * this data you can use the method: {@link InMemoryDataProducer#getData()}.
     *
     */
    @Override
    public void consume() {
        InMemoryDataProducer mProducer = (InMemoryDataProducer) producer;
        //all good, export the data
        if (exporters != null) {
            for (DataExporter exporter : exporters) {
                //add column names to exporters
                exporter.loadColumns(mProducer.getFullDataColumnNames());
                for (String dataRecord : mProducer.getData()) {
                    exporter.exportRecord(dataRecord);
                }
            }
        }
    }

    @Override
    public Class<? extends DataProducer> getTargetDataProducer() {
        return InMemoryDataProducer.class;
    }

    @Override
    protected DataConsumer createPrototype() {
        return new InMemoryDataConsumer();
    }

}
