package org.condast.wph.rest.config;

import java.util.Properties;

import org.condast.commons.project.ProjectFolderUtils;
import org.condast.commons.strings.StringStyler;

public class Config {

	public static final String S_PRODUCTION_PORT = "8080";
	public static final String S_DEVELOPMENT_PORT = "8081";

	public static final String S_CONDAST_URL = "http://www.condast.com";
	public static final String S_REST_BASE = "/rest/";

	public enum Fields{
		ENVIRONMENT,
		PORT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public enum Environment{
		DEBUG,
		PRODUCTION;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private Properties props;
	
	private static Config config = new Config();
	
	private Config() {
		props = ProjectFolderUtils.getConfiguration();
		if( props.isEmpty() ){
			props.setProperty( Fields.ENVIRONMENT.name(), Environment.PRODUCTION.toString() );
			props.setProperty( Fields.PORT.name(), S_PRODUCTION_PORT );
		}
	}
	
	public static Config getInstance(){
		return config;
	}
	
	public boolean isProduction(){
		String result = props.getProperty( Fields.ENVIRONMENT.name() );
		return ( Environment.PRODUCTION.toString().equals( result ));
	}

	public String getPort(){
		String result = props.getProperty( Fields.PORT.name() );
		return result;
	}

	public String getRestURL(){
		String result = S_CONDAST_URL + ":" + getPort() + S_REST_BASE;
		return result;
	}

}
