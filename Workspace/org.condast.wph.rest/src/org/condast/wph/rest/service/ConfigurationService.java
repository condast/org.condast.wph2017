package org.condast.wph.rest.service;

import org.condast.commons.project.IConfigProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component()
public class ConfigurationService {

	@Reference(
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.STATIC,
			policyOption = ReferencePolicyOption.GREEDY
			)
	private IConfigProvider provider;

	private static Dispatcher dispatcher = Dispatcher.getInstance();

	public ConfigurationService() {
		super();
	}
	
	@Activate
	public void activate() {
		dispatcher.setConfig(provider.getConfiguration());	
		provider.getConfiguration().setLogLevel();

	}	
}
