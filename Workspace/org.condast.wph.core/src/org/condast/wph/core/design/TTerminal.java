package org.condast.wph.core.design;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.AbstractBehavedTransformer;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.MessageHandler;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.IntervalProcess;
import org.condast.wph.core.model.Terminal;

public class TTerminal extends AbstractLinkedTransformation<IShip, IContainer> implements ICapacityProcess<IShip, IContainer>,
	IIntervalProcess<IShip, IContainer>{

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}

	private MessageHandler handler = MessageHandler.getInstance();

	private boolean sendPMMessage;
	private boolean sendModMessage;
	private IBehaviour<IShip,Integer> behaviour;

	public TTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour, INeighbourhood<IShip, IContainer> neighbourhood) {
		super( ModelTypes.TERMINAL.toString(), neighbourhood );
		super.setTransformer( new TRTerminal( terminal, behaviour));
		this.behaviour = behaviour;
	}

	public Terminal getModel() {
		return (Terminal) super.getTransformer();
	}

	public IBehaviour<IShip, Integer> getBehaviour(){
		return behaviour;
	}
	
	public boolean allowNextShip(){
		TRTerminal trt = (TRTerminal) super.getTransformer();
		return trt.allowNextShip();
	}
	
	protected Date getJobCompletion( IShip ship ){
		Date current = Calendar.getInstance().getTime();
		long interval = ship.getNrOfContainers() * getModel().getUnloadTime();
		current.setTime( current.getTime() + interval );
		return current;
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

	@Override
	public void next(long interval) {
		super.transform();
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

	public class TRTerminal extends AbstractBehavedTransformer<IShip, IContainer, Integer> implements ICapacityProcess<IShip, IContainer>{

		private Terminal terminal;
		private IntervalProcess<IShip, IContainer> process;

		public TRTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour ) {
			super( behaviour);
			this.terminal = terminal;
			this.process = new IntervalProcess<IShip, IContainer>();
		}

		@Override
		public boolean addInput(IShip input) {
			if( input == null )
				return false;
			return process.addInput(input, getJobCompletion( input ));
		}

		@Override
		public boolean isFull() {
			return getInputSize() >= terminal.getMaxDocks();
		}

		@Override
		public int getCapacity() {
			return terminal.getMaxDocks() - getInputSize();
		}

		public boolean allowNextShip(){
			IBehaviour<IShip,Integer> behaviour = super.getBehaviour();
			int slack = behaviour.getOutput() * 15;//minutes
			long firstJob = process.getFirstDueJob().getTime() - Calendar.getInstance().getTimeInMillis();
			return( firstJob < slack );
		}

		@Override
		protected IContainer onTransform(Iterator<IShip> inputs) {
			if( inputs == null )
				return null;
			while( inputs.hasNext() )
				process.removeInput(inputs.next());
			return null;
		}

		@Override
		protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
			if( this.isFull()){
				symbiot.clearStress();
			}
			else{
				symbiot.increaseStress();
			}
		}
	}
}