package eu.execom.paymyparking.model;

import java.util.ArrayList;
import java.util.List;

public class City {

	private String name;
	private List<ParkingZone> parkingZones;
	
	public City()
	{
		this.parkingZones = new ArrayList<ParkingZone>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParkingZone> getParkingZones() {
		return parkingZones;
	}

	public void setParkingZones(List<ParkingZone> zones) {
		this.parkingZones = zones;
	}

}
