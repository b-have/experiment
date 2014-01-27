package org.bhave.experiment;

import org.bhave.experiment.data.producer.DataProducer;
import org.junit.Test;

/**
 *
 * @author davide
 */
public class TestDummyKafkaProducer {

    @Test
    public void testDummyKafkaProducer() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> modelClass = Class.forName("org.bhave.experiment.data.producer.KafkaDataProducer", true, loader).asSubclass(DataProducer.class);

        DataProducer producer = (DataProducer) modelClass.newInstance();
    }

}
