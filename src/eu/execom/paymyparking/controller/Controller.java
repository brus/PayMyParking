package eu.execom.paymyparking.controller;

import eu.execom.paymyparking.model.Data;
import android.app.Application;

public class Controller extends Application {
	
	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
