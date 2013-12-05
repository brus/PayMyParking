package eu.execom.paymyparking.service;

import eu.execom.paymyparking.model.Settings;

public class SettingsParseModel {

	Settings settings = new Settings();  
	String lastTag;
	
	public Settings getSettings() {
		return settings;
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public String getLastTag() {
		return lastTag;
	}
	
	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
}
