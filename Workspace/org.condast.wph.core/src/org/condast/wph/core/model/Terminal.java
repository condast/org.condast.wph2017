package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;

public class Terminal extends AbstractModel {

	public Terminal(String id, LatLng lnglat) {
		super(id, ModelTypes.TERMINAL, lnglat);
	}

}
