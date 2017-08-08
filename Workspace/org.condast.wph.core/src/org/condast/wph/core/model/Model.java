package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public class Model extends AbstractModel<IModel.ModelTypes> {

	public Model(String id, ModelTypes type, LatLng lnglat) {
		super(id, type, lnglat);
	}

}
