package eu.execom.paymyparking.view;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.controller.Controller;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.service.CitiesService;
import eu.execom.paymyparking.service.LicensePlatesService;
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
	
	private Controller getController() {
		return (Controller) this.getApplication();
	}

	private void initializeModel() {
		AssetManager assetMgr = this.getAssets();
		
		try {
			Data data = new Data();
			data.setCities(CitiesService.LoadCities(assetMgr.open("parking_information.xml")));
			data.setLicensePlates(LicensePlatesService.LoadLicensePlates(assetMgr.open("license_plates.xml")));
			data.setSettings(SettingsService.LoadSettings(assetMgr.open("settings.xml"), data.getCities()));
			getController().setData(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateLicensePlates() {
		Spinner licensePlate = (Spinner) findViewById(R.id.spnLicensePlate);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getController().getData().getLicensePlates());
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		licensePlate.setAdapter(dataAdapter);
	}

	private void updateParkingZones() {
		final ListView lsvParkingZones = (ListView) findViewById(R.id.lsvParkingZones);
	    
	    ParkingZoneArrayAdapter adapter = new ParkingZoneArrayAdapter(this, getController().getData().getSettings().getSelectedCity().getParkingZones());
	    lsvParkingZones.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_parking, menu);
		return true;
	}

}
