package org.condast.wph.core.model;

import org.condast.commons.lnglat.LngLat;

public class Terminal extends AbstractModel {

	public Terminal(String id, LngLat lnglat) {
		super(id, ModelTypes.TERMINAL, lnglat);
	}

}
