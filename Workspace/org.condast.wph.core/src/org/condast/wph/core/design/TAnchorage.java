package org.condast.wph.core.design;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.symbiotic.core.transformation.Transformation;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformer;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.MessageController;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.IntervalProcess;

public class TAnchorage extends Transformation<IShip, IShip>
implements IIntervalProcess<IShip, IShip>, ICapacityProcess<IShip, IShip>{

	private long interval;
	private Anchorage anchorage;
	private TRAnchorage tanc;
	private LinkedHashMap<Date, IShip> anchorTime;
	private MessageController mc;
	private boolean blockOutput;

	public TAnchorage( Anchorage anchorage, IBehaviour behaviour) {
		super( ModelTypes.ANCHORAGE.toString() );
		this.mc = new MessageController(behaviour);
		anchorTime = new LinkedHashMap<Date, IShip>();
		tanc = new TRAnchorage( behaviour);
		super.setTransformer( tanc );
		this.anchorage = anchorage;
		this.blockOutput = false;
	}

	public Anchorage getModel() {
		return anchorage;
	}

	public MessageController getMessageController() {
		return mc;
	}

	@Override
	public void next( long interval ) {
		this.interval = interval;
		super.transform();	
	}
	
	
	//Handle the throughput to the next node
	@Override
	protected void onHandleOutput(ITransformListener<IShip> listener, TransformEvent<IShip> event) {
		this.blockOutput = !event.isAccept();
		if( event.isAccept() )
			removeInput(event.getOutput());
		else{
			mc.sendMessage(Parties.TERMINAL, "dockEarly");
		}
		super.onHandleOutput(listener, event);
	}

	@Override
	public int getRemainingCapacity() {
		return Integer.MAX_VALUE - super.getInputSize();
	}

	/**
	 * Get the longest waiting time in minutes
	 * @return
	 */
	public long getLongestWaitingTime( long interval ){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( calendar.getTimeInMillis() + interval );
		if( anchorTime.isEmpty() )
			return calendar.getTimeInMillis();
		Map.Entry<Date, IShip> entry = anchorTime.entrySet().iterator().next();
		long longest = entry.getKey().getTime();
		long diff = calendar.getTimeInMillis() - longest;
		return (long) ((float)diff/Anchorage.TO_MINUTES);
	}

	@Override
	public Date getFirstDueJob() {
		if( anchorTime.isEmpty() )
			return null;
		Map.Entry<Date, IShip> entry = anchorTime.entrySet().iterator().next();
		return entry.getKey();
	}

	@Override
	public int getJobSize() {
		return super.getInputSize();
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

	private class TRAnchorage extends AbstractBehavedTransformer<IShip, IShip>{

		protected TRAnchorage( IBehaviour behaviour) {
			super(behaviour);
		}

		@Override
		public boolean addInput(IShip input) {
			if(( input == null ) || ( blockOutput))
				return false;
			anchorTime.put( IntervalProcess.getSimulatedTime( interval ), input );
			return super.addInput( input );
		}

		@Override
		public boolean removeInput(IShip input) {
			Iterator<Map.Entry<Date, IShip>> iterator = anchorTime.entrySet().iterator();
			Collection<Date> keys = new ArrayList<Date>();
			while( iterator.hasNext() ){
				Map.Entry<Date, IShip> entry = iterator.next();
				if( entry.getValue().equals( input )){
					keys.add(entry.getKey());
				}
			}
			for( Date key: keys )
				anchorTime.remove(key);
			return super.removeInput(input);
		}
		
		public boolean isEmpty(){
			return super.getInputs().isEmpty();
		}
	
		@Override
		protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
			if( isEmpty()){
				symbiot.clearStress();
			}
			else{
				float quotient = 60 * anchorage.getMaxWaitingTime();//hours
				float longest = (float)getLongestWaitingTime( interval );
				symbiot.setStress(NumberUtils.clip(1, longest/quotient));
			}
		}

		@Override
		protected IShip onTransform(Iterator<IShip> inputs) {
			if( anchorTime.isEmpty())
				return null;
			//Linked hashmap preserves the moment of adding
			Map.Entry<Date, IShip> entry = anchorTime.entrySet().iterator().next();
			return entry.getValue();
		}
	}
}