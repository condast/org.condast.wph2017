package org.condast.wph.builder.design;

import java.util.ArrayList;
import java.util.List;

import org.condast.symbiotic.core.DefaultBehaviour;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.definition.IJourney;
import org.condast.wph.core.definition.IModel.ModelTypes;

public class Journey implements IJourney {
	
	private SymbiotCollection symbiots;
	
	//private Environment environment;
	//private ISymbiot transport;
	private IContainer container;
	private List<ISymbiot> chain;
	private int index;
	
	//private static ModelProvider provider = ModelProvider.getInstance();

	public Journey( IContainer container, Environment environment) {
		this.symbiots = new SymbiotCollection();
		this.container = container;
		//this.environment = environment;
		chain = new ArrayList<ISymbiot>();
		this.index = 0;
		createDependencies();
	}
	
	private void createDependencies(){
		//int index = 0;
		IBehaviour behaviour = new DefaultBehaviour(5);
		ModelTypes type = ModelTypes.ANCHORAGE;
		String name = "Hoek van Holland";
		symbiots.add( createId(type, name), behaviour);
		//IIntervalTransformation<IShip,Boolean> anch = new IntervalTransformation( ModelTypes.ANCHORAGE.toString(), 
		//		new TAnchorage( new Anchorage( "Hoek van Holland", new LatLng(4.2f, 51.8f), 3), behaviour));

		behaviour = new DefaultBehaviour(5);
		type = ModelTypes.TERMINAL;
		name = "APM-T";
		symbiots.add(createId(type, name), behaviour);
		//IIntervalTransformation<?,?> term = new IntervalTransformation( ModelTypes.TERMINAL.toString(),
		//		new TTerminal( new Terminal( "APM-T", new LatLng(4.2f, 51.8f), 3), behaviour ));
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
	
	private String createId( ModelTypes type, String name ){
		return type.toString() + ": " + name;
	}

	@Override
	public IContainer getContainer() {
		return container;
	}
	
	@Override
	public ISymbiot next(){
		if( index < chain.size()-1 )
			this.index += 1;
		ISymbiot symbiot = chain.get(index);
		//container.setLnglat( symbiot.getModel().getLnglat() );
		return symbiot;
	}
	
	@Override
	public boolean isCompleted(){
		return this.index >= chain.size();
	}
	
	public int getIndex( boolean direction, int current ){
		return direction? current++: current--;
	}
}
