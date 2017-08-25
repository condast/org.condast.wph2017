package org.condast.wph.core.design;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.Transformation;
import org.condast.symbiotic.core.transformer.AbstractTransformer;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.model.IntervalProcess;

public class MultiCapacityTransformation<I extends Object> extends Transformation<I,I>
implements IIntervalProcess<I,I>
{
	private static final int DEFAULT_BUFFER_SIZE = 10;
	private static final int DEFAULT_TRAVEL_TIME = 2;//hours
	
	private int travelTime;

	public MultiCapacityTransformation(String name) {
		this( name, DEFAULT_BUFFER_SIZE, DEFAULT_TRAVEL_TIME);
	}
	
	public MultiCapacityTransformation(String name, int capacity, int travelTime) {
		super(name );
		this.travelTime = travelTime;
		super.setTransformer( new TRProcessList() );
	}
	
	protected boolean addProcess( String id, int capacity, IBehaviour<I,Integer> behaviour ){
		TRProcessList trpl = new TRProcessList();
		trpl.addProcess( id, capacity, behaviour );
		return true;
	}

	@Override
	public void next(long time) {
		TRProcessList trpl = new TRProcessList();
		trpl.next(time);
	}
	
	private class TRProcessList extends AbstractTransformer <I, I>{

		private Map<IntervalProcess<I>, IBehaviour<I,Integer>> processes;

		protected TRProcessList() {
			super();
			this.processes = new HashMap<IntervalProcess<I>, IBehaviour<I,Integer>>();
		}

		protected void addProcess( String id, int capacity, IBehaviour<I, Integer> behaviour ){
			processes.put( new IntervalProcess<I>( id, capacity ), behaviour );
		}

		@SuppressWarnings("unchecked")
		protected void updateStress(Iterator<I> inputs, ISymbiot symbiot) {
			if( isEmpty()){
				symbiot.clearStress();
			}
			else{
				float stress = 0;
				symbiot.setStress( stress );
			}
		}

		public I transform( Iterator<I> inputs) {
			for( IntervalProcess<I> process: this.processes.keySet() ){
				updateStress(inputs, processes.get(process).getOwner());
			}
			if( inputs == null )
				return null;
			return inputs.next();
		}
		
		public void next(long time) {
			for( IntervalProcess<I> process: this.processes.keySet() ){
				process.next(time);
			}
			this.transform( super.getInputs().iterator());
		}
	}
}
