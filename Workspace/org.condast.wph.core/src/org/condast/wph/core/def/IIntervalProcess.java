package org.condast.wph.core.def;

public interface IIntervalProcess<I,O extends Object>{

	public void next( long interval );
}
