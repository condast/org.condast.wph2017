package org.condast.wph.core.design;

import java.util.Iterator;

import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.transformation.AbstractModelTransformer;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Anchorage;

public class TAnchorage extends AbstractModelTransformer<Anchorage, IShip, Boolean, Integer>{

	private Anchorage anchorage;

	public TAnchorage( Anchorage anchorage, IBehaviour<IShip, Integer> behaviour ) {
		super( ModelTypes.ANCHORAGE.toString(), anchorage, behaviour );
		this.anchorage = anchorage;
	}

	@Override
	public boolean addInput( IShip ship ) {
		boolean retval = super.addInput(ship);
		this.anchorage.addShip( ship );
		return retval;
	}

	@Override
	public boolean removeInput(IShip input) {
		boolean retval = super.removeInput( input );
		this.anchorage.removeShip( input );
		return retval;
	}

	@Override
	protected Boolean onTransform( Iterator<IShip> inputs) {
		return !this.anchorage.isEmpty();
	}

	@Override
	protected void onUpdateStress( Iterator<IShip> inputs, ISymbiot symbiot) {
		if( !this.anchorage.isEmpty())
			symbiot.clearStress();
		else
			symbiot.increaseStress();
	}
}