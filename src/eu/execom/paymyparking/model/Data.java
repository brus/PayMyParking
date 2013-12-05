package eu.execom.paymyparking.model;

import java.util.List;

public class Data {
	
	private List<City> cities;
	private List<String> licensePlates;
	private Settings settings;
	
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public List<String> getLicensePlates() {
		return licensePlates;
	}
	public void setLicensePlates(List<String> licensePlates) {
		this.licensePlates = licensePlates;
	}
	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
