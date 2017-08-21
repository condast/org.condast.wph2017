package org.condast.wph.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.rest.service.Dispatcher;

@Path("/control")
public class ControlResource{

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/activate")
	@Produces(MediaType.TEXT_PLAIN)
	public String startStop() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		if( !ce.isRunning())
			ce.start();
		else
			ce.stop();
		return "Started: " + ce.isRunning();
	}

	// This method is called if XML is request
	@GET
	@Path("/isactive")
	@Produces(MediaType.TEXT_PLAIN)
	public String isActive() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		return "Started: " + ce.isRunning();
	}
	
	// This method is called if XML is request
	@GET
	@Path("/elapsed")
	@Produces(MediaType.TEXT_PLAIN)
	public String getElapsedTime(){
		IContainerEnvironment ce = dispatcher.getEnvironment();
		return "Time: " + ce.getSimulatedTime();		
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}}
