package eu.execom.paymyparking.application;

import eu.execom.paymyparking.model.Data;
import android.app.Application;

public class PayMyParkingApplication extends Application {
	
	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
