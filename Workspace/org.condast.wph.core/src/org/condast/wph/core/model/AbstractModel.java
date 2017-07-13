package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public abstract class AbstractModel implements IModel{

	private String id;
	private LatLng lnglat;
	private ModelTypes type;
	
	protected AbstractModel( String id, ModelTypes type, LatLng lnglat ) {
		this.id = id;
		this.type = type;
		this.lnglat = lnglat;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ModelTypes getType() {
		return type;
	}

	@Override
	public LatLng getLnglat() {
		return lnglat;
	}

}
