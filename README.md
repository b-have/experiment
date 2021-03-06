Experiment Building Library
===========================

A Java framework to build and execute experiments and collect data.

Uses the parameter sweeping library [param-sweeper](https://github.com/davidenunes/param-sweeper/)
Currently, it provides a basic multi-thread experiment runner and a simple console to load experiments

Example of an experiment configuration file using kafka as a service to produce and collect data:

```
# Experiment Configuration File
euid = 123
#load the model of experiments here
model = org.bhave.experiment.dummy.DummyModel
#your default experiment runner, we will add grid capabilities later
runner = org.bhave.experiment.run.MultiThreadedRunner

#kafka 
data.producers.0 = org.bhave.experiment.data.producer.KafkaDataProducer
data.producers.0.broker.port = 9092
data.producers.0.broker.host = localhost
data.producers.0.broker.topic = test

#statistics to be used in the data production
data.producers.0.stats.0 = org.bhave.experiment.dummy.DummyStats
data.producers.0.stats.1 = org.bhave.experiment.dummy.DummyStats

#consumes data stored in memory in the data producers
data.consumers.0 = org.bhave.experiment.data.consumer.KafkaDataConsumer

#dummy exporter that prints the data received in kafka to the standard output
data.consumers.0.export.0 = org.bhave.experiment.dummy.StdOutDataExporter

#map consumer to producer
data.consumers.0.producer = 0

#if you use the FileDataExporter class you need to add these configurations
#data.consumers.0.export.0.file.name = test.csv
#data.consumers.0.export.0.file.append = true


# experiment parameter space
runs = 1

params.0.name = p1
params.0.sweep = sequence
params.0.type = double
params.0.value.from = 0
params.0.value.to = 100
params.0.value.step = 0.5

params.1.name = p2
params.1.sweep = sequence
params.1.type = int
params.1.value.from = 0
params.1.value.to = 10
params.1.value.step = 1

```

## Running an Experiment ## 
To run experiments and take advantage of multiple existing processing nodes (multiple cores for instance) I include a MultiThreadedRunner class (see example above). This runner basically takes the experiment and submits the multiple runs making sure all the cores are allways busy. I have developed another runner that uses a grid infrastructure (using the [gridgain](http://www.gridgain.com/) platform) to deploy the simulation runs but this is not yet included.

To run an experiment we need something to assemble all the experiment components into one place. For this we have the _ExperimentConsole_ implementations. There are two options, a simple GUI based console which allow us to load configuration files and start experiments and a text-based console which basically loads a given configuration file and displays the progress using a text-based progress bar. 



