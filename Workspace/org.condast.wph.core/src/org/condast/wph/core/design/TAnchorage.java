package org.condast.wph.core.design;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.filter.ITransformFilter;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformer;
import org.condast.symbiotic.core.transformer.LinkedTransformation;
import org.condast.symbiotic.core.transformer.FilteredTransformer;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.IMessageListener;
import org.condast.wph.core.message.MessageEvent;
import org.condast.wph.core.message.MessageHandler;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.IntervalProcess;

public class TAnchorage extends LinkedTransformation<IShip, IShip>
implements IIntervalProcess<IShip, IShip>, ICapacityProcess<IShip, IShip>{

	private long interval;
	private Anchorage anchorage;
	private TRAnchorage tanc;
	private LinkedHashMap<Date, IShip> anchorTime;

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public TAnchorage( Anchorage anchorage, IBehaviour<IShip,Integer> behaviour, ITransformation<IShip, IShip> outputNode) {
		super( ModelTypes.ANCHORAGE.toString(), outputNode );
		anchorTime = new LinkedHashMap<Date, IShip>();
		tanc = new TRAnchorage( behaviour);
		FilteredTransformer<IShip, IShip> ft = new FilteredTransformer<IShip, IShip>( tanc);
		ft.addFilter( new LinkedFilter( tanc, outputNode ));
		super.setTransformer( ft );
		this.anchorage = anchorage;
	}

	public Anchorage getModel() {
		return anchorage;
	}

	@Override
	public void next( long interval ) {
		this.interval = interval;
		IShip ship = super.transform();	
		ICapacityProcess<?,?> outNode = (ICapacityProcess<?,?>) super.getOutputNode();
		if( !outNode.isFull() ){
			removeInput(ship);
		}	
	}
	
	@Override
	public int getReaminingCapacity() {
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

	/**
	 * The linked filter checks to see if the stress needs to be updated
	 * @author Kees
	 *
	 */
	private class LinkedFilter implements ITransformFilter< IShip, IShip>{

		private boolean sendMessage;
		private MessageHandler handler = MessageHandler.getInstance();
		private IMessageListener listener = new IMessageListener() {

			@Override
			public void notifyMessageReceived(MessageEvent event) {
				logger.info("Response " + event.getParty() + ": " + event.getResult());
			}
		};

		public LinkedFilter( TRAnchorage transformer, ITransformation<IShip, ?> outputNode) {
			handler.addMessageListener(listener);
		}
	
		@Override
		public boolean accept(IShip input) {
			if( tanc.isEmpty()){
				this.sendMessage = false;
			}else{
				float longest = (float)getLongestWaitingTime( interval );
				if(( !this.sendMessage ) && ( longest > 1 )){
					this.sendMessage = true;
					handler.sendMessage( Parties.PORTMASTER, "dock ship");
				}
			}
			return true;
		}


		@Override
		public boolean acceptTransform(Iterator<IShip> inputs) {
			return true;
		}			
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

	private class TRAnchorage extends AbstractBehavedTransformer<IShip, IShip, Integer>{

		protected TRAnchorage( IBehaviour<IShip, Integer> behaviour) {
			super(behaviour);
		}

		@Override
		public boolean addInput(IShip input) {
			if( input == null )
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