package org.condast.wph.core.model;

import java.util.Date;

import org.condast.wph.core.def.ICarrier;

public abstract class AbstractCarrier implements ICarrier {

	private Date timeStamp;
	private int length;
	private int containerSize;
	private int containers;
	private float speed;
	private String name;
	
	protected AbstractCarrier( String name ) {
		this( name, 0 );
	}

	protected AbstractCarrier( String name, long time ) {
		this.name = name;
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
	public String getName() {
		return name;
	}

	@Override
	public int getNrOfContainers() {
		return this.containers;
	}

	@Override
	public Date getTimeStamp() {
		return this.timeStamp;
	}
}
