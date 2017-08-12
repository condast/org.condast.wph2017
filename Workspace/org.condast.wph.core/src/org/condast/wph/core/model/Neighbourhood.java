package org.condast.wph.core.model;

import org.condast.wph.core.design.TimedNode;

public class Neighbourhood extends TimedNode {

	public enum Direction{
		ARRIVAL,
		DEPARTURE
	}
	
	private Direction direction;
	
	public Neighbourhood( long time) {
		super(time);
	}

	public Neighbourhood(String id, long time) {
		super(id, time);
	}

	protected Direction getDirection() {
		return direction;
	}

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}
}
