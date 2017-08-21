package org.condast.wph.core.def;

public interface ICapacityProcess<I, O extends Object>{

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
