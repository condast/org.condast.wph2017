package org.condast.wph.rest.resources;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.rest.service.Dispatcher;

import com.google.gson.Gson;

@Path("/overview")
public class OverviewResource{

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/stress")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStress() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		Gson gson = new Gson();
		SymbiotCollection symbiots = (SymbiotCollection) ce.getSymbiots();
		Map<String, Float> stress = ( symbiots == null )? new HashMap<String, Float>(): symbiots.getCumultatedStress();
		if( symbiots != null ){
			for( ISymbiot symbiot: symbiots )	
				stress.put( symbiot.getId(), symbiot.getStress() );
		}
		return createResponse( gson.toJson( stress ));
	}

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/weights")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeights() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		Gson gson = new Gson();
		SymbiotCollection symbiots = (SymbiotCollection) ce.getSymbiots();
		Map<String, Float> weights = ( symbiots == null )? new HashMap<String, Float>(): symbiots.getCumultatedStress();
		for( ISymbiot symbiot: symbiots )	
			weights.put( symbiot.getId(), symbiot.getStress() );
		return createResponse( gson.toJson( weights ));
	}

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/throughput")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getThroughput() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		Integer[] throughput = new Integer[0];
		if( ce.getStatistics() != null )
			throughput = ce.getStatistics().getScaledThroughput( ce.getInterval());
		Gson gson = new Gson();
		return createResponse( gson.toJson(throughput));
	}

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/departure")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getDeparture() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		float value = 0f;
		if( ce.getStatistics() != null )
			value = ce.getStatistics().getCostSavingShips( ce.getInterval());
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(value);

		return createResponse( String.valueOf( moneyString ));
	}

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/allStress")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllStress() {
		IContainerEnvironment ce = dispatcher.getEnvironment();
		SymbiotCollection sc = (SymbiotCollection) ce.getSymbiots();
		Map<String, Map<String,Float>> allStress = sc.getStress();
		Gson gson = new Gson();
		return createResponse( gson.toJson(allStress) );
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