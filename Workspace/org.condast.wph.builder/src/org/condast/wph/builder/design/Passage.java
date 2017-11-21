package org.condast.wph.builder.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.condast.commons.data.latlng.LatLng;
import org.condast.symbiotic.core.DefaultBehaviour;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.ecosystem.AbstractLinkedNeighbourhood;
import org.condast.symbiotic.core.ecosystem.ILinkedNeighbourhood;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.def.ICarrier;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.design.CapacityTransformation;
import org.condast.wph.core.design.TAnchorage;
import org.condast.wph.core.design.TTerminal;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Carrier;
import org.condast.wph.core.model.Modality;
import org.condast.wph.core.model.Ship;
import org.condast.wph.core.model.StakeHolder;
import org.condast.wph.core.model.Statistics;
import org.condast.wph.core.model.Terminal;
import org.condast.wph.core.model.WaterWay;

public class Passage {
	
	public static final int DEFAULT_INTERVAL = 3*60*1000; //3 min
	
	private SymbiotCollection symbiots;
	private Map<IStakeHolder<?,?>,IBehaviour> models;
	
	private int index;
	private int interval;
	
	private TAnchorage tanch;
	
	private Statistics statistics;

	private Collection<ITransformListener<IShip>> listeners;
	
	private ITransformListener<IShip> listener = new ITransformListener<IShip>(){

		@Override
		public void notifyChange(TransformEvent<IShip> event) {
			for( ITransformListener<IShip> listener: listeners )
				listener.notifyChange(event);
		}
	};

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Passage( Environment environment, SymbiotCollection symbiots) {
		this.symbiots = symbiots;
		this.models = new HashMap<IStakeHolder<?,?>,IBehaviour>();
		
		this.index = 0;
		this.interval = DEFAULT_INTERVAL;
		this.statistics = new Statistics();
		this.listeners = new ArrayList<ITransformListener<IShip>>();
		createDependencies();
	}
	
