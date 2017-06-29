package org.condast.wph.core.symbiot;

import org.condast.symbiotic.core.AbstractSymbiot;
import org.condast.wph.core.definition.IModel;

public class Symbiot extends AbstractSymbiot {

	private IModel model;
	
	public Symbiot(IModel model, int maxStrategy) {
		super(model.getId(), maxStrategy);
		this.model = model;
	}

	@Override
	protected void onSetStrategy(int strategy) {
		// TODO Auto-generated method stub

	}

}
