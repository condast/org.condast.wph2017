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
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.design.CapacityNeighbourhood;
import org.condast.wph.core.design.TAnchorage;
import org.condast.wph.core.design.TTerminal;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Ship;
import org.condast.wph.core.model.Terminal;

public class ShipEntry {
	
	public static final int DEFAULT_INTERVAL = 15*60*1000; //15 min
	
	private SymbiotCollection symbiots;
	private Map<ModelTypes, IIntervalProcess<?,?>> models;
	
	private List<ITransformation<?,?>> chain;
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
		this.models = new HashMap<ModelTypes, IIntervalProcess<?,?>>();
		
		chain = new ArrayList<ITransformation<?,?>>();
		this.index = 0;
		this.interval = DEFAULT_INTERVAL;
		this.listeners = new ArrayList<ITransformListener<Boolean>>();
		createDependencies();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createDependencies(){
		IBehaviour<IShip, Integer> behaviour = new DefaultBehaviour<>(5);
		
		String name = "APM-T";
		IIntervalProcess<IShip, IContainer> term = (IIntervalProcess<IShip, IContainer>) 
				setupTransformation(ModelTypes.TERMINAL, name, behaviour, new TTerminal( new Terminal( name, new LatLng(4.2f, 51.8f), 3), behaviour, null));

		INeighbourhood<IShip, Boolean> neighbourhood = new CapacityNeighbourhood<IShip, Boolean>("Nieuwe Maas", (ICapacityProcess) term );
		chain.add(neighbourhood);

		name = "Hoek van Holland";
		behaviour = new DefaultBehaviour<>(5);
		ITransformation<IShip, Boolean> anch = 
				(ITransformation<IShip, Boolean>) setupTransformation(ModelTypes.ANCHORAGE, name, behaviour, new TAnchorage( new Anchorage( name, new LatLng(4.2f, 51.8f), 3), behaviour, neighbourhood ));
		anch.addTransformationListener(listener);	
	}

	private IIntervalProcess<?, ?> setupTransformation( ModelTypes type, String name, IBehaviour<IShip, Integer> behaviour, ITransformation<?,?> transformation ){
		symbiots.add( createId(type, name), behaviour);
		IIntervalProcess<?, ?> term = (IIntervalProcess<?, ?>) transformation;
		models.put(type, term );
		return term;
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
		return (ITransformation<?, ?>) models.get( type );
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

	public void clear(){
		this.index = 0;
	}
	
	public void next(){
		TAnchorage tanch = (TAnchorage) models.get(ModelTypes.ANCHORAGE );
		tanch.addInput( new Ship());
		long time = index*interval;
		index++;
		for( IIntervalProcess<?,?> trf: models.values() )
			trf.next(time);
		for( ITransformation<?,?> neighbourhood: chain )
			neighbourhood.transform();
	}
	
	
	//private int getIndex( boolean direction, int current ){
	//	return direction? current++: current--;
	//}
	
	private String createId( ModelTypes type, String name ){
		return type.toString() + ": " + name;
	}
	
/*
	private static class ShipNeighbourhood extends AbstractNeighbourhood< Boolean, IShip>{

		private Map<IShip,Date> nodes;
		private boolean block;

		protected ShipNeighbourhood( String name, ITransformation<IShip, Boolean> inNode, ITransformation<IShip, Boolean>outNode) {
			super(name, inNode, outNode );
			this.block = false;
			nodes = new HashMap<IShip, Date>();
		}

		private boolean isBlocked() {
			return block;
		}
		
		@Override
		public boolean addInput(IShip input) {
			if( isBlocked())
				return false;
			Date current = Calendar.getInstance().getTime();
			current.setTime( current.getTime() + input.getAverageTransportTime());
			nodes.put( input, current);
			return super.addInput(input);
		}


		@Override
		public Boolean transform() {
			Iterator<Map.Entry<IShip,Date>> iterator = nodes.entrySet().iterator();
			Date current = Calendar.getInstance().getTime();
			while( iterator.hasNext() ){
				Map.Entry<IShip,Date> entry = iterator.next();
				if( entry.getValue().getTime() <= current.getTime() ){
					super.transform();
				}
				
			}
			return false;
		}
	}
	
		
		@Override
		public void next(int interval) {

			switch( neighbourhood.getType() ){
			case OUT:
				if( ShipNeighbourhood.Types.OUT.equals( neighbourhood.getType() ) && 
						neighbourhood.isBlocked() ){
					//MESSAGE
				}else
					super.transform();
				break;
			default:
				if( neighbourhood.isEmpty()){
					if( super.i)
					//MESSAGE
				}
				break;
			}
			
	}
	*/
}
