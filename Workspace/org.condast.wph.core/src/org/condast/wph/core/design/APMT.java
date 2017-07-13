package org.condast.wph.core.design;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.model.Terminal;

public class APMT extends Terminal {

	public static final String S_ID = "APM-T Terminals";

	public static final float LONGITUDE = 51.936914f;
	public static final float LATITUDE = 4.055672f;

	public APMT(LatLng lnglat) {
		super( S_ID, lnglat);
	}
}
