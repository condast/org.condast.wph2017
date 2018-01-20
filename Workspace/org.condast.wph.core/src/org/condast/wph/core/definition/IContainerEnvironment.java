package org.condast.wph.core.definition;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Statistics;

public interface IContainerEnvironment {

	void addListener(IEnvironmentListener<Object> listener);

	void removeListener(IEnvironmentListener<Object> listener);
	
	public void start();
	
	public void stop();

	public void step();

	public void pause();

	public boolean isRunning();
	
	public boolean isPaused();

	IJourney[] getJourneys();

	/**
	 * Get the symbiots
	 * @return
	 */
	public Collection<ISymbiot> getSymbiots();

	public Map<IStakeHolder<?,?>,IBehaviour> getModels();

	/**
	 * Get a specific model
	 * @param type
	 * @return
	 */
	public IStakeHolder<?, ?> getStakeHolder(ModelTypes type);

	/**
	 * Get the interval
	 * @return
	 */
	public int getInterval();
	
	/**
	 * Get the time that has passed in minutes
	 * @return
	 */
	public long getElapsedTime();

	Date getStartDate();

	/**
	 * Clear the date and interval
	 */
	public void clear();

	/**
	 * Convenience method displays the simulated time
	 * @return
	 */
	public String getSimulatedTime();

	Statistics getStatistics();
}