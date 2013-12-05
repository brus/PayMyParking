package eu.execom.paymyparking.service;

import java.util.ArrayList;
import java.util.List;

public class LicensePlatesParseModel {
	String lastTag;
	List<String> licensePlates = new ArrayList<String>();

	public String getLastTag() {
		return lastTag;
	}

	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}

	public List<String> getLicensePlates() {
		return licensePlates;
	}

	public void setLicensePlates(List<String> licensePlates) {
		this.licensePlates = licensePlates;
	}
}
