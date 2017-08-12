package org.condast.wph.core.def;

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
}
