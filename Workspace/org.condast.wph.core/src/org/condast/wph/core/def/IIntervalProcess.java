package org.condast.wph.core.def;

public interface IIntervalProcess<I,O extends Object>{

	public String getName();
	
	/**
	 * Perform an operation after the given time
	 * @param interval
	 */
	public void next( long time );
}
