package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.transformer.LinkedTransformation;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformerWrapper;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.model.IntervalProcess;

public class CapacityNeighbourhood<I extends Object> extends LinkedTransformation<I,I>
implements INeighbourhood<I,I>, IIntervalProcess<I,I>
{
	private static final int DEFAULT_BUFFER_SIZE = 10;
	private static final int DEFAULT_TRAVEL_TIME = 2;//hours
	
	private IntervalProcess<I> process;
	private int travelTime;

	public CapacityNeighbourhood(String name, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,I> outNode) {
		this( name, DEFAULT_BUFFER_SIZE, DEFAULT_TRAVEL_TIME, behaviour, outNode );
	}
	
	@SuppressWarnings("unchecked")
	public CapacityNeighbourhood(String name, int buffer, int travelTime, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,?> outNode) {
		super(name, (ITransformation<I, ?>) outNode);
		this.travelTime = travelTime;
		this.process = new IntervalProcess<I>( buffer );
		super.setTransformer( new TRNeighbourhood(behaviour) );
	}
	
	private int getActiveJobs(){
		return this.process.getInputs().size() + super.getInputSize();
	}

	@Override
	public boolean addInput(I input) {
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

	private class TRNeighbourhood extends AbstractBehavedTransformerWrapper<I, I, Integer>{

		protected TRNeighbourhood( IBehaviour<I, Integer> behaviour) {
			super(process, behaviour);
		}

		@Override
		protected void onUpdateStress(Iterator<I> inputs, ISymbiot symbiot) {
			if( isEmpty()){
				symbiot.clearStress();
			}
			else{
				float stress = NumberUtils.clip(1f, getInputSize() / process.getCapacity() );
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
