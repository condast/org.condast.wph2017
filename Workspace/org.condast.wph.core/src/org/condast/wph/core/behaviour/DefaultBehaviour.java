package org.condast.wph.core.behaviour;

import org.condast.symbiotic.core.Symbiot;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.IStressListener;
import org.condast.symbiotic.core.def.ISymbiot;

public class DefaultBehaviour extends Symbiot implements IBehaviour {

	private int value;
	
	
	public DefaultBehaviour(int someValue) {
		super( String.valueOf( someValue));
		this.value = someValue;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public ISymbiot getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInclusive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getWeightStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStress(double stress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWeights(ISymbiot symbiot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addStressListener(IStressListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int calculate(boolean equals) {
		// TODO Auto-generated method stub
		return 0;
	}

}
