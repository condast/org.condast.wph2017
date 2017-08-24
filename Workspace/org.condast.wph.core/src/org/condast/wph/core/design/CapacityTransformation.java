package org.condast.wph.core.design;

import java.util.Date;
import java.util.Iterator;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.transformer.LinkedTransformation;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformerWrapper;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.model.IntervalProcess;

public class CapacityTransformation<I extends Object> extends LinkedTransformation<I,I>
implements IIntervalProcess<I,I>, ICapacityProcess<I,I>
{
	private static final int DEFAULT_BUFFER_SIZE = 10;
	private static final int DEFAULT_TRAVEL_TIME = 2;//hours
	
	private IntervalProcess<I> process;
	private int travelTime;

	public CapacityTransformation(String name, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,I> outNode) {
		this( name, DEFAULT_BUFFER_SIZE, DEFAULT_TRAVEL_TIME, behaviour, outNode );
	}
	
	public CapacityTransformation(String name, int capacity, int travelTime, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,?> outNode) {
		super(name, (ITransformation<I, ?>) outNode);
		this.travelTime = travelTime;
		this.process = new IntervalProcess<I>( name, capacity );
		super.setTransformer( new TRNeighbourhood(behaviour) );
	}
	
	private int getActiveJobs(){
		return this.process.getJobSize() + super.getInputSize();
	}

	@Override
	public boolean addInput(I input) {
		if( this.isFull() )
			return false;
		if ( this.getActiveJobs() >= this.process.getCapacity())
			return false;
		return process.addInput(input, IntervalProcess.getSimulatedTime( this.travelTime * IntervalProcess.TO_HOURS));
	}

	@Override
	public void next(long time) {
		process.next(time);
		I result = super.transform();
		if( result != null ){
			super.removeInput(result);
		}
	}
	
	@Override
	public Date getFirstDueJob() {
		return process.getFirstDueDate();
	}

	@Override
	public int getJobSize() {
		return process.getJobSize();
	}

	@Override
	public int getReaminingCapacity() {
		return Integer.MAX_VALUE - process.getJobSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isFull() {
		ICapacityProcess<I,I> outNode = (ICapacityProcess<I, I>) super.getOutputNode();
		int onWaterway = process.getInputs().size();
		if( outNode.getReaminingCapacity() < onWaterway )
			return true;
		return ( onWaterway >= process.getCapacity() );
	}

	@Override
	public int getCapacity() {
		return process.getCapacity();
	}

	private class TRNeighbourhood extends AbstractBehavedTransformerWrapper<I, I, Integer>{

		protected TRNeighbourhood( IBehaviour<I, Integer> behaviour) {
			super(process, behaviour);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onUpdateStress(Iterator<I> inputs, ISymbiot symbiot) {
			if( isEmpty()){
				symbiot.clearStress();
			}
			else{
				ICapacityProcess<I,I> outNode = (ICapacityProcess<I, I>)getOutputNode();
				float stress = NumberUtils.clip(1f, getActiveJobs()/( getCapacity() + outNode.getReaminingCapacity()));
				symbiot.setStress( stress );
			}
		}

		@Override
		protected I onTransform(Iterator<I> inputs, I selected) {
			if( inputs == null )
				return null;
			if( getActiveJobs() < process.getCapacity() ){
				removeInput( selected );
				return selected;
			}
			return selected;
		}
	}
}
