package org.condast.wph.core.model;

import java.util.Collection;

import org.condast.commons.latlng.LatLng;
import org.condast.symbiotic.core.AbstractSymbiot;
import org.condast.symbiotic.core.AbstractTransformation;
import org.condast.symbiotic.def.ITransformation;
import org.condast.wph.core.definition.IModel;

public class Terminal extends AbstractModel<IModel.ModelTypes> {

	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}
	
	private ITransformation<Strategies, Boolean> transformation;
	
	public Terminal(String id, LatLng lnglat) {
		super(id, ModelTypes.TERMINAL, lnglat);
		transformation = new Transformation( id );
	}

	private class Transformation extends AbstractTransformation<Strategies, Boolean, Terminal>{

		protected Transformation(String name) {
			super(name);
		}

		@Override
		protected Boolean onTransform(Collection<Strategies> inputs) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private class Symbiot extends AbstractSymbiot<Strategies, Boolean, IModel<IModel.ModelTypes>> {

		public Symbiot(ITransformation<Strategies, Boolean> transformation, int maxStrategy) {
			super(transformation, getId(), maxStrategy);
		}

		@Override
		protected void onSetStrategy(int strategy) {
			
		}
	}

}
