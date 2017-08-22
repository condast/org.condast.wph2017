package org.condast.wph.rest;

import javax.servlet.Servlet;
import javax.ws.rs.ApplicationPath;

import org.condast.commons.http.AbstractServletWrapper;
import org.condast.wph.rest.resources.AnchorageResource;
import org.condast.wph.rest.resources.ControlResource;
import org.condast.wph.rest.resources.StressResource;
import org.condast.wph.rest.resources.TerminalResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServlet extends AbstractServletWrapper {

	public static final String S_CONTEXT_PATH = "rest";
	
	public RestServlet() {
		super( S_CONTEXT_PATH );
	}
	
	@Override
	protected Servlet onCreateServlet(String contextPath) {
		RestApplication resourceConfig = new RestApplication();
		return new ServletContainer(resourceConfig);
	}

	@ApplicationPath(S_CONTEXT_PATH)
	private class RestApplication extends ResourceConfig {

		//Loading classes is the safest way...
		//in equinox the scanning of packages may not work
		private RestApplication() {
			register( ControlResource.class );
			register( StressResource.class );
			register( TerminalResource.class );
			register( AnchorageResource.class );
		}
	}
}
