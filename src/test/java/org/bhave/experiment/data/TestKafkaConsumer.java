/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bhave.experiment.data.consumer.KafkaDataConsumer;
import org.bhave.experiment.dummy.StdOutDataExporter;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Davide Nunes
 */
public class TestKafkaConsumer {

    public TestKafkaConsumer() {
    }

    @Test
    public void testConfigureKafkaConsumer() throws InterruptedException {

        KafkaDataConsumer consumer = new KafkaDataConsumer();

        Configuration config = new PropertiesConfiguration();
        config.addProperty(KafkaDataConsumer.HOST_CFG, "localhost");
        config.addProperty(KafkaDataConsumer.PORT_CFG, KafkaDataConsumer.DEFAULT_PORT_CFG);
        config.addProperty(KafkaDataConsumer.TOPIC_CFG, "test");
        config.addProperty(KafkaDataConsumer.P_PRODUCER_ID, 0);

        consumer.loadConfiguration(config);

        Configuration configuration = consumer.getConfiguration();
        assertNotNull(configuration);

        consumer.addExporter(new StdOutDataExporter());

        Thread consumerThread = new Thread(consumer);
        consumerThread.setName("Consumer Thread");
        consumerThread.start();

        assertTrue(consumerThread.isAlive());
        //this should terminate the thread in which it is running
        consumer.finish();

        Thread.sleep(1000);
        assertFalse(consumerThread.isAlive());

        System.out.println("Kafka Consumer Thread terminated");

    }

}
