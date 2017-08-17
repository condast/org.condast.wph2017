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

	public void pause();

	public boolean isRunning();
	
	public boolean isPaused();

	IJourney[] getJourneys();

	IModel<ModelTypes>[] getModels();

	ITransformation<?, ?> getTransformation(ModelTypes type);

	SymbiotCollection getSymbiots();

	long getElapsedTime();

	Date getStartDate();

}