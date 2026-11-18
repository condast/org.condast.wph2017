package org.condast.wph.core.behaviour;

import java.util.Collection;
import java.util.Iterator;

import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ITransformer;

public class AbstractTransformer<I, O extends Object> implements ITransformer<I, O>{

	private IBehaviour behaviour;
	
	protected AbstractTransformer() {
	}
	
	protected AbstractTransformer(IBehaviour behaviour) {
		super();
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

	@Override
	public O transform(Iterator<I> inputs) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getInputSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
