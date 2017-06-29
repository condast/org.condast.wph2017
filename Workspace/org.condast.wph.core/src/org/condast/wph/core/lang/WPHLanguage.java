package org.condast.wph.core.lang;

import org.condast.commons.i18n.Language;

public class WPHLanguage extends Language {

	private static final String S_VG_LANGUAGE = "VGLanguage";
	
	private static WPHLanguage language = new WPHLanguage();
	
	public enum SupportedText{
		GUEST_MEMBERS;
	}
	
	private WPHLanguage() {
		super( S_VG_LANGUAGE, "NL", "nl");
	}
	
	public static WPHLanguage getInstance(){
		return language;
	}	
}
