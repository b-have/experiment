package org.bhave.experiment.run;

import java.io.File;
import java.util.HashSet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bhave.experiment.Experiment;
import org.bhave.experiment.ExperimentRunner;
import org.joda.time.Period;

import scala.reflect.internal.Trees.This;

/**
 * <h2>Text-based Experiment Console</h2>
 * <p>
 * While the {@link GUIConsole} is a simple alternative to load experiments and
 * track experiment progress, this is a more straightforward tool that enables
 * us to load an experiment directly from the command line, execute it or stop
 * it.
 * </p>
 * 
 * <p>
 * There are two options, the first being that the user provides the
 * configuration file using the program parameters. The second option is to
 * launch this utility and type in "load experiment.cfg" for instance.
 * 
 * If you launch the application with the -r option it will automatically load
 * and run the experiment.
 * </p>
 * 
 * TODO: this could have more functionalities but for now I'm sticking with the
 * basics
 * 
 * @author Davide Nunes
 * 
 */
public class TerminalConsole implements ExperimentConsole {

	// used in conjuction with -f to load and run an experiment
	public static final String ARG_R = "r";
	// used to load an experiment
	public static final String ARG_F = "f";

	private Experiment experiment;
	private ExperimentRunner runner;

	private static TerminalConsole console;

	/**
	 * Receives the parameters, loads the experiments and runs them we will keep
	 * the command line interface library here if we need to implement more
	 * parameters in the future
	 * 
	 * to run and experiment for now you just need to evoque
	 * 
	 * "java -jar experiment -f some-file" if experiment
	 * 
	 * TODO: adapt this to run either in command line format or to launch gui
	 * with the -gui option, in case GUI option is enabled no parameters are
	 * necessary, the user will have to load the experiment from the GUI menu
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) {

		console = new TerminalConsole();

		Options options = new Options();
		// add file option
		options.addOption(ARG_F, true, "the experiment file to be loaded");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out
					.println("Error starting the experiment console the available options are: ");
			System.out.println(options.toString());
		}

		if (cmd != null) {
			if (cmd.hasOption(ARG_F)) {
				String filename = cmd.getOptionValue(ARG_F);
				if (filename == null) {
					System.err
							.println("a filename was expected after the option "
									+ ARG_F);
				} else {
					File file = new File(filename);
					if (!file.exists()) {
						System.err
								.println("The configuration file you specified was not found");
					} else {
						console.loadExperiment(file);
						// this means load and start running

						console.start();
					}
				}
			} else {
				System.err
						.println("Please provide a file to be run with the experiment using the option -f");
			}
		}

	}

	private int progress = 0;

	/**
	 * Createns an {@link Experiment} instance and displays some information in
	 * the standard output
	 * 
	 * @param file
	 * @return
	 */
	private void loadExperiment(File file) {
		Experiment experiment = Experiment.fromFile(file);

		System.out.println("Experiment Loaded: " + experiment.getUID());
		System.out.println("Runner: "
				+ experiment.getRunner().getClass().getName());
		System.out.println("Model: "
				+ experiment.getModel().getClass().getName());

		System.out.println("Parameter Space: "
				+ experiment.getParameterSpace().size() + " runs");

		this.experiment = experiment;
	}

	/**
	 * This launches a thread that executes the runner and waits for the results
	 * depending on the runner, the runner can be stopped or not
	 */
	private void start() {

		if (this.experiment != null) {
			this.runner = experiment.getRunner();
			// this.isRunning = true;
			// this.runner = experiment.getRunner();
			// final ExperimentRunner runner = this.runner;
			// final TerminalConsole console = this;
			// final Experiment experiment = this.experiment;
			//
			// Thread runnerThread = new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// attach this console
			runner.attach(this);

			// load the current experiment into the runner
			runner.load(experiment);

			runner.start();

			// when the runner finishes
			System.out.println("Experiment Done");
			//
			// }
			// });

			// runnerThread.start();
		}

	}

	// number of columns to display progress
	private static final int NUM_COLS = 50;

	@Override
	public void updateProgress(Period remaining, int value, int currentRun,
			int totalRuns) {

		progress = NUM_COLS * currentRun / totalRuns;
		String pBar = progressBar(progress, NUM_COLS);

		String strRemain = null;
		if (currentRun == totalRuns) {
			strRemain = "(          done          )";
		} else {
			strRemain = "(time remaining: "
					+ String.format("%02d:%02d:%02d", remaining.getHours(),
							remaining.getMinutes(), remaining.getSeconds())
					+ ")";
		}

		String strRuns = "(runs: " + currentRun + " / " + totalRuns + ")";

		System.out.print('\r' + pBar + " " + strRemain + " " + strRuns);
		if (currentRun == totalRuns)
			System.out.print('\n');

	}

	/**
	 * If progress == maxProgress print done and clean the progress bar
	 * 
	 * @param progress
	 * @param maxProgress
	 * @return
	 */
	private String progressBar(int progress, int maxProgress) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i <= maxProgress; i++) {

			if (i <= progress)
				sb.append('=');
			else
				sb.append(' ');
		}
		sb.append(']');

		return sb.toString();
	}

}
