package org.condast.wph.builder.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.latlng.LatLng;
import org.condast.symbiotic.core.DefaultBehaviour;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.design.CapacityTransformation;
import org.condast.wph.core.design.MultiCapacityTransformation;
import org.condast.wph.core.design.TAnchorage;
import org.condast.wph.core.design.TTerminal;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Modality;
import org.condast.wph.core.model.Ship;
import org.condast.wph.core.model.StakeHolder;
import org.condast.wph.core.model.Terminal;
import org.condast.wph.core.model.WaterWay;

public class ShipEntry {
	
	public static final int DEFAULT_INTERVAL = 3*60*1000; //3 min
	
	private SymbiotCollection symbiots;
	private Map<IStakeHolder<?,?>,IBehaviour<?,?>> models;
	
	private int index;
	private int interval;
	
	private TAnchorage tanch;

	private Collection<ITransformListener<IShip>> listeners;
	
	private ITransformListener<IShip> listener = new ITransformListener<IShip>(){

		@Override
		public void notifyChange(TransformEvent<IShip> event) {
			for( ITransformListener<IShip> listener: listeners )
				listener.notifyChange(event);
		}
	};

	public ShipEntry( Environment environment) {
		this.symbiots = new SymbiotCollection();
		this.models = new HashMap<IStakeHolder<?,?>,IBehaviour<?,?>>();
		
		this.index = 0;
		this.interval = DEFAULT_INTERVAL;
		this.listeners = new ArrayList<ITransformListener<IShip>>();
		createDependencies();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createDependencies(){
		IBehaviour<IShip, Integer> behaviour = new DefaultBehaviour<>(5);
		String name = "Modalities";
		IModel<IModel.ModelTypes> model =  new Modality( name, ModelTypes.LOGISTICS, new LatLng(4.5f, 51.8f));
		IStakeHolder<IContainer, IShip> mod = (IStakeHolder<IContainer, IShip>) 
				setupTransformation( model, behaviour, new MultiCapacityTransformation( name ));
		
		name = "APM-T";
		model =  new Terminal( name, new LatLng(4.2f, 51.8f), 3);
		IStakeHolder<IShip, IContainer> term = (IStakeHolder<IShip, IContainer>) 
				setupTransformation( model, behaviour, new TTerminal( (Terminal) model, behaviour, (ITransformation<IContainer, IShip>) mod.getTransformation()));
		
		name = "Nieuwe Maas";
		model = new WaterWay(name, ModelTypes.PORT_AUTHORITY, new LatLng(4.3f, 51.8f));
		behaviour = new DefaultBehaviour<>(5);
		IStakeHolder<IShip, IShip> waterway = 
				(IStakeHolder<IShip, IShip>) setupTransformation( model, behaviour, new CapacityTransformation<IShip>(name, behaviour, (ICapacityProcess) term.getTransformation() ));

		name = "Hoek van Holland";
		behaviour = new DefaultBehaviour<>(5);
		model = new Anchorage( name, new LatLng(4.2f, 51.8f), 3 );
		tanch = new TAnchorage(( Anchorage )model, behaviour, (ITransformation<IShip, IShip>) waterway.getTransformation() );
		tanch.addTransformationListener(listener);	
		setupTransformation( model, behaviour, tanch);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IStakeHolder<?, ?> setupTransformation( IModel model, IBehaviour<IShip, Integer> behaviour, IIntervalProcess<?,?> transformation ){
		symbiots.add( createId( (ModelTypes) model.getType(), model.getId()), behaviour);
		IStakeHolder<?, ?> term = new StakeHolder(  transformation, (ModelTypes) model.getType(), model.getLnglat() );
		models.put(term, behaviour );
		return term;
	}

			
	public Map<IStakeHolder<?,?>,IBehaviour<?,?>> getModels(){
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
		tanch.addInput( new Ship());
		long time = index*interval;
		index++;
		for( IStakeHolder<?,?> stakeholder: models.keySet() ){
			stakeholder.next(time);
		}
	}
	
	
	//private int getIndex( boolean direction, int current ){
	//	return direction? current++: current--;
	//}
	
	private String createId( ModelTypes type, String name ){
		return type.toString() + ": " + name;
	}
}
