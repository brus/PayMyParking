package eu.execom.paymyparking.view;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.service.CitiesService;
import eu.execom.paymyparking.service.LicensePlatesService;
import eu.execom.paymyparking.service.SettingsParseModel;
import eu.execom.paymyparking.service.SettingsService;

public class PayParkingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_parking);

		initializeModel();
		updateLicensePlates();
		updateParkingZones();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		case R.id.action_about:
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initializeModel() {
		AssetManager assetMgr = this.getAssets();

		try {
			Data data = new Data();
			data.setCities(CitiesService.LoadCities(assetMgr.open("parking_information.xml")));
			data.setLicensePlates(LicensePlatesService.LoadLicensePlates(assetMgr.open("license_plates.xml")));
			SettingsParseModel settings = SettingsService.LoadSettings(assetMgr.open("settings.xml"), data.getCities());
			data.setSelectedCity(settings.getSelectedCity());
			data.setDefaultLicensePlate(settings.getDefaultLicensePlate());
			ViewHelper.getApplication(this).setData(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			assetMgr.close();
		}
	}

	private void updateLicensePlates() {
		Spinner licensePlate = (Spinner) findViewById(R.id.spnLicensePlate);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ViewHelper.getApplication(this).getData().getLicensePlates());
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		licensePlate.setAdapter(dataAdapter);
	}

	private void updateParkingZones() {
		final ListView lsvParkingZones = (ListView) findViewById(R.id.lsvParkingZones);
		ParkingZoneArrayAdapter adapter = new ParkingZoneArrayAdapter(this, ViewHelper.getApplication(this).getData().getSelectedCity().getParkingZones());
		lsvParkingZones.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_parking, menu);
		return true;
	}

}
