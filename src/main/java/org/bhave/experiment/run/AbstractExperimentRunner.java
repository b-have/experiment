package org.bhave.experiment.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.Experiment;
import org.bhave.experiment.ExperimentRunner;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.consumer.DataConsumer;
import org.bhave.experiment.data.consumer.RemoteDataConsumer;
import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.sweeper.CombinedParameterSweep;
import org.bhave.sweeper.ParameterSweep;
import org.joda.time.Period;

/**
 * 
 * @author Davide Nunes
 */
public abstract class AbstractExperimentRunner implements ExperimentRunner {

	protected Experiment experiment;
	protected Model modelProto;

	protected Period estimatedTime;
	protected int completedRuns;
	protected int totalRuns;

	protected ExperimentConsole console;

	protected static int currentCFGID = 0;
	protected static final String CFG_ID = "cfgid";

	protected List<RemoteDataConsumer> remoteConsumers;
	protected List<DataConsumer> inMemoryConsumers;

	@Override
	public void start() {
		setupDataConsumers(experiment);
	}

	/**
	 * Prepare a model for submission
	 * 
	 * @param modelProto
	 *            a model prototype class from which we create a new instance
	 * @param config
	 *            the configuration for this model
	 * @param producers
	 *            the data producers to be attached to the model
	 * @return the model ready to submit
	 */
	protected Model setupModel(Model modelProto, Configuration config,
			Experiment experiment) {
		Model model = modelProto.create();

		model.loadConfiguration(config);

		// load data producers
		for (DataProducer dataP : experiment.getProducers()) {
			model.registerDataProducer(dataP.create());
		}

		return model;
	}

	protected void setupDataConsumers(Experiment experiment) {
		Collection<? extends DataConsumer> consumers = experiment
				.getConsumers();
		for (DataConsumer consumer : consumers) {

			DataProducer producer = experiment.getProducer(consumer
					.getProducerID());
			consumer.loadDataProducer(producer);

			if (consumer instanceof RemoteDataConsumer) {
				startRemoteConsumer((RemoteDataConsumer) consumer);
			} else {
				startInMemoryConsumer(consumer);
			}
		}
	}

	private void startRemoteConsumer(RemoteDataConsumer consumer) {
		Thread consumerThread = new Thread(consumer);

		if (remoteConsumers == null) {
			remoteConsumers = new LinkedList<>();
		}

		this.remoteConsumers.add(consumer);
		consumerThread.start();
	}

	private void startInMemoryConsumer(DataConsumer consumer) {
		if (inMemoryConsumers == null) {
			inMemoryConsumers = new LinkedList<>();
		}
		this.inMemoryConsumers.add(consumer);
	}

	/**
	 * Get the progress in terms of percentage of runs complete
	 * 
	 * @return
	 */
	public int getProgress() {
		return completedRuns * 100 / totalRuns;
	}

	public synchronized Period estimatedEndTime() {
		return estimatedTime;
	}

	@Override
	public void load(Experiment experiment) {
		this.experiment = experiment;
		this.completedRuns = 0;
		this.totalRuns = experiment.getParameterSpace().size();
		estimatedTime = new Period(0);
	}

	/**
	 * Console acts as an observer the experiment runner acts as the observed
	 * and calls {@link ExperimentConsole#updateProgress(Period, int, int, int)}
	 * on the console.
	 * 
	 * @param console
	 */
	@Override
	public void attach(ExperimentConsole console) {
		this.console = console;
	}

	@Override
	public int getCurrentRuns() {
		return completedRuns;
	}

	@Override
	public int getTotalRuns() {
		return totalRuns;
	}

	/**
	 * Shutdown Data consumers by calling finish. It should also terminate all
	 * threads associated with the Remote data collection.
	 */
	protected void shutdown() {
		if (inMemoryConsumers != null) {
			for (DataConsumer consumer : inMemoryConsumers) {
				consumer.finish();
			}
			inMemoryConsumers = null;
		}
		if (remoteConsumers != null) {
			for (RemoteDataConsumer consumer : remoteConsumers) {
				consumer.finish();
			}
			remoteConsumers = null;
		}
	}

	protected boolean writeParams = true;

	public void setPrintParamSpace(boolean write) {
		this.writeParams = write;
	}

	protected void writeParamSpace(CombinedParameterSweep params) {
		if (writeParams) {
			// experiment file name
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Calendar cal = Calendar.getInstance();

			StringBuilder sb = new StringBuilder("param-space");

			sb.append('_').append(dateFormat.format(cal.getTime()));
			sb.append("experiment:").append(experiment.getUID());
			sb.append('_');
			sb.append(".csv");

			try {
				params.writeSweepFile(sb.toString());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public boolean isPrintingParamSpace() {
		return this.writeParams;
	}

}
