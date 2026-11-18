package org.condast.wph.core.behaviour;

import java.util.Collection;
import java.util.Iterator;

import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.wph.core.def.IIntervalProcess;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.model.IntervalProcess;

public abstract class AbstractProcessWrapper<I, O extends Object> extends AbstractBehavedTransformer<I,O> implements IIntervalProcess<I,O> {

	protected AbstractProcessWrapper(IBehaviour behaviour) {
		super( behaviour);
	}

	protected AbstractProcessWrapper(IIntervalProcess<I, I> process) {
		super( null );
	}
	
	@Override
	public void clearInputs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addInput(I input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeInput(I input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<I> getInputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public O transform(Iterator<I> inputs) {
		// TODO Auto-generated method stub
		return null;
	}

	public IntervalProcess<IShip> getTransformer() {
		// TODO Auto-generated method stub
		return null;
	}
}
