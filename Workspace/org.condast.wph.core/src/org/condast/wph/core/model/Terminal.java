package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public class Terminal extends AbstractModel<IModel.ModelTypes> {

	public Terminal(String id, LatLng lnglat) {
		super(id, ModelTypes.TERMINAL, lnglat);
	}

}
