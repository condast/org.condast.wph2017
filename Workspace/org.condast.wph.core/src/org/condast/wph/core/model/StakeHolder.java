package org.condast.wph.core.model;

import org.condast.commons.data.latlng.LatLng;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.definition.IModel.ModelTypes;

public class StakeHolder<I,O extends Object> implements IStakeHolder<I, O> {

	private IIntervalProcess<I, O> process;
	private ModelTypes model; 
	private LatLng latlng;
	
	public StakeHolder( IIntervalProcess<I, O> process, ModelTypes model, LatLng latlng ) {
		this.process = process;
		this.model = model;
		this.latlng = latlng;
	}

	@Override
	public int compareTo(IStakeHolder<I, O> arg0) {
		int compare = (int)( this.latlng.getLongitude() - arg0.getLnglat().getLongitude())*1000; 
		return compare;
	}

	@Override
	public String getName() {
		return process.getName();
	}

	@Override
	public ModelTypes getModel() {
		return model;
	}

	@Override
	public LatLng getLnglat() {
		return latlng;
	}

	@Override
	public void next(long time ) {
		process.next(time);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITransformation<I, O> getTransformation() {
		ITransformation<I, O> trf = (ITransformation<I, O>) process;
		return trf;
	}

}
