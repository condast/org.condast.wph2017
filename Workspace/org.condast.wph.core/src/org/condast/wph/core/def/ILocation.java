package org.condast.wph.core.def;

import org.condast.commons.data.latlng.LatLng;

public interface ILocation {
	
	/**
	 * Get the name of the location
	 * @return
	 */
	public String getName();
	
	/**
	 * The location of the event
	 * @return
	 */
	public LatLng getLocation();
	}
