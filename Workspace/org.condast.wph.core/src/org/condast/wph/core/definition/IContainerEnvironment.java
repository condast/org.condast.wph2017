package org.condast.wph.core.definition;

import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.wph.core.definition.IModel.ModelTypes;

public interface IContainerEnvironment {

	void addListener(IEnvironmentListener listener);

	void removeListener(IEnvironmentListener listener);
	
	public void start();
	
	public void stop();

	IJourney[] getJourneys();

	IModel<ModelTypes>[] getModels();

}