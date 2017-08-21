package org.condast.wph.core.design;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.symbiotic.core.transformation.AbstractModelTransformer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.IMessageListener;
import org.condast.wph.core.message.MessageEvent;
import org.condast.wph.core.message.MessageHandler;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.Anchorage;

public class TAnchorage extends AbstractLinkedTransformation<IShip, Boolean> 
implements IIntervalProcess<IShip, Boolean>{

	private long interval;
	
	public TAnchorage( Anchorage anchorage, IBehaviour<IShip,Integer> behaviour, INeighbourhood<IShip, Boolean> outputNode) {
		super( ModelTypes.ANCHORAGE.toString(), outputNode);
		super.setTransformer( new TRAnchorage( anchorage, behaviour));
	}

	@Override
	protected void onOutputBlocked( Boolean output ) {
		MessageHandler handler = MessageHandler.getInstance();
		handler.sendMessage( Parties.PORTMASTER, "dock ship");
	}

	public Anchorage getModel() {
		TRAnchorage tanc = (TRAnchorage) getTransformer();
		return (Anchorage)tanc.getModel();
	}

	@Override
	public void next( long interval ) {
		this.interval = interval;
		super.transform();	
	}

	private class TRAnchorage extends AbstractModelTransformer<Anchorage, IShip, Boolean, Integer>{

		private Anchorage anchorage;
		private boolean sendMessage;
		private MessageHandler handler = MessageHandler.getInstance();
		private IMessageListener listener = new IMessageListener() {

			@Override
			public void notifyMessageReceived(MessageEvent event) {
				logger.info("Response " + event.getParty() + ": " + event.getResult());
			}
		};
		
		private Logger logger = Logger.getLogger( this.getClass().getName() );

		public TRAnchorage( Anchorage anchorage, IBehaviour<IShip, Integer> behaviour ) {
			super( ModelTypes.ANCHORAGE.toString(), anchorage, behaviour );
			this.anchorage = anchorage;
			handler.addMessageListener(listener);
		}

		@Override
		public boolean addInput( IShip ship ) {
			boolean retval = super.addInput(ship);
			this.anchorage.addShip( ship );
			return retval;
		}

		@Override
		public boolean removeInput(IShip input) {
			boolean retval = super.removeInput( input );
			this.anchorage.removeShip( input );
			return retval;
		}

		@Override
		protected Boolean onTransform( Iterator<IShip> inputs) {
			return !this.anchorage.isEmpty();
		}

		@Override
		protected void onUpdateStress( Iterator<IShip> inputs, ISymbiot symbiot) {
			if( this.anchorage.isEmpty()){
				symbiot.clearStress();
				this.sendMessage = false;
			}
			else{
				float quotient = 60 * this.anchorage.getMaxWaitingTime();
				float longest = (float)this.anchorage.getLongestWaitingTime( interval );
				symbiot.setStress(NumberUtils.clip(1, longest/quotient));
				if( !this.sendMessage ){
					this.sendMessage = true;
					handler.sendMessage( Parties.PORTMASTER, "help");
				}
			}
		}

		@Override
		public Collection<IShip> getInputs() {
			return anchorage.getInputs();
		}
	}
}