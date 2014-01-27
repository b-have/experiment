package org.bhave.experiment.data.producer;

import org.bhave.experiment.data.consumer.RemoteDataConsumer;

/**
 * Tagging interface for that producers that send data using some service. This
 * data is later to be retrieved by the {@link RemoteDataConsumer} objects.
 *
 * @author Davide Nunes
 */
public interface RemoteDataProducer extends DataProducer {

}
