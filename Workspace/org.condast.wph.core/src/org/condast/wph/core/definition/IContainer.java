package org.condast.wph.core.definition;

import java.util.Date;

import org.condast.commons.latlng.LatLng;

public interface IContainer {

	String getTag();

	Date getDeparture();

	Date getETA();

	LatLng getLngLat();

	void setLnglat(LatLng lnglat);

}