package org.bhave.experiment.data.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.Model;

/**
 * <p>
 * An abstract data producer that can be used to write data to a Kafka server.
 * this could be consumed by some clients and stored either in files or a
 * databases.
 * </p>
 * For more information about kafka refer to <a
 * href="https://kafka.apache.org/"> kafka landing page </a>.
 *
 * <p>
 * In order to properly use the data producer you need to first call the load
 * configuration to load the configuration of this producer.
 * </p>
 *
 * @author Davide Nunes
 */
public class KafkaDataProducer extends AbstractDataProducer implements RemoteDataProducer {

    protected Producer<Integer, String> producer;

    private final Properties props = new Properties();

    public KafkaDataProducer() {
        configured = false;
    }

    @Override
    protected void forwardData(Model model, Properties props) {
        //setup the data producer before using it
        if (!configured) {
            setup();
        }

        String data = produceCSVData(model, props);
        KeyedMessage<Integer, String> keyedMessage = new KeyedMessage<>(topic, data);
        producer.send(keyedMessage);
    }

    protected static final String TOPIC_CFG = "broker.topic";
    protected static final String HOST_CFG = "broker.host";
    protected static final String PORT_CFG = "broker.port";

    protected String topic;
    protected String host;
    protected String port;
    protected boolean configured;

    protected void setup() {

        this.topic = config.getString(TOPIC_CFG);
        this.host = config.getString(HOST_CFG);
        this.port = config.getString(PORT_CFG);

        StringBuilder brokerAddress = new StringBuilder(host);
        brokerAddress.append(':').append(port);

        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");
        props.put("zk.connect", host + ":" + 2181);
        props.put("metadata.broker.list", brokerAddress.toString());
        // Use random partitioner. Don't need the key type. Just set it to Integer.
        // The message is of type String.
        producer = new kafka.javaapi.producer.Producer<>(new ProducerConfig(props));

        configured = true;
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

        //if everything is correct, this loads the target configuration
        super.loadConfiguration(config);
    }

    @Override
    public Map<String, Class<? extends Object>> getConfigurableParameters() {
        HashMap<String, Class<? extends Object>> parameters = new HashMap<>();

        parameters.put(TOPIC_CFG, String.class);
        parameters.put(HOST_CFG, String.class);
        parameters.put(PORT_CFG, String.class);

        return parameters;
    }

    @Override
    public String getName() {
        return "Kafka Data Producer";
    }

    /**
     * This is forwarded to {@link AbstractDataProducer#create() } where the
     * statistics objects are also prototyped using the create method.
     *
     * @return
     */
    @Override
    protected DataProducer createPrototype() {
        return new KafkaDataProducer();
    }

}
