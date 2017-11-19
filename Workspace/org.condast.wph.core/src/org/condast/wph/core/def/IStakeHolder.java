package org.condast.wph.core.def;

import org.condast.commons.data.latlng.LatLng;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.wph.core.definition.IModel.ModelTypes;

public interface IStakeHolder<I,O extends Object> extends IIntervalProcess<I,O>, Comparable<IStakeHolder<I,O>>{

	public String getName();
	
	public ModelTypes getModel();
	
	public LatLng getLnglat();
	
	public ITransformation<I,O> getTransformation();
	
	public void next( long interval );
}
