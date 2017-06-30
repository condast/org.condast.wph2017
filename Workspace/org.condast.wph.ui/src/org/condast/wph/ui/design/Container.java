package org.condast.wph.ui.design;

import java.util.Date;

import org.condast.commons.lnglat.LngLat;

public class Container {

	private String tag;
	private Date departure;
	private Date eta;
	private LngLat lnglat;

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

	public LngLat getLngLat() {
		return lnglat;
	}

	public void setLnglat(LngLat lnglat) {
		this.lnglat = lnglat;
	}
}
