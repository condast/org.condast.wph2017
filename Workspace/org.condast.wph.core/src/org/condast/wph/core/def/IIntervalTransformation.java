package org.condast.wph.core.def;

import org.condast.symbiotic.core.def.ITransformation;

public interface IIntervalTransformation<M, I,O extends Object> extends ITransformation<I,O>{

	public M getModel();
	
	public void next( long interval );
}
