package org.condast.wph.core.design;

import java.util.Collection;

import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.transformation.AbstractBehavedTransformation;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.def.IIntervalTransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Terminal;

public class TTerminal extends AbstractBehavedTransformation<IShip, Boolean, Terminal, Integer> 
implements IIntervalTransformation<IShip, Boolean>{

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}

	private int interval;
	private Terminal terminal;

	public TTerminal( IBehaviour<IShip,Integer> behaviour, Terminal terminal ) {
		super( ModelTypes.TERMINAL.toString(), behaviour, terminal);
		this.terminal = terminal;
	}

	@Override
	public boolean addInput(IShip ship) {
		super.addInput(ship);
		return this.terminal.addJob( ship.getName(), ship.getNrOfContainers() * terminal.getUnloadTime() );
	}

	public void update( int interval ){
		this.interval = interval;
		this.transform();
	}

	@Override
	protected Boolean onTransform(Collection<IShip> inputs) {
		boolean retval = this.terminal.update( interval );
		ISymbiot symbiot = super.getBehaviour().getOwner();
		if( this.terminal.isAvailable())
			symbiot.clearStress();
		else
			symbiot.increaseStress();
		return retval;
	}

	@Override
	public void next(int interval) {
		// TODO Auto-generated method stub
		
	}
}