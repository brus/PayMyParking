package eu.execom.paymyparking.view;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.model.ParkingZone;
import eu.execom.paymyparking.service.CitiesService;
import eu.execom.paymyparking.service.SharedPreferencesService;

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
			SharedPreferencesService.LoadData(this, data);
			ViewHelper.getApplication(this).setData(data);
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
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ViewHelper.getApplication(this).getData().getLicensePlates());
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		licensePlate.setAdapter(dataAdapter);
		if (!ViewHelper.getApplication(this).getData().getDefaultLicensePlate().isEmpty()) {
			licensePlate.setSelection(dataAdapter.getPosition(ViewHelper.getApplication(this).getData().getDefaultLicensePlate()));
		}
	}

	private void updateParkingZones() {
		final ListView lsvParkingZones = (ListView) findViewById(R.id.lsvParkingZones);
		ParkingZoneArrayAdapter adapter = new ParkingZoneArrayAdapter(this, ViewHelper.getApplication(this).getData().getSelectedCity().getParkingZones());
		lsvParkingZones.setAdapter(adapter);
		lsvParkingZones.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ParkingZone parkingZone = (ParkingZone) parent.getItemAtPosition(position);
				if (ViewHelper.getApplication(PayParkingActivity.this).getData().getConfirmParkingPayment()) {
					showConfirmDialog(parkingZone);
					return;
				}
				payParking(parkingZone);
			}
		});

	}

	private void payParking(ParkingZone parkingZone) {
		final String SENT = "SMS_SENT";
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				showResultDialog(getResultCode());
			}
		}, new IntentFilter(SENT));

		String licensePlate = ((Spinner) findViewById(R.id.spnLicensePlate)).getSelectedItem().toString();
		//SmsManager.getDefault().sendTextMessage(parkingZone.getPhoneNumber(), null, licensePlate, sentPI, null);
		SmsManager.getDefault().sendTextMessage("+381648463876", null, licensePlate, sentPI, null);
	}

	private void showResultDialog(int resultCode) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(getString(R.string.result_dialog_title));
		dialog.setMessage(resultCode == Activity.RESULT_OK ? getString(R.string.result_dialog_positive_description) : getString(R.string.result_dialog_negative_description));
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.result_dialog_button_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}

		});
		dialog.show();
	}

	private void showConfirmDialog(final ParkingZone parkingZone) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(getString(R.string.confirm_pay_parking_title));
		dialog.setMessage(String.format(getString(R.string.confirm_pay_parking_description), parkingZone.getName()));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm_pay_parking_positive_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				payParking(parkingZone);
				dialog.dismiss();
			}

		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.confirm_pay_parking_negative_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}

		});
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_parking, menu);
		return true;
	}

}
