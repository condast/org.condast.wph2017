package org.condast.wph.core.definition;

import org.condast.symbiotic.def.ISymbiot;

public interface IJourney {

	IContainer getContainer();

	ISymbiot<IModel<IModel.ModelTypes>> next();

	boolean isCompleted();

}