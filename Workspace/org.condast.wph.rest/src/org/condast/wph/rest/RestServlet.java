package org.condast.wph.rest;

import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.condast.wph.rest.resources.AnchorageResource;
import org.condast.wph.rest.resources.ControlResource;
import org.condast.wph.rest.resources.OverviewResource;
import org.condast.wph.rest.resources.TerminalResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletName;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;

@Component(service = Servlet.class, 
scope=ServiceScope.PROTOTYPE)
@HttpWhiteboardServletName( RestServlet.S_SERVLET_NAME )
@HttpWhiteboardServletPattern( RestServlet.S_SERVLET_PATTERN)
public class RestServlet extends ServletContainer{
	private static final long serialVersionUID = 1L;

	public static final String S_CONTEXT_PATH = "wph";
	protected static final String S_SERVLET_NAME = S_CONTEXT_PATH + ".rest";
	public static final String S_SERVLET_PATTERN = "/" + RestServlet.S_CONTEXT_PATH +"/*";//DON'T do this in the annotation!!!

	private Logger logger = Logger.getLogger(RestServlet.class.getName());
	
	public RestServlet() {
		super( new RestApplication());
		logger.info("STARTING REST SERVICE: " + this.getClass().getName()); 
	}
	
	private static class RestApplication extends ResourceConfig {

		public RestApplication() {
			super();
			property(ServerProperties.WADL_FEATURE_DISABLE, true);
			register( ControlResource.class );
			register( OverviewResource.class );
			register( TerminalResource.class );
			register( AnchorageResource.class );
		}
	}
}
