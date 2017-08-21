package org.condast.wph.rest.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.rest.service.Dispatcher;

import com.google.gson.Gson;

@Path("/stress")
public class StressResource{

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/overview")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStress() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		Gson gson = new Gson();
		//Map<String, Float> stress = ce.getSymbiots().getCumultatedStress();
		//String result = 
		Map<String, Float> stress = new HashMap<String, Float>();
		for( IModel.ModelTypes type: IModel.ModelTypes.values() )	
			stress.put(type.toString(), (float)Math.random());
		return "Stress: " + gson.toJson( stress );
	}
}
