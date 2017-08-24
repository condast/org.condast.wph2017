package org.condast.wph.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.design.TTerminal;
import org.condast.wph.rest.service.Dispatcher;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/terminal")
public class TerminalResource{

	private Dispatcher dispatcher = Dispatcher.getInstance();

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/requestSlot")
	public String requestSlot() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		TTerminal term= (TTerminal) ce.getStakeHolder( ModelTypes.TERMINAL ).getTransformation();
		return term.allowNextShip()? "OK": "DENY";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/requestDockLater")
	public String requestLater() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}
}