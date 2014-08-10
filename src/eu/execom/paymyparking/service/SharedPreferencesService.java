package eu.execom.paymyparking.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.view.ViewHelper;

public class SharedPreferencesService {

	private static String SHARED_PREFERENCES = "shared_preferences";
	private static String LICENSE_PLATES = "license_plates";
	private static String DEFAULT_LICENSE_PLATE = "default_license_plate";
	private static String SELECTED_CITY = "selected_city";

	public static void LoadData(Activity activity, Data data) {
		SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Set<String> licensePlates = sharedPref.getStringSet(LICENSE_PLATES, new HashSet<String>());
		data.setLicensePlates(new ArrayList<String>(licensePlates));

		data.setDefaultLicensePlate(sharedPref.getString(DEFAULT_LICENSE_PLATE, ""));

		String selectedCity = sharedPref.getString(SELECTED_CITY, "");
		if (selectedCity.isEmpty()) {
			data.setSelectedCity(data.getCities().get(0));
		} else {
			for (City city : data.getCities()) {
				if (city.getName().equals(selectedCity)) {
					data.setSelectedCity(city);
				}
			}
		}
	}

	public static void SaveLicensePlates(Activity activity) {
		SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(LICENSE_PLATES, new HashSet<String>(ViewHelper.getApplication(activity).getData().getLicensePlates()));
		editor.commit();
	}

	public static void SaveDefaultLicensePlate(Activity activity) {
		SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(DEFAULT_LICENSE_PLATE, ViewHelper.getApplication(activity).getData().getDefaultLicensePlate());
		editor.commit();
	}

	public static void SaveSelectedCity(Activity activity) {
		SharedPreferences sharedPref = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SELECTED_CITY, ViewHelper.getApplication(activity).getData().getSelectedCity().getName());
		editor.commit();
	}
}
