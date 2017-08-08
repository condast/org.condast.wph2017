package org.condast.wph.service;

import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.ui.swt.WPHFrontend;

public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();

	private IContainerEnvironment cenv;
	private WPHFrontend frontend;
	
	private Dispatcher() {}

	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public void startApplication( WPHFrontend frontend ){
		this.frontend = frontend;
		if( this.cenv != null )
			this.frontend.setEnvironment(cenv);
	}

	public void startApplication( IContainerEnvironment cenv ){
		this.cenv = cenv;
		if( this.frontend != null )
			this.frontend.setEnvironment(cenv);
	}
	
	public void dispose(){
	}

}
