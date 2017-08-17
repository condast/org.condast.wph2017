package org.condast.wph.core.definition;

import org.condast.symbiotic.core.def.ISymbiot;

public interface IJourney {

	IContainer getContainer();

	ISymbiot next();

	boolean isCompleted();

}