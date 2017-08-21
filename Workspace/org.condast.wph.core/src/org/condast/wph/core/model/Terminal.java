package org.condast.wph.core.model;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.definition.IModel;

public class Terminal extends AbstractModel<IModel.ModelTypes> {

	public static final int DEFAULT_UNLOAD_TIME = 3;//minutes per container
	
	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}
	
	private int maxDocks;
	private int unloadTime;

	public Terminal( String id, LatLng lnglat, int maxDocks) {
		this( id, lnglat, maxDocks, DEFAULT_UNLOAD_TIME );
	}
	
	public Terminal( String id, LatLng lnglat, int maxDocks, int unloadTime) {
		super(id, ModelTypes.TERMINAL, lnglat);
		this.maxDocks = maxDocks;
		this.unloadTime = unloadTime;
	}

	public int getMaxDocks() {
		return maxDocks;
	}

	public int getUnloadTime() {
		return unloadTime;
	}
}