package eu.execom.paymyparking.model;

/**
 * Contains information needed for settings screen.
 * 
 * @author brus
 * 
 */
public class Settings {

	private String licensePlate;
	private City selectedCity;

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

}
