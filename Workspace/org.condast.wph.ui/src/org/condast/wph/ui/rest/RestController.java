package org.condast.wph.ui.rest;

import java.util.logging.Logger;

import org.condast.js.commons.controller.AbstractJavascriptController;
import org.eclipse.swt.browser.Browser;

public class RestController extends AbstractJavascriptController {

	public static final String S_INDEX_HTML = "/wph2017/index.html";
	public static final String S_INITIALISTED_ID = "RestInitialisedId";
	public static final String S_IS_INITIALISTED = "isInitialised";

	public enum Pages{
		INDEX;

		@Override
		public String toString() {
			String str = "/resources/" + name().toLowerCase(); 
			str = str.replaceAll("_", "-");
			return str + ".html";
		}

		public static String[] getItems(){
			String[] items = new String[ values().length ];
			for( int i=0; i< items.length; i++ ){
				items[i] = Pages.values()[i].name();
			}
			return items;
		}
	}

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	
	public RestController(Browser browser) {
		super( browser, S_INITIALISTED_ID, S_INDEX_HTML );
	}

	public void setBrowser( Pages page ) {
		super.setBrowser( RestController.class.getResourceAsStream( page.toString() ));
	}

	@Override
	protected void onLoadCompleted() {
		logger.info("COMPLETED");
	}

}
