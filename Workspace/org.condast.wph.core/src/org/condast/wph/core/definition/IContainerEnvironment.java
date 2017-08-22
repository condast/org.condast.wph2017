package org.condast.wph.core.definition;

import java.util.Date;

import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.wph.core.definition.IModel.ModelTypes;

public interface IContainerEnvironment {

	void addListener(IEnvironmentListener listener);

	void removeListener(IEnvironmentListener listener);
	
	public void start();
	
	public void stop();

	public void step();

	public void pause();

	public boolean isRunning();
	
	public boolean isPaused();

	IJourney[] getJourneys();

	IModel<ModelTypes>[] getModels();

	ITransformation<?, ?> getTransformation(ModelTypes type);

	SymbiotCollection getSymbiots();

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

}