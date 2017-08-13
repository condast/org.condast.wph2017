package org.condast.wph.builder.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.condast.commons.latlng.LatLng;
import org.condast.symbiotic.core.AbstractNeighbourhood;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.definition.IContainer;
import org.condast.wph.core.definition.IJourney;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Terminal;
import org.condast.wph.core.symbiot.AnchorageBot;
import org.condast.wph.core.symbiot.TerminalBot;

public class Journey implements IJourney {
	
	private Environment environment;
	private ISymbiot<?,?> transport;
	private IContainer container;
	private List<ISymbiot<?,?>> chain;
	private int index;
	
	private static ModelProvider provider = ModelProvider.getInstance();

	public Journey( IContainer container, Environment environment) {
		this.container = container;
		this.environment = environment;
		chain = new ArrayList<ISymbiot<?,?>>();
		this.index = 0;
		createDependencies();
	}
	
	private void createDependencies(){
		int index = 0;
		ISymbiot<?,?> anch = new AnchorageBot( new Anchorage( "Hoek van Holland", new LatLng(4.2f, 51.8f), 3));
		ISymbiot<?,?> term = new TerminalBot( new Terminal( "APM-T", new LatLng(4.2f, 51.8f), 3));
/*				
				create( IModel.ModelTypes.CLIENT );
		chain.add( client );
		ISymbiot<?,?> supplier = create( IModel.ModelTypes.SUPPLIER );
		container.setLnglat( supplier.getModel().getLnglat());
		chain.add( supplier );
		environment.addNeighbourhood(client, supplier, new Neighbourhood(index++));
		ISymbiot<?,?> shipagent = create( IModel.ModelTypes.SHIPPING_AGENT );
		chain.add( shipagent );
		index = createJourney(shipagent, environment, index, false);
		index = createJourney(shipagent, environment, index, false);
		
		environment.addNeighbourhood(anch, term, new Neighbourhood( index ));
		*/
	}

	/*
	private int createJourney( ISymbiot<?,?> shipagent, Environment environment, int index, boolean destination){
		transport = create( IModel.ModelTypes.LOGISTICS );
		chain.add( transport );
		environment.addNeighbourhood(shipagent, transport, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<?,?> terminal = create( IModel.ModelTypes.TERMINAL );
		chain.add( terminal );
		environment.addNeighbourhood(transport, terminal, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<?,?> shipowner = create( IModel.ModelTypes.SHIP_OWNER );
		chain.add( shipowner );
		environment.addNeighbourhood(terminal, shipowner, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<?,?> port = create( IModel.ModelTypes.PORT_AUTHORITY );
		chain.add( port );
		environment.addNeighbourhood(shipowner, port, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<?,?> tug = create( IModel.ModelTypes.TUG_BOAT );
		chain.add( tug );
		environment.addNeighbourhood(port, tug, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<?,?> pilot = create( IModel.ModelTypes.PILOT );
		chain.add( pilot );
		environment.addNeighbourhood(port, pilot, new Neighbourhood( index ));
		return getIndex( destination, index);
	}
	*/
	
	@Override
	public IContainer getContainer() {
		return container;
	}
	
	@Override
	public ISymbiot<?,?> next(){
		if( index < chain.size()-1 )
			this.index += 1;
		ISymbiot<?,?> symbiot = chain.get(index);
		//container.setLnglat( symbiot.getModel().getLnglat() );
		return symbiot;
	}
	
	@Override
	public boolean isCompleted(){
		return this.index >= chain.size();
	}
	
	private int getIndex( boolean direction, int current ){
		return direction? current++: current--;
	}
}
