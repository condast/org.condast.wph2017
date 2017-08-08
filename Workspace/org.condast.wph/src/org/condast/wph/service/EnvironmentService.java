package org.condast.wph.service;

import org.condast.wph.core.definition.IEnvironmentFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class EnvironmentService {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	@Reference
	public void bindEnvironment( IEnvironmentFactory factory){
		this.dispatcher.startApplication( factory.createEnvironment());
	}

	public void unbindEnvironment( IEnvironmentFactory ce ){
	}

}
