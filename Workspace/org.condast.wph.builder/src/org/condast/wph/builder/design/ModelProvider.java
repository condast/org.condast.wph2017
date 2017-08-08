package org.condast.wph.builder.design;

import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.xml.XMLFactoryBuilder;

public class ModelProvider {

	private static ModelProvider provider = new ModelProvider();
	
	private XMLFactoryBuilder builder;
	
	private ModelProvider(){
		builder = new XMLFactoryBuilder( this.getClass() ); 
		builder.build();		
	}

	public static ModelProvider getInstance(){
		return provider;
	}
	
	public IModel<IModel.ModelTypes>[] getModels(){
		return builder.getUnits();
	}

	public IModel<IModel.ModelTypes> getModel( IModel.ModelTypes type ){
		for( IModel<IModel.ModelTypes> model: builder.getUnits()){
			if( model.getType().equals( type ))
				return model;
		}
		return null;
	}

}
