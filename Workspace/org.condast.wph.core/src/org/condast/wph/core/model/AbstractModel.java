package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public abstract class AbstractModel<E extends Enum<E>> implements IModel<E>{

	private String id;
	private LatLng lnglat;
	private E type;
	
	protected AbstractModel( String id, E type, LatLng lnglat ) {
		this.id = id;
		this.type = type;
		this.lnglat = lnglat;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public E getType() {
		return type;
	}

	@Override
	public LatLng getLnglat() {
		return lnglat;
	}

}
