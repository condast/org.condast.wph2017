package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.symbiotic.core.transformation.AbstractTransformer;
import org.condast.wph.core.def.ICapacityTransformation;

public class CapacityNeighbourhood<O, I extends Object> extends AbstractLinkedTransformation<I,O>
implements INeighbourhood<O,I>
{

	public CapacityNeighbourhood(String name, ICapacityTransformation<O,I> outNode) {
		super(name, outNode);
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
			ICapacityTransformation<I,O> outNode = (ICapacityTransformation<I, O>) getOutNode();
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
