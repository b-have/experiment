/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.I0Itec.zkclient.exception.ZkTimeoutException;
import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.data.DataExporter;
import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.experiment.data.producer.KafkaDataProducer;
import org.bhave.experiment.data.producer.RemoteDataProducer;

/**
 *
 * @author Davide Nunes
 */
public class KafkaDataConsumer extends AbstractDataConsumer implements RemoteDataConsumer {

    public static final String TOPIC_CFG = "broker.topic";
    public static final String HOST_CFG = "broker.host";
    public static final String PORT_CFG = "broker.port";

    private ConsumerConnector consumer;

    protected boolean configured;
    private String topic;
    private String host;
    private String port;

    public final static String DEFAULT_PORT_CFG = "2181";
    final static String groupID = "data-consumers";
    private boolean done;

    public KafkaDataConsumer() {
        super();
        configured = false;
        done = false;
    }

    protected void setup() {
        try {
            this.topic = config.getString(TOPIC_CFG);
            this.host = config.getString(HOST_CFG);
            this.port = config.getString(PORT_CFG);

            Properties props = new Properties();
            props.put("zookeeper.connect", host + ":" + this.port);
            props.put("group.id", groupID);
            props.put("zookeeper.sessiontimeout.ms", "400");
            props.put("zookeeper.synctime.ms", "200");
            props.put("autocommit.interval.ms", "1000");

            ConsumerConfig consumerCfg = new ConsumerConfig(props);
            consumer = Consumer.createJavaConsumerConnector(consumerCfg);
            configured = true;
        } catch (ZkTimeoutException ex) {
            throw new RuntimeException("Failed to connect to zookeper: " + ex.getMessage());
        }
    }

    String currentData = null;

    @Override
    public void consume() {

        if (!configured) {
            setup();
            configured = true;
        }

        if (producer != null && !(producer instanceof KafkaDataProducer)) {
            throw new RuntimeException("Invalid Data Producer: an object of the type: " + getTargetDataProducer().toString() + " should be used insted.");
        } else {
            KafkaDataProducer kafkaProducer = (KafkaDataProducer) producer;

            //consume from kafka queue
            if (exporters != null) {

                for (DataExporter exporter : exporters) {
                    if (producer != null) {
                        exporter.loadColumns(kafkaProducer.getFullDataColumnNames());
                    }

                    //fetch data from queue
                    exporter.exportRecord(currentData);

                }
            }
        }
    }

    @Override
    public Class<? extends DataProducer> getTargetDataProducer() {
        return KafkaDataProducer.class;
    }

    @Override
    public void loadConfiguration(Configuration config) {
        if (!config.containsKey(TOPIC_CFG)) {
            throw new RuntimeException("Invalid configuration for data producer. Please supply a valid topic for the data broker service: " + TOPIC_CFG + " = " + " topic-name");
        }
        if (!config.containsKey(HOST_CFG)) {
            throw new RuntimeException("Invalid configuration for this producer. Please supply a valid hostname or ip address for the data broker service. Example: " + HOST_CFG
                    + " = " + "localhost");
        }
        if (!config.containsKey(PORT_CFG)) {
            throw new RuntimeException("Invalid configuration for this producer. Please supply a valid port used by the data broker service. Example: " + PORT_CFG
                    + " = " + "2181");
        }

        super.loadConfiguration(config);
    }

    @Override
    public Map<String, Class<? extends Object>> getConfigurableParameters() {
        Map<String, Class<? extends Object>> parameters = super.getConfigurableParameters();

        parameters.put(TOPIC_CFG, String.class);
        parameters.put(HOST_CFG, String.class);
        parameters.put(PORT_CFG, String.class);

        return parameters;
    }

    @Override
    public Configuration getConfiguration() {
        return config;
    }

    @Override
    public void run() {
        if (!done) {
            Map<String, Integer> topicCountMap = new HashMap<>();
            topicCountMap.put(topic, new Integer(1));

            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
            List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
            KafkaStream<byte[], byte[]> stream = streams.get(0);

            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext() && !done) {
                String message = new String(it.next().message());
                currentData = message;
                consume();
            }

            finish();
        }
    }

    /**
     * The default behavior finishes all the exporters in a clean manner
     */
    @Override
    public void finish() {
        super.finish();

        done = true;
        //close the connection with kafka ? 
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    @Override
    protected DataConsumer createPrototype() {
        return new KafkaDataConsumer();
    }
}
