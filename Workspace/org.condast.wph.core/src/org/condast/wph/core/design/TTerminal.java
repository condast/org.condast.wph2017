package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.transformation.AbstractModelTransformer;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Terminal;

public class TTerminal extends AbstractModelTransformer<Terminal, IShip, Boolean, Integer>{

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}

	private int interval;
	private Terminal terminal;

	public TTerminal( Terminal terminal, IBehaviour<IShip,Integer> behaviour ) {
		super( ModelTypes.TERMINAL.toString(), terminal, behaviour);
		this.terminal = terminal;
	}

	@Override
	public boolean addInput(IShip ship) {
		super.addInput(ship);
		return this.terminal.addJob( ship.getName(), ship.getNrOfContainers() * terminal.getUnloadTime() );
	}

	@Override
	protected Boolean onTransform(Iterator<IShip> inputs) {
		boolean retval = this.terminal.update( interval );
		return retval;
	}

	@Override
	protected void onUpdateStress(Iterator<IShip> inputs, ISymbiot symbiot) {
		if( this.terminal.isAvailable())
			symbiot.clearStress();
		else
			symbiot.increaseStress();
	}
}