package org.condast.wph.core.def;

import java.util.Date;

public interface IShip extends ICarrier{
	
	public enum VesselType{
		CARGO,
		CONTAINER,
		TANKER,
		COASTER
	}
	
	/**
	 * Get the type of vessel
	 * @return
	 */
	public VesselType getVesselType();
	
	/**
	 * Get the number of containers
	 */
	public int getNrOfContainers();

	/**
	 * Depth of a loaded vessel
	 * @return
	 */
	public float getDraught();
	
	/**
	 * The average time that the ship needs to reach a terminal
	 * Temporary measure..eventually will be dependent on the warterway
	 */
	public long getAverageTransportTime();
	
	/**
	 * Get the time that the ship is created
	 * @return
	 */
	public Date getTimeStamp();

}
