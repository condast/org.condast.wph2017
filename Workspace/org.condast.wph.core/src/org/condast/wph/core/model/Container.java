package org.condast.wph.core.model;

import java.util.Date;

import org.condast.commons.data.latlng.LatLng;
import org.condast.wph.core.def.IContainer;

public class Container implements IContainer {

	private String tag;
	private Date departure;
	private Date eta;
	private LatLng lnglat;
	private Date timeStamp;

	public Container( String tag, Date departure, Date eta, Date timeStamp) {
		this.tag = tag;
		this.departure = departure;
		this.eta = eta;
		this.timeStamp = timeStamp;
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

	@Override
	public Date getTimeStamp() {
		return timeStamp;
	}

}
