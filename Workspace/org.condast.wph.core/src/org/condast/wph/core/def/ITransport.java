package org.condast.wph.core.def;

public interface ITransport extends ILocation{
		
	/**
	 * The current speed (knots)
	 * @return
	 */
	public float getSpeed();

	/**
	 * Get the actual time of departure
	 * @return
	 */
	public IEventLocation getATD();

	/**
	 * Get the estimated time of Arrival
	 * @return
	 */
	public IEventLocation getETA();
}
