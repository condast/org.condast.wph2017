package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.symbiotic.core.transformation.AbstractTransformer;
import org.condast.wph.core.def.ICapacityProcess;

public class CapacityNeighbourhood<O, I extends Object> extends AbstractLinkedTransformation<I,O>
implements INeighbourhood<O,I>
{

	@SuppressWarnings("unchecked")
	public CapacityNeighbourhood(String name, ICapacityProcess<O,I> outNode) {
		super(name, (ITransformation<O, ?>) outNode);
		this.setTransformer( new CapacityTransformer());
	}


	@Override
	protected void onOutputBlocked(O output) {
		// UPDATE STRESS OF BLOCKING WATERWAY
		
	}
	
	private class CapacityTransformer extends AbstractTransformer<I,O>{

		@SuppressWarnings("unchecked")
		@Override
		public boolean addInput( I input) {
			ICapacityProcess<I,O> outNode = (ICapacityProcess<I, O>) getOutNode();
			if( outNode.isFull()){
				return false;
			}else{
				return super.addInput(input);
			}
		}
		
		@Override
		public O transform(Iterator<I> inputs) {
			// TODO Auto-generated method stub
			return null;
		}		
	}	
}
