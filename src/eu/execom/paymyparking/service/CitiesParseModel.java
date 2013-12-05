package eu.execom.paymyparking.service;

import java.util.ArrayList;
import java.util.List;

import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.ParkingZone;

public class CitiesParseModel {
	private List<City> cities;
    private City currentCity;
    private ParkingZone currentParkingZone;
    private String lastTag;
    
    public CitiesParseModel() {
    	cities = new ArrayList<City>();
        currentCity = null;
        currentParkingZone = null;
        lastTag = null;
    }

	public List<City> getCities() {
		return cities;
	}

	public City getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
	}

	public ParkingZone getCurrentParkingZone() {
		return currentParkingZone;
	}

	public void setCurrentParkingZone(ParkingZone currentParkingZone) {
		this.currentParkingZone = currentParkingZone;
	}

	public String getLastTag() {
		return lastTag;
	}

	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
}
