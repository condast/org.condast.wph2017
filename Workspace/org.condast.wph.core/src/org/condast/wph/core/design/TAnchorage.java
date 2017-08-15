package org.condast.wph.core.design;

import java.util.Collection;

import org.condast.symbiotic.core.IBehaviour;
import org.condast.symbiotic.core.transformation.AbstractBehavedTransformation;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.def.IIntervalTransformation;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Anchorage;
import org.condast.wph.core.model.Ship;

public class TAnchorage extends AbstractBehavedTransformation<IShip, Boolean, Anchorage, Integer> implements IIntervalTransformation<IShip, Boolean>{

	private Anchorage anchorage;

	public TAnchorage( IBehaviour<IShip, Integer> behaviour, Anchorage anchorage ) {
		super(ModelTypes.ANCHORAGE.toString(), behaviour, anchorage );
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
		ISymbiot symbiot = super.getBehaviour().getOwner();
		if( retval)
			symbiot.clearStress();
		else
			symbiot.increaseStress();
		return retval;
	}

	@Override
	public void next(int interval) {
		super.addInput( new Ship( ));
		super.transform();
	}
}