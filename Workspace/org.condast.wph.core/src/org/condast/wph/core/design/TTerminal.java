package org.condast.wph.core.design;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import org.condast.wph.core.message.MessageController;
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

	private Terminal terminal;
	private ProcessTransformation process;
	private MessageController mc;
    //private Logger logger = Logger.getLogger( this.getClass().getName());
	
	public TTerminal( Terminal terminal, IBehaviour behaviour ) {
		super( ModelTypes.TERMINAL.toString() );
		this.terminal = terminal;
		this.process = new ProcessTransformation( terminal.getId(), this.terminal.getMaxDocks() );
		mc = new MessageController( behaviour );
		super.setTransformer( new TRTerminal( terminal, behaviour));
	}

	public Terminal getModel() {
		return terminal;
	}
	
	public MessageController getMessageController() {
		return mc;
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
	public int getRemainingCapacity() {
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
				super.removeInput( input );
			}
		}
		super.onHandleOutput(listener, event);
	}

	@Override
	public void next( long time ) {
		process.next(time);
		super.transform();
	}

	public class TRTerminal extends AbstractBehavedTransformerWrapper<IShip, IContainer>{

		private Terminal terminal;
		private Map<IShip, Integer> buffer;

		public TRTerminal( Terminal terminal, IBehaviour behaviour ) {
			super( process, behaviour);
			this.terminal = terminal;
			buffer = new HashMap<IShip, Integer>();
		}

		protected Terminal getTerminal() {
			return terminal;
		}		

		public boolean allowNextShip(){
			IBehaviour behaviour = super.getBehaviour();
			int slack = behaviour.getValue() * 15;//minutes
			//logger.log( Level.parse("FLOW"), "Behaviour: " + slack );
			Date dueDate = process.getFirstDueDate();
			if( dueDate == null )
				return true;
			long firstJob = process.getFirstDueDate().getTime() - Calendar.getInstance().getTimeInMillis();
			return( firstJob < slack );
		}
		
		@Override
		public boolean addInput(IShip input) {
			if( input == null )
				return false;
			if( !allowNextShip())
				return false;
			
			if( !buffer.containsKey(input)){
				buffer.put(input, input.getNrOfContainers());
				mc.sendMessage( Parties.BARGE, "transport");
				mc.sendMessage( Parties.TRAIN, "transport");
				mc.sendMessage( Parties.TRUCK, "transport");
			}
			return super.addInput(input);
		}
		
		@Override
		public boolean removeInput(IShip input) {
			buffer.remove(input);
			return super.removeInput(input);
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
			remaining -=1;
			buffer.replace(ship, remaining );
			if( remaining == 0 )
				removeInput(ship );
			String tag = ship.getName() + ": " + remaining;
			Date date = Calendar.getInstance().getTime();
			return new Container( tag, ship.getTimeStamp(), date, date );
		}

		@Override
		protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
			float stress = getRemainingCapacity()/getCapacity();
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