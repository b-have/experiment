/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.run;

import org.joda.time.Period;

/**
 * 
 * @author davide
 */
public interface ExperimentConsole {

	public void updateProgress(final Period remaining, final int value,
			final int currentRun, final int totalRuns);

}
