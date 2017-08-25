package org.condast.wph.rest.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.wph.core.definition.IContainerEnvironment;
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
		SymbiotCollection symbiots = (SymbiotCollection) ce.getSymbiots();
		Map<String, Float> stress = ( symbiots == null )? new HashMap<String, Float>(): symbiots.getCumultatedStress();
		for( ISymbiot symbiot: symbiots )	
			stress.put( symbiot.getId(), symbiot.getStress() );
		return gson.toJson( stress );
	}

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/weights")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWeights() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		Gson gson = new Gson();
		SymbiotCollection symbiots = (SymbiotCollection) ce.getSymbiots();
		Map<String, Float> weights = ( symbiots == null )? new HashMap<String, Float>(): symbiots.getCumultatedStress();
		for( ISymbiot symbiot: symbiots )	
			weights.put( symbiot.getId(), symbiot.getStress() );
		return gson.toJson( weights );
	}

}
