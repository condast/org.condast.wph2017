package org.condast.wph.core.design;

import org.condast.symbiotic.core.def.INeighbourhood;
import org.condast.symbiotic.core.transformation.AbstractLinkedTransformation;
import org.condast.wph.core.def.ICapacityTransformation;

public class CapacityNeighbourhood<O, I extends Object> extends AbstractLinkedTransformation<O, I>
implements INeighbourhood<I,O>
{

	public CapacityNeighbourhood(String name, ICapacityTransformation<I,O> outNode) {
		super(name, outNode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addInput( O input) {
		ICapacityTransformation<I,O> outNode = (ICapacityTransformation<I, O>) super.getOutNode();
		if( outNode.isFull()){
			return false;
		}else{
			return super.addInput(input);
		}
	}

	@Override
	protected void onOutputBlocked(I output) {
		// UPDATE STRESS OF BLOCKING WATERWAY
		
	}
	
}
