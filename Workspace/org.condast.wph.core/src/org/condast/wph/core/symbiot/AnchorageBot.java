package org.condast.wph.core.symbiot;

import java.util.Collection;

import org.condast.symbiotic.core.AbstractModelTransformation;
import org.condast.symbiotic.core.AbstractSymbiot;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.symbiotic.def.ITransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.model.Anchorage;

public class AnchorageBot extends AbstractSymbiot<Anchorage,IShip, Boolean> {

	private Anchorage anchorage;
	
	public AnchorageBot( Anchorage anchorage ) {
		super(anchorage.getId(), 5);
		this.anchorage = anchorage;
	}
	
	@Override
	protected ITransformation<IShip, Boolean> createTransformation() {
		return new Transformation( this, anchorage );
	}

	public boolean addShip( IShip ship ){
		return super.getTransformation().addInput( ship );
	}
		
	public void update( int interval ){
		super.getTransformation().transform();
	}

	private static class Transformation extends AbstractModelTransformation<IShip, Boolean, Anchorage>{

		private Anchorage anchorage;
		private ISymbiot<IShip, Boolean> symbiot;

		protected Transformation(ISymbiot<IShip, Boolean> symbiot, Anchorage anchorage ) {
			super(symbiot.getName(), anchorage);
			this.symbiot = symbiot;
			this.anchorage = anchorage;
		}

		@Override
		public boolean addInput(IShip ship) {
			super.addInput(ship);
			this.anchorage.addShip( ship );
			return true;
		}

		@Override
		protected Boolean onTransform(Collection<IShip> inputs) {
			boolean retval = !this.anchorage.isEmpty();
			if( retval)
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