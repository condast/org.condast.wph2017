package org.condast.wph.core.symbiot;

import java.util.Collection;

import org.condast.symbiotic.core.AbstractModelTransformation;
import org.condast.symbiotic.core.AbstractSymbiot;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.symbiotic.def.ITransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.model.Terminal;

public class TerminalBot extends AbstractSymbiot<Terminal,IShip, Boolean> {

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}
	
	private Terminal terminal;
	
	public TerminalBot( Terminal terminal ) {
		super(terminal.getId(), 5);
		this.terminal = terminal;
	}
	
	@Override
	protected ITransformation<IShip, Boolean> createTransformation() {
		return new Transformation( this, terminal );
	}

	public boolean dockShip( IShip ship ){
		return super.getTransformation().addInput( ship );
	}
		
	public void update( int interval ){
		Transformation transformation = (Transformation) super.getTransformation();
		transformation.update(interval);
	}

	private static class Transformation extends AbstractModelTransformation<IShip, Boolean, Terminal>{

		private int interval;
		private Terminal terminal;
		private ISymbiot<IShip, Boolean> symbiot;

		protected Transformation(ISymbiot<IShip, Boolean> symbiot, Terminal terminal ) {
			super(symbiot.getName(), terminal);
			this.symbiot = symbiot;
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
			if( this.terminal.isAvailable())
				symbiot.clearStress();
			else
				symbiot.increaseStress();
			return retval;
		}

	}

	@Override
	protected float onChangeLevel(ISymbiot<?, ?> symbiot, float current) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}