package org.bhave.experiment.run;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.configuration.Configuration;
import org.bhave.experiment.Experiment;
import org.bhave.experiment.ExperimentRunner;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.consumer.DataConsumer;
import org.bhave.experiment.data.consumer.RemoteDataConsumer;
import org.bhave.experiment.data.producer.DataProducer;
import org.bhave.sweeper.CombinedParameterSweep;
import org.joda.time.Period;

/**
 * This is an experiment runner that uses a the local computer threads as a mean
 * to parallelize the model execution.
 *
 * @author Davide Nunes
 */
public class MultiThreadedRunner extends AbstractExperimentRunner {

    @Override
    public void start() {

        super.start();

        int cores = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(cores);
        CompletionService<Model> pool = new ExecutorCompletionService<>(
                executor);

        CombinedParameterSweep params = experiment.getParameterSpace();
        Iterator<Configuration> configs = params.iterator();

        /**
         * *********************************************** + submit "core"
         * number of tasks
         */
        int numSubmitted = 0;
        modelProto = experiment.getModel();

        while (configs.hasNext() && numSubmitted < cores) {

            Configuration config = configs.next();

            //Setup the model with data producers
            Model model = setupModel(modelProto, config, experiment);

            pool.submit(model, model);
            numSubmitted++;

            if (console == null) {
                int run = config.containsKey("run") ? config.getInt("run") : 1;
                System.out.println("Submited Run: " + run);
            }
        }

        /**
         * *********************************************** + keep submitting
         * tasks
         */
        this.completedRuns = 0;
        long sumDurations = 0;
        while (completedRuns < params.size()) {
            try {
                Future<Model> futureModel = pool.take();

                completedRuns++;

                Model model = futureModel.get();

                long modelStartTime = model.getStartTime();

                long currentTime = System.currentTimeMillis();

                long duration = currentTime - modelStartTime;

                sumDurations = (sumDurations + duration);
                long estimatedMillisRemaining = (sumDurations / completedRuns)
                        * (totalRuns - completedRuns);

                estimatedTime = new Period(estimatedMillisRemaining);
                if (console != null) {
                    console.updateProgress(estimatedTime, getProgress(),
                            completedRuns, totalRuns);
                } else {
                    System.out
                            .println("progress: "
                                    + completedRuns
                                    + "/"
                                    + totalRuns
                                    + " estimated time: "
                                    + String.format(
                                            "%02d days %02d hours%02d minutes %02d seconds",
                                            estimatedTime.getDays(),
                                            estimatedTime.getHours(),
                                            estimatedTime.getMinutes(),
                                            estimatedTime.getSeconds()));
                }

                //process inMemory Data Consumers
                for (DataConsumer consumer : inMemoryConsumers) {
                    DataProducer producer = model.getDataProducer(consumer.getProducerID());
                    //this is needed to retrieve the data from the producer
                    consumer.loadDataProducer(producer);
                    consumer.consume();
                }

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            // submit another task
            if (configs.hasNext()) {
                Configuration config = configs.next();
                //setup the model with data producers
                Model model = setupModel(modelProto, config, experiment);

                pool.submit(model, model);
                numSubmitted++;

                if (console == null) {
                    int run = config.containsKey("run") ? config.getInt("run") : 1;
                    System.out.println("Submited Run: " + run);
                }
            }
        }
        System.out.println("All done, shuttdown executor");
        executor.shutdown();
        //shutdown experiment runner
        shutdown();

    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("The Multi-threaded runner cannot be stopped");

    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("The Multi-threaded runner cannot be stopped");
    }

}
