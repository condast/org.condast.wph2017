package org.condast.wph.core.model;

import java.util.Date;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.def.IEventLocation;
import org.condast.wph.core.def.IShip;

public class Ship implements IShip {

	private Date timeStamp;
	private int length;
	private int containerSize;
	private int containers;
	private float speed;
	
	public Ship() {
		this( 0 );
	}

	public Ship( long time ) {
		timeStamp = IntervalProcess.getSimulatedTime(time);
		this.length = 20 + ( int )( 300* Math.random());
		this.containerSize = 20 + ( int )( this.length * Math.random());
		this.containers = 20 + ( int )(( this.containerSize - 20)* Math.random());
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int getMaxContainerSize() {
		return this.containerSize;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public IEventLocation getATD() {
		return null;
	}

	@Override
	public IEventLocation getETA() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public LatLng getLocation() {
		return null;
	}

	@Override
	public VesselType getVesselType() {
		return null;
	}

	@Override
	public int getNrOfContainers() {
		return this.containers;
	}

	@Override
	public float getDraught() {
		return 0;
	}

	@Override
	public long getAverageTransportTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getTimeStamp() {
		return this.timeStamp;
	}

	
}
