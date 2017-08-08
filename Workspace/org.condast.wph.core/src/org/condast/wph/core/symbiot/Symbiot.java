package org.condast.wph.core.symbiot;

import org.condast.symbiotic.core.AbstractSymbiot;
import org.condast.wph.core.definition.IModel;

public class Symbiot extends AbstractSymbiot<IModel<IModel.ModelTypes>> {

	public Symbiot(IModel<IModel.ModelTypes> model, int maxStrategy) {
		super(model, model.getId(), maxStrategy);
	}

	@Override
	protected void onSetStrategy(int strategy) {
		// TODO Auto-generated method stub

	}

}
