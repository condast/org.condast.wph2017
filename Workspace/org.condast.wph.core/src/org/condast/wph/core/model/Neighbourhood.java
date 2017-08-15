package org.condast.wph.core.model;

import org.condast.symbiotic.core.utils.TimedNode;

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
		super( time);
	}

	protected Direction getDirection() {
		return direction;
	}

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}
}
