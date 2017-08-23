package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.transformer.LinkedTransformation;
import org.condast.symbiotic.core.transformer.AbstractBehavedTransformer;
import org.condast.symbiotic.core.transformer.FilteredTransformer;
import org.condast.wph.core.def.ICapacityProcess;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.model.IntervalProcess;

public class CapacityNeighbourhood<I extends Object> extends LinkedTransformation<I,I>
implements INeighbourhood<I,I>, IIntervalProcess<I,I>
{
	private static final int DEFAULT_BUFFER_SIZE = 10;
	
	private IntervalProcess<I> process;
	private long time;
	private int buffer;

	public CapacityNeighbourhood(String name, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,I> outNode) {
		this( name, DEFAULT_BUFFER_SIZE, behaviour, outNode );
	}
	
	@SuppressWarnings("unchecked")
	public CapacityNeighbourhood(String name, int buffer, IBehaviour<I,Integer> behaviour, ICapacityProcess<I,?> outNode) {
		super(name, (ITransformation<I, ?>) outNode);
		this.time = 0;
		this.buffer = buffer;
		this.process = new IntervalProcess<I>();
		super.setTransformer( new TRNeighbourhood(behaviour) );
	}

	@Override
	public boolean addInput(I input) {
		if( super.addInput(input))
			return true;
		
		if ( super.getInputSize() >= this.buffer )
			return false;
		return process.addInput(input);
	}

	@Override
	public void next(long time) {
		this.time = time;
		super.transform();
	}

	private class TRNeighbourhood extends AbstractBehavedTransformer<I, I, Integer>{

		protected TRNeighbourhood( IBehaviour<I, Integer> behaviour) {
			super(behaviour);
		}

		@Override
		protected void onUpdateStress(Iterator<I> inputs, ISymbiot symbiot) {
			if( isEmpty()){
				symbiot.clearStress();
			}
			else{
				float stress = NumberUtils.clip(1f, super.getInputSize() / buffer );
				symbiot.setStress( stress );
			}
		}

		@Override
		protected I onTransform(Iterator<I> inputs) {
			if( inputs == null )
				return null;
			if( super.getInputSize() < buffer ){
				I inp = inputs.next();
				removeInput( inp );
				return inp;
			}
			return process.transform(inputs);
		}
	}
}
