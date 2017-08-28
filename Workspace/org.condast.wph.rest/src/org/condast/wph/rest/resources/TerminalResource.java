package org.condast.wph.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
	public String requestSlot(@QueryParam("party") String party) {
		TTerminal term= (TTerminal) dispatcher.getStakeholder( ModelTypes.TERMINAL ).getTransformation();
		boolean result = term.getMessageController().sendMessage(party, "request Slot");
		return result? "OK": "DENY";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/requestDockLater")
	public String requestLater() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/transportResponse")
	public Response responseTransport(@QueryParam("party")String party, @QueryParam("response")String response ) {
		TTerminal term= (TTerminal) dispatcher.getStakeholder( ModelTypes.TERMINAL ).getTransformation();
		return createResponse( term.getMessageController().handleResponse( party, response ));
	}

	private static Response createResponse( String message ){
		ResponseBuilder builder = Response.ok( message );

		builder.status(200)
		.header("Access-Control-Allow-Origin", "*")
		.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		.header("Access-Control-Allow-Credentials", "true")
		.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		.header("Access-Control-Max-Age", "1209600");
		return builder.build();
	}

}