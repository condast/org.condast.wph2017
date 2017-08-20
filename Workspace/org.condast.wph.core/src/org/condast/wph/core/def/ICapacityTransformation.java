package org.condast.wph.core.def;

import org.condast.symbiotic.core.def.ITransformation;

public interface ICapacityTransformation<I, O extends Object> extends ITransformation<I, O> {

	/**
	 * Returns true if the capacity has been rewached 
	 * @return
	 */
	public boolean isFull();
	
	/**
	 * Get the remaining capacity of the transformation
	 * @return
	 */
	public int getCapacity();
}
