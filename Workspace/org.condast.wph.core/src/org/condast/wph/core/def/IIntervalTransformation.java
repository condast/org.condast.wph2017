package org.condast.wph.core.def;

import org.condast.symbiotic.core.def.ITransformation;

public interface IIntervalTransformation<I,O extends Object> extends ITransformation<I,O>{

	public void next( int interval );
}
