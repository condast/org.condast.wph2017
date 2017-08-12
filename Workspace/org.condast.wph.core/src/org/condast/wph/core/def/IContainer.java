package org.condast.wph.core.def;

import java.util.Date;

public interface IContainer extends ITransport{

	/**
	 * Get the date when the cargo is due
	 * @return
	 */
	public Date getDueDate();
}
