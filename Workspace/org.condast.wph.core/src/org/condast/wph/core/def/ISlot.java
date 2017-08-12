package org.condast.wph.core.def;

/**
 * A slot in a terminal. Contains a length and a boolean
 * to show that it is free
 * @author Kees
 *
 */
public interface ISlot extends IEventLocation{

	public int getLength();
	
	public boolean isFree();

	void block();

	void unblock();
}
