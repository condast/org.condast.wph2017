package org.condast.wph.core.def;

import java.util.Date;

public interface ICarrier extends ITransport {

	public enum Modality{
		AIR,
		PIPE,
		WATERWAY,
		RAIL,
		INFORMATION
	}

	/**
	 * Length of the carrier in m.
	 * @return
	 */
	public int getLength();


	/**
	 * The number of containers currently being transported
	 * @return
	 */
	public int getNrOfContainers();

	/**
	 * The maximum number of containers allowed
	 * @return
	 */
	public int getMaxContainerSize();
	
	/**
	 * Get the time that the ship is created
	 * @return
	 */
	public Date getTimeStamp();

}
