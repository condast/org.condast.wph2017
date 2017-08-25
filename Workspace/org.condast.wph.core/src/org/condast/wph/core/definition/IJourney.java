package org.condast.wph.core.definition;

import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.wph.core.def.IContainer;

public interface IJourney {

	IContainer getContainer();

	ISymbiot next();

	boolean isCompleted();

}