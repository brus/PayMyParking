package eu.execom.paymyparking.view;

import android.app.Activity;
import eu.execom.paymyparking.application.PayMyParkingApplication;

public class ViewHelper {

	public static PayMyParkingApplication getApplication(Activity activity) {
		return (PayMyParkingApplication) activity.getApplication();
	}
}
