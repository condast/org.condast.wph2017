package org.condast.wph.ui.design;

import java.util.Date;

import org.condast.commons.latlng.LatLng;

public class Container {

	private String tag;
	private Date departure;
	private Date eta;
	private LatLng lnglat;

	public Container( String tag, Date departure, Date eta) {
		this.tag = tag;
		this.departure = departure;
		this.eta = eta;
	}

	public String getTag() {
		return tag;
	}

	public Date getDeparture() {
		return departure;
	}

	public Date getETA() {
		return eta;
	}

	public LatLng getLngLat() {
		return lnglat;
	}

	public void setLnglat(LatLng lnglat) {
		this.lnglat = lnglat;
	}
}
