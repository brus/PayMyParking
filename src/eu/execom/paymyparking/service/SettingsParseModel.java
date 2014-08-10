package eu.execom.paymyparking.service;

import eu.execom.paymyparking.model.City;

public class SettingsParseModel {

	private City selectedCity;
	private String defaultLicensePlate; 
	String lastTag;
	
	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	public String getDefaultLicensePlate() {
		return defaultLicensePlate;
	}

	public void setDefaultLicensePlate(String defaultLicensePlate) {
		this.defaultLicensePlate = defaultLicensePlate;
	}

	public String getLastTag() {
		return lastTag;
	}
	
	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
}
