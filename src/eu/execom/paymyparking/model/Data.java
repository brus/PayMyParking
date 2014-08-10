package eu.execom.paymyparking.model;

import java.util.List;

public class Data {
	
	private List<City> cities;
	private City selectedCity;
	private List<String> licensePlates;
	private String defaultLicensePlate;
	
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public City getSelectedCity() {
		return selectedCity;
	}
	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}
	public List<String> getLicensePlates() {
		return licensePlates;
	}
	public void setLicensePlates(List<String> licensePlates) {
		this.licensePlates = licensePlates;
	}
	public String getDefaultLicensePlate() {
		return defaultLicensePlate;
	}
	public void setDefaultLicensePlate(String defaultLicensePlate) {
		this.defaultLicensePlate = defaultLicensePlate;
	}

}
