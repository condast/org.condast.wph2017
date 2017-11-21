package org.condast.wph.core.model;

import org.condast.commons.data.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public class Modality extends AbstractModel<IModel.ModelTypes> {

	public Modality(String id, IModel.ModelTypes type, LatLng lnglat) {
		super(id, type, lnglat);
	}

}
