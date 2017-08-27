package org.condast.wph.rest.service;

import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IModel.ModelTypes;

public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();

	private IContainerEnvironment cenv;
	
	private Dispatcher() {}

	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public void startApplication( IContainerEnvironment cenv ){
		this.cenv = cenv;
	}
		
	public IContainerEnvironment getEnvironment() {
		return cenv;
	}

	public IStakeHolder<?,?> getStakeholder( ModelTypes type ) {
		return cenv.getStakeHolder(type);
	}

	public void dispose(){
	}
}
