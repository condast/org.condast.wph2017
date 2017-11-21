/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.condast.wph.core.xml;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.xml.AbstractXMLBuilder;
import org.condast.commons.xml.AbstractXmlHandler;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.lang.WPHLanguage;
import org.condast.wph.core.model.Model;
import org.xml.sax.Attributes;

public class XMLFactoryBuilder extends AbstractXMLBuilder<IModel<IModel.ModelTypes>, XMLFactoryBuilder.Selection> {

	public static String S_DEFAULT_FOLDER = "/model";
	public static String S_DEFAULT_DESIGN_FILE = "model.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/rdm-schema.xsd";
	
	public static enum Selection{
		MODELS,
		MODEL;

		@Override
		public String toString() {
			return WPHLanguage.getInstance().getString( super.toString() );
		}
	}

	public enum AttributeNames{
		ID,
		NAME,
		TYPE,
		LONGITUDE,
		LATITUDE,
		SCOPE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}

		public static boolean isAttribute( String value ){
			if( StringUtils.isEmpty( value ))
				return false;
			for( AttributeNames attr: values() ){
				if( attr.toString().equals( value ))
					return true;
			}
			return false;
		}
	}

	public XMLFactoryBuilder( ) {
		this( XMLFactoryBuilder.class );
	}

	public XMLFactoryBuilder( Class<?> clss ) {
		this( clss.getResource( S_DEFAULT_FOLDER + File.separator + S_DEFAULT_DESIGN_FILE) );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public XMLFactoryBuilder( URL url ) {
		super( new XMLHandler(), url);
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}

	@Override
	public IModel<IModel.ModelTypes>[] getUnits() {
		return getHandler().getUnits();
	}
	
	private static class XMLHandler extends AbstractXmlHandler<IModel<IModel.ModelTypes>,XMLFactoryBuilder.Selection>{
		
		public XMLHandler() {
			super( EnumSet.allOf( XMLFactoryBuilder.Selection.class));
		}

		@Override
		protected IModel<IModel.ModelTypes> parseNode( Selection node, Attributes attributes) {
			IModel<IModel.ModelTypes> model = null;
			String id = getAttribute( attributes, AttributeNames.ID );
			//String name = getAttribute( attributes, AttributeNames.NAME );
			String lat_str = getAttribute( attributes, AttributeNames.LATITUDE );
			String lng_str = getAttribute( attributes, AttributeNames.LONGITUDE );
			String type_str = getAttribute( attributes, AttributeNames.TYPE );
			switch( node ){
			case MODELS:
				break;
			case MODEL:
				float lng = Float.parseFloat( lng_str );
				float lat = Float.parseFloat( lat_str );				
				model = new Model( id, IModel.ModelTypes.valueOf( type_str ), new LatLng( lng, lat ));
				break;
			default:
				break;
			}
			return model;
		}

		@Override
		protected void completeNode(Enum<Selection> node) {
			// TODO Auto-generated method stub			
		}

		@Override
		protected void addValue(Enum<Selection> node, String value) {
			// TODO Auto-generated method stub		
		}
	
		@SuppressWarnings("unchecked")
		@Override
		public IModel<IModel.ModelTypes>[] getUnits() {
			Collection<IModel<IModel.ModelTypes>> results = super.getResults();
			return results.toArray( new IModel[ results.size() ]);
		}

		@Override
		public IModel<IModel.ModelTypes> getUnit(String id) {
			for( IModel<IModel.ModelTypes> model: super.getResults()){
				if( model.getId().equals(id))
					return model;
			}
			return null;
		}
	}
}