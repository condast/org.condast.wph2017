package org.condast.wph.builder.service;

import org.condast.wph.builder.eco.ContainerEnvironment;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IEnvironmentFactory;
import org.osgi.service.component.annotations.Component;

@Component
public class EnvironmentFactory implements IEnvironmentFactory{

	
	public EnvironmentFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public IContainerEnvironment createEnvironment() {
		return new ContainerEnvironment();
	}

}
