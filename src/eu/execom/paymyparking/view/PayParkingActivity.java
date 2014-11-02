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
import android.widget.TextView;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.model.ParkingZone;
import eu.execom.paymyparking.service.CitiesService;
import eu.execom.paymyparking.service.SharedPreferencesService;

public class PayParkingActivity extends Activity {

	private static final String SENT = "SMS_SENT";
	private BroadcastReceiver smsIndicationReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_parking);

		initializeModel();
	}

	@Override
	protected void onResume() {
		super.onResume();

		updateLicensePlates();
		updateParkingZones();

		smsIndicationReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				String message = getResultCode() == Activity.RESULT_OK ? getString(R.string.result_dialog_positive_description) : getString(R.string.result_dialog_negative_description);
				showInfoDialog(getString(R.string.result_dialog_title), message, getResultCode() == Activity.RESULT_OK);
			}
		};
		registerReceiver(smsIndicationReceiver, new IntentFilter(SENT));
	}

	@Override
	protected void onPause() {
		super.onPause();

		unregisterReceiver(smsIndicationReceiver);
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
			e.printStackTrace();
		} catch (XmlPullParserException e) {
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
		final TextView cityName = (TextView) findViewById(R.id.tvCityName);
		cityName.setText(ViewHelper.getApplication(this).getData().getSelectedCity().getName());

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
		if (((Spinner) findViewById(R.id.spnLicensePlate)).getSelectedItem() == null) {
			showInfoDialog(getString(R.string.no_license_plate_title), getString(R.string.no_license_plate_description), false);
			return;
		}

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		String licensePlate = ((Spinner) findViewById(R.id.spnLicensePlate)).getSelectedItem().toString();
		SmsManager.getDefault().sendTextMessage(parkingZone.getPhoneNumber(), null, licensePlate, sentPI, null);
	}

	private void showInfoDialog(String title, String message, final Boolean closeActivity) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.ok_button_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (closeActivity) {
					finish();
				}
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
