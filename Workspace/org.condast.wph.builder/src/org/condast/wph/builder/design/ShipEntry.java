package org.condast.wph.builder.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.condast.commons.latlng.LatLng;
import org.condast.symbiotic.core.DefaultBehaviour;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.def.ITransformer;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.core.transformation.IModelTransformer;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.symbiotic.core.transformation.Transformation;
import org.condast.wph.core.def.IIntervalTransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.design.TAnchorage;
import org.condast.wph.core.design.TTerminal;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Ship;
import org.condast.wph.core.model.Terminal;

public class ShipEntry {
	
	public static final int DEFAULT_INTERVAL = 15*60*1000; //15 min
	
	private SymbiotCollection symbiots;
	private Map<ModelTypes, IntervalTransformation<?>> models;
	
	private List<ISymbiot> chain;
	private int index;
	private int interval;

	private Collection<ITransformListener<Boolean>> listeners;
	
	private ITransformListener<Boolean> listener = new ITransformListener<Boolean>(){

		@Override
		public void notifyChange(TransformEvent<Boolean> event) {
			for( ITransformListener<Boolean> listener: listeners )
				listener.notifyChange(event);
			
		}
	};


	public ShipEntry( Environment environment) {
		this.symbiots = new SymbiotCollection();
		this.models = new HashMap<ModelTypes, IntervalTransformation<?>>();
		
		chain = new ArrayList<ISymbiot>();
		this.index = 0;
		this.interval = DEFAULT_INTERVAL;
		this.listeners = new ArrayList<ITransformListener<Boolean>>();
		createDependencies();
	}
	
	private void createDependencies(){
		IBehaviour<IShip, Integer> behaviour = new DefaultBehaviour<>(5);
		
		ModelTypes type = ModelTypes.ANCHORAGE;
		String name = "Hoek van Holland";
		symbiots.add( createId(type, name), behaviour);
		IntervalTransformation<Anchorage> anch = new IntervalTransformation<Anchorage>( ModelTypes.ANCHORAGE.toString(), 
				new TAnchorage( new Anchorage( name, new LatLng(4.2f, 51.8f), 3), behaviour));
		this.models.put(ModelTypes.ANCHORAGE, anch);
		anch.addTransformationListener(listener);
		
		type = ModelTypes.TERMINAL;
		name = "APM-T";
		behaviour = new DefaultBehaviour<>(5);
		symbiots.add(createId(type, name), behaviour);
		IntervalTransformation<Terminal> term = new IntervalTransformation<Terminal>( type.toString(),
				new TTerminal( new Terminal( "APM-T", new LatLng(4.2f, 51.8f), 3), behaviour ));
		this.models.put(ModelTypes.TERMINAL, term);
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
	
	public ITransformation<?,?> getTransformation( ModelTypes type ){
		return models.get( type );
	}

	/**
	 * Get the collection of symbiots
	 * @return
	 */
	public SymbiotCollection getSymbiots() {
		return symbiots;
	}

	public void addTransformListener( ITransformListener<Boolean> listener ){
		this.listeners.add( listener );
	}

	public void removeTransformListener( ITransformListener<?> listener ){
		this.listeners.remove( listener );
	}

	public void next(){
		TAnchorage tanch = (TAnchorage) models.get(ModelTypes.ANCHORAGE ).getTransformer();
		tanch.addInput( new Ship());
		for( IIntervalTransformation<?,?> trf: models.values() )
			trf.next(interval);
	}
	
	public boolean isCompleted(){
		return this.index >= chain.size();
	}
	
	//private int getIndex( boolean direction, int current ){
	//	return direction? current++: current--;
	//}
	
	private String createId( ModelTypes type, String name ){
		return type.toString() + ": " + name;
	}
	
/*
	private class ShipNeighbourhood extends AbstractNeighbourhood< Boolean, IShip>{

		private Map<IShip,TimedNode> nodes;
		private long time;
		
		protected ShipNeighbourhood(String name, long time ) {
			super(name);
			this.time = time;
			nodes = new HashMap<IShip, TimedNode>();
		}

		
		@Override
		public boolean addInput(IShip input) {
			nodes.put( input, new TimedNode( time ));
			return super.addInput(input);
		}
	}
	*/
	
	private class IntervalTransformation<M extends Object> extends Transformation<IShip, Boolean> implements IIntervalTransformation<IShip, Boolean>{

		public IntervalTransformation(String name, IModelTransformer<M, IShip, Boolean> transformer) {
			super(name, transformer);
		}
		
		protected ITransformer<IShip,Boolean> getTransformer(){
			return super.getTransformer();
		}
		
		@Override
		public void next(int interval) {
			super.transform();
		}		
	}
}