	public Statistics getStatistics() {
		return statistics;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createDependencies(){
		
		IBehaviour behaviour = new DefaultBehaviour(5);

		String name = "Hoek van Holland";
		IModel<IModel.ModelTypes> model = new Anchorage( name, new LatLng(4.2f, 51.8f), 3 );
		tanch = new TAnchorage(( Anchorage )model, behaviour );
		tanch.addTransformationListener(listener);	
		setupTransformation( model, behaviour, tanch);

		name = "Nieuwe Maas";
		model = new WaterWay(name, ModelTypes.PORT_AUTHORITY, new LatLng(4.3f, 51.8f));
		behaviour = new DefaultBehaviour(5);
		IStakeHolder<IShip, IShip> waterway = 
				(IStakeHolder<IShip, IShip>) setupTransformation( model, behaviour, new CapacityTransformation<IShip>(name, behaviour ));

		ILinkedNeighbourhood<IShip,IShip> entryNeighbourhood = new AbstractLinkedNeighbourhood<IShip, IShip>(){
	
			@Override
			protected void onChange(ITransformation<IShip, ?> transformation, TransformEvent<IShip> event) {
				ICapacityProcess<IShip,IShip> outNode = (ICapacityProcess<IShip, IShip>) transformation;
				boolean accept = event.isAccept()? true: outNode.addInput(event.getOutput()) ;
				if( accept ){
					long time = ( index * interval )/60000;
					//logger.log( Level.parse("FLOW"), String.valueOf( time ) + "\tShip passing to waterway" );
				}
				event.setAccept( accept);
			}			
		};
		entryNeighbourhood.addTransformation( waterway.getTransformation());
		tanch.addTransformationListener(entryNeighbourhood );

		name = "APM-T";
		model =  new Terminal( name, new LatLng(4.2f, 51.8f), 50);//50 docks
		behaviour = new DefaultBehaviour(60);//The range translates to 60 minutes
		IStakeHolder<IShip, IContainer> term = (IStakeHolder<IShip, IContainer>) 
				setupTransformation( model, behaviour, new TTerminal( (Terminal) model, behaviour ));

		ILinkedNeighbourhood<IShip, IShip> dockNeighbourhood = new AbstractLinkedNeighbourhood<IShip, IShip>(){
			
			@Override
			protected void onChange(ITransformation<IShip, ?> transformation, TransformEvent<IShip> event) {
				ICapacityProcess<IShip,IShip> outNode = (ICapacityProcess<IShip, IShip>) transformation;
				boolean accept = event.isAccept()? true: outNode.addInput(event.getOutput()) ;
				if( accept ){
					long time = ( index * interval )/60000;
					//logger.log( Level.parse("FLOW"), String.valueOf( time ) + "\tTerminal has unloaded containers" );
				}
				event.setAccept( accept);
			}		
		};
		dockNeighbourhood.addTransformation( term.getTransformation() );
		waterway.getTransformation().addTransformationListener(dockNeighbourhood );
		
		ILinkedNeighbourhood<IContainer, ICarrier> terminalNeighbourhood = new AbstractLinkedNeighbourhood<IContainer, ICarrier>(){
	
			private Carrier carrier = null;
			
			@Override
			protected void onChange(ITransformation<ICarrier, ?> transformation, TransformEvent<IContainer> event) {
				if( event.getOutput() == null ){
					return; 
				}
				if( carrier == null ){
					carrier = new Carrier( "blah", transformation.getName());
				}
				boolean accept = true;
				if( carrier.getNrOfContainers() < carrier.getMaxContainerSize() ){
					carrier.addContainer(event.getOutput());
				}else{
					long time = index*interval;
					ICapacityProcess<ICarrier,?> outNode = (ICapacityProcess<ICarrier,?>) transformation;
					if( carrier != null )
						statistics.next(time, carrier);
					accept = outNode.addInput(carrier);
					//logger.log( Level.parse("FLOW"), String.valueOf( time ) + "\tCarrier ready to depart" );
				}
				event.setAccept( accept );
			}			
		};
		term.getTransformation().addTransformationListener( terminalNeighbourhood );

		ILinkedNeighbourhood<ICarrier,Boolean> endNeighbourhood = new AbstractLinkedNeighbourhood<ICarrier, Boolean>(){
		
			@Override
			protected void onChange(ITransformation<Boolean, ?> transformation, TransformEvent<ICarrier> event) {
				ICapacityProcess<Boolean,?> outNode = (ICapacityProcess<Boolean,?>) transformation;
				boolean accept = true;// event.isAccept()? true: outNode.addInput(event.getOutput()) ;
				event.setAccept( accept);
			}			
		};

		name = "DB Schenker";
		behaviour = new DefaultBehaviour(5);
		model =  new Modality( name, ModelTypes.TRUCK, new LatLng(4.5f, 51.8f));
		CapacityTransformation<ICarrier> ct = new CapacityTransformation( name, 1, 20, behaviour );//20 containers per hour
		IStakeHolder<IContainer, ICarrier> mod = (IStakeHolder<IContainer, ICarrier>) 
				setupTransformation( model, behaviour, ct);
		terminalNeighbourhood.addTransformation(ct);
		ct.addTransformationListener( endNeighbourhood );

		name = "NedCargo";
		behaviour = new DefaultBehaviour(5);
		model =  new Modality( name, ModelTypes.BARGE, new LatLng(4.5f, 51.8f));
		ct = new CapacityTransformation( name, 1, 40, behaviour );//40 containers per hour
		mod = (IStakeHolder<IContainer, ICarrier>) 
				setupTransformation( model, behaviour, ct);
		terminalNeighbourhood.addTransformation(ct);
		ct.addTransformationListener( endNeighbourhood );

		name = "NS Cargo";
		behaviour = new DefaultBehaviour(5);
		model =  new Modality( name, ModelTypes.TRAIN, new LatLng(4.5f, 51.8f));
		ct = new CapacityTransformation( name, 1, 200, behaviour ); //200 containersv per hour
		mod = (IStakeHolder<IContainer, ICarrier>) 
				setupTransformation( model, behaviour, ct);
		terminalNeighbourhood.addTransformation(ct);
		ct.addTransformationListener( endNeighbourhood );
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IStakeHolder<?, ?> setupTransformation( IModel model, IBehaviour behaviour, IIntervalProcess<?,?> transformation ){
		symbiots.add( ModelTypes.getAbbreviation( (ModelTypes) model.getType() ), behaviour);
		IStakeHolder<?, ?> term = new StakeHolder(  transformation, (ModelTypes) model.getType(), model.getLnglat() );
		models.put(term, behaviour );
		return term;
	}

			
	public Map<IStakeHolder<?,?>,IBehaviour> getModels(){
		return this.models;
	}

	public void addTransformListener( ITransformListener<IShip> listener ){
		this.listeners.add( listener );
	}

	public void removeTransformListener( ITransformListener<?> listener ){
		this.listeners.remove( listener );
	}

	public void clear(){
		this.index = 0;
	}
	
	public void next(){
		//logger.log( Level.parse("FLOW"), "Next Log");
		long time = index*interval;
		boolean newShip = true;// ( 10 * Math.random()) <=1;
		if( newShip )
			tanch.addInput( new Ship( time ));
		index++;
		for( IStakeHolder<?,?> stakeholder: models.keySet() ){
			stakeholder.next(time);
		}
	}
}