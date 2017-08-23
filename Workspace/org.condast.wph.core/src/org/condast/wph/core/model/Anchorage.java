package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public class Anchorage extends AbstractModel<IModel.ModelTypes> {

	public static final float TO_MINUTES = 60000;//scale from msec to hours
	public static final float TO_HOURS = 60* TO_MINUTES;//scale from msec to hours

	public static final int MAX_WAITING_TIME = 12;//hours

	private int maxWaitingTime;//minutes
	
	public Anchorage( String id, LatLng lnglat) {
		this( id, lnglat, MAX_WAITING_TIME );
	}
	
	/**
	 * 
	 * @param id
	 * @param lnglat
	 * @param maxWaitingTime (hors)
	 */
	public Anchorage( String id, LatLng lnglat, int maxWaitingTime) {
		super(id, ModelTypes.TERMINAL, lnglat);
		this.maxWaitingTime = maxWaitingTime;
	}

	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}
}