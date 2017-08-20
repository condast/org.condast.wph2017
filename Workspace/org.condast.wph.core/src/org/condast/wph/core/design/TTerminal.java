package org.condast.wph.core.design;

import java.util.Iterator;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.symbiotic.core.transformation.AbstractModelTransformer;
import org.condast.wph.core.def.ICapacityTransformation;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalTransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.MessageHandler;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.Terminal;

public class TTerminal extends AbstractLinkedTransformation<IShip, IContainer> implements ICapacityTransformation<IShip, IContainer>,
	IIntervalTransformation<Terminal, IShip, IContainer>{

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}

	private MessageHandler handler = MessageHandler.getInstance();

	private boolean sendPMMessage;
	private boolean sendModMessage;

	public TTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour, INeighbourhood<IShip, IContainer> neighbourhood) {
		super( ModelTypes.TERMINAL.toString(), neighbourhood );
		super.setTransformer( new TRTerminal( terminal, behaviour));
	}

	public Terminal getModel() {
		return (Terminal) super.getTransformer();
	}

	@Override
	public boolean addInput(IShip ship) {
		super.addInput(ship);
		return getModel().addJob( ship.getName(), ship.getNrOfContainers() * getModel().getUnloadTime() );
	}

	
	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onOutputBlocked(IContainer output) {
		// TODO Auto-generated method stub
		
	}

	public void next(int interval) {
		this.sendModMessage = false;
		this.sendPMMessage = false;
		if(!this.sendModMessage){
			handler.sendMessage( Parties.PORTMASTER, "help");				
		}
		if( !this.sendModMessage ){
			this.sendModMessage = true;
			handler.sendMessage( Parties.TRAIN, "help");
			handler.sendMessage( Parties.TRUCK, "help");
			handler.sendMessage( Parties.BARGE, "help");
		}

	}

	public class TRTerminal extends AbstractModelTransformer<Terminal, IShip, IContainer, Integer>{

		private int interval;
		private Terminal terminal;

		public TRTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour ) {
			super( ModelTypes.TERMINAL.toString(), terminal, behaviour);
			this.terminal = terminal;
		}

		@Override
		public boolean addInput(IShip ship) {
			super.addInput(ship);
			return this.terminal.addJob( ship.getName(), ship.getNrOfContainers() * terminal.getUnloadTime() );
		}

		@Override
		protected IContainer onTransform(Iterator<IShip> inputs) {
			boolean retval = this.terminal.update( interval );
			return null;
		}

		@Override
		protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
			if( !this.terminal.isAvailable()){
				symbiot.clearStress();
			}
			else{
				symbiot.increaseStress();
			}
		}
	}
}