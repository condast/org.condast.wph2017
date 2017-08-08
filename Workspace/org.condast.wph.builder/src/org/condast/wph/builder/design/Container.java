package org.condast.wph.builder.design;

import java.util.Date;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IContainer;

public class Container implements IContainer {

	private String tag;
	private Date departure;
	private Date eta;
	private LatLng lnglat;

	public Container( String tag, Date departure, Date eta) {
		this.tag = tag;
		this.departure = departure;
		this.eta = eta;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public Date getDeparture() {
		return departure;
	}

	@Override
	public Date getETA() {
		return eta;
	}

	@Override
	public LatLng getLngLat() {
		return lnglat;
	}

	@Override
	public void setLnglat(LatLng lnglat) {
		this.lnglat = lnglat;
	}
}
