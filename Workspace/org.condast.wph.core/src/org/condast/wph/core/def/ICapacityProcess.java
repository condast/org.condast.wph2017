package org.condast.wph.core.def;

import java.util.Date;

import org.condast.symbiotic.core.def.ITransformation;

public interface ICapacityProcess<I, O extends Object> extends ITransformation<I,O>{
 
	/**
	 * Get the date of the first job that is due
	 * @return
	 */
	public Date getFirstDueJob();
	
	/**
	 * Returns true if the capacity has been rewached 
	 * @return
	 */
	public boolean isFull();
	
	/**
	 * Get the amount of jobs currently active
	 * @return
	 */
	public int getJobSize();
	
	/**
	 * Get the remaining capacity of the process
	 */
	public int getReaminingCapacity();
	
	/**
	 * Get the capacity of the transformation
	 * @return
	 */
	public int getCapacity();
}
