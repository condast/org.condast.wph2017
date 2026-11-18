package org.condast.wph.core.behaviour;

import java.util.Collection;
import java.util.Iterator;

import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.ITransformer;
import org.condast.wph.core.def.IIntervalProcess;

public abstract class AbstractBehavedTransformer<I, O extends Object> implements ITransformer<I, O>{

	private IBehaviour behaviour;
	private IIntervalProcess<I,O> process;
	
	public AbstractBehavedTransformer(IBehaviour behaviour) {
		super();
		this.behaviour = behaviour;
	}

	
	public AbstractBehavedTransformer(IIntervalProcess<I,O> process, IBehaviour behaviour) {
		this.process = process;
		this.behaviour = behaviour;
	}

	public IBehaviour getBehaviour() {
		return behaviour;
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

	abstract protected O onTransform(Iterator<I> inputs);

	@Override
	public O transform(Iterator<I> inputs) {
		return this.onTransform(inputs);
	}

	abstract protected void onUpdateStress(Iterator<I> inputs, ISymbiot symbiot);
}
