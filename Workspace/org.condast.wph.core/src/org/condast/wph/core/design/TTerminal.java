package org.condast.wph.core.design;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.condast.symbiotic.core.transformation.Transformation;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformerWrapper;
import org.condast.symbiotic.core.transformer.AbstractProcessWrapper;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.message.MessageHandler;
import org.condast.wph.core.message.MessageHandler.Parties;
import org.condast.wph.core.model.Container;
import org.condast.wph.core.model.IntervalProcess;
import org.condast.wph.core.model.Terminal;

public class TTerminal extends Transformation<IShip, IContainer> implements ICapacityProcess<IShip, IContainer>,
	IIntervalProcess<IShip, IContainer>{

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}

	private MessageHandler handler = MessageHandler.getInstance();

	private boolean sendPMMessage;
	private boolean sendModMessage;
	
	private Terminal terminal;
	private ProcessTransformation process;
	
	public TTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour ) {
		super( ModelTypes.TERMINAL.toString() );
		this.terminal = terminal;
		this.process = new ProcessTransformation( terminal.getId(), this.terminal.getMaxDocks() );
		super.setTransformer( new TRTerminal( terminal, behaviour));
	}

	public Terminal getModel() {
		return terminal;
	}
	
	public boolean allowNextShip(){
		TRTerminal trt = (TRTerminal) super.getTransformer();
		return trt.allowNextShip();
	}

	@Override
	public Date getFirstDueJob() {
		return process.getFirstDueDate();
	}

	@Override
	public int getJobSize() {
		return super.getInputSize();
	}

	@Override
	public int getCapacity() {
		return process.getCapacity();
	}

	@Override
	public int getReaminingCapacity() {
		return process.getCapacity() - super.getInputSize();
	}

	@Override
	public boolean isFull() {
		return super.getInputSize() >= process.getCapacity();
	}
	
	@Override
	protected void onHandleOutput(ITransformListener<IContainer> listener, TransformEvent<IContainer> event) {
		if( event.accept ){
			Collection<IShip> temp = new ArrayList<IShip>( super.getInput());
			for( IShip input: temp ){
				//for( )
				super.removeInput( input );
			}
		}
		super.onHandleOutput(listener, event);
	}

	@Override
	public void next( long time ) {
		process.next(time);
		IContainer result = super.transform();
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

	public class TRTerminal extends AbstractBehavedTransformerWrapper<IShip, IContainer, Integer>{

		private Terminal terminal;
		private Map<IShip, Integer> buffer;

		public TRTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour ) {
			super( process, behaviour);
			this.terminal = terminal;
			buffer = new HashMap<IShip, Integer>();
		}

		protected Terminal getTerminal() {
			return terminal;
		}

		public boolean allowNextShip(){
			IBehaviour<IShip,Integer> behaviour = super.getBehaviour();
			int slack = behaviour.getOutput() * 15;//minutes
			long firstJob = process.getFirstDueDate().getTime() - Calendar.getInstance().getTimeInMillis();
			return( firstJob < slack );
		}
		
		
		@Override
		public boolean addInput(IShip input) {
			if( input == null )
				return false;
			buffer.put(input, input.getNrOfContainers());
			return super.addInput(input);
		}

		@Override
		protected IContainer onTransform(Iterator<IShip> inputs, IContainer output) {
			if( inputs == null )
				return null;
			IShip ship = null;
			int remaining= 0;
			while( inputs.hasNext() ){
				ship = inputs.next();
				if( buffer.get( ship ) != null )
					remaining = buffer.get( ship );
				break;
			}
			buffer.replace(ship, remaining-- );
			if( remaining == 0 )
				removeInput(ship );
			String tag = ship.getName() + ": " + remaining;
			Date date = Calendar.getInstance().getTime();
			return new Container( tag, ship.getTimeStamp(), date, date );
		}

		@Override
		protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
			float stress = getReaminingCapacity()/getCapacity();
			symbiot.setStress(stress);
		}
		
		protected Date getJobCompletion( IShip ship ){
			Date current = Calendar.getInstance().getTime();
			long interval = ship.getNrOfContainers() * getModel().getUnloadTime();
			current.setTime( current.getTime() + interval );
			return current;
		}
	}
	
	private static class ProcessTransformation extends AbstractProcessWrapper<IShip, IContainer>{

		protected ProcessTransformation( String name, int capacity) {
			super( new IntervalProcess<IShip>( name, capacity));
		}

		@Override
		public boolean addInput( IShip input ){
			if( input == null )
				return false;
			boolean retval = getProcess().addInput(input, getJobcompletion(input));
			if( retval )
				super.addInput(input);
			return retval;
		}
		
		public Date getJobcompletion( IShip input ){
			return IntervalProcess.getSimulatedTime( 3 * IntervalProcess.TO_HOURS );
		}
		
		public IntervalProcess<IShip> getProcess(){
			return (IntervalProcess<IShip>) super.getTransformer();
		}
		
		public Date getFirstDueDate() {
			return getProcess().getFirstDueDate();
		}

		public int getCapacity() {
			return getProcess().getCapacity();
		}

		public void next( long time ){
			getProcess().next(time);
		}
		
		@Override
		protected IContainer onTransform(Iterator<IShip> inputs) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}