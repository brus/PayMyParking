package eu.execom.paymyparking.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.Data;
import eu.execom.paymyparking.service.SharedPreferencesService;

public class SettingsActivity extends Activity {

	private LicensePlateArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		updateLicensePlates();
		addListeners();
		updateNewEnabled();
		initializeSelectCity();
		initializeConfirmParkingPayment();
	}

	private void initializeConfirmParkingPayment() {
		((RadioButton) findViewById(R.id.confirmParkingPaymentNo)).setChecked(!ViewHelper.getApplication(this).getData().getConfirmParkingPayment());
		((RadioButton) findViewById(R.id.confirmParkingPaymentYes)).setChecked(ViewHelper.getApplication(this).getData().getConfirmParkingPayment());
	}

	private void initializeSelectCity() {
		Spinner selectCity = (Spinner) findViewById(R.id.spnSelectCity);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getListOfCities());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectCity.setAdapter(adapter);
		selectCity.setSelection(adapter.getPosition(ViewHelper.getApplication(this).getData().getSelectedCity().getName()));
	}

	private List<String> getListOfCities() {
		List<String> cities = new ArrayList<String>();
		for (City city : ViewHelper.getApplication(this).getData().getCities()) {
			cities.add(city.getName());
		}
		return cities;
	}

	private void updateNewEnabled() {
		String newLicensePlate = ((EditText) findViewById(R.id.newLicensePlate)).getText().toString();
		boolean enabled = !newLicensePlate.isEmpty() && !ViewHelper.getApplication(this).getData().getLicensePlates().contains(newLicensePlate);
		((ImageButton) findViewById(R.id.addLicensePlate)).setEnabled(enabled);
	}

	private void addListeners() {
		EditText newLicensePlate = (EditText) findViewById(R.id.newLicensePlate);
		newLicensePlate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateNewEnabled();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		Spinner selectCity = (Spinner) findViewById(R.id.spnSelectCity);
		selectCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Data data = ViewHelper.getApplication(SettingsActivity.this).getData();
				for (City city : data.getCities()) {
					if (city.getName().equals(parent.getItemAtPosition(pos).toString())) {
						data.setSelectedCity(city);
					}
				}
				SharedPreferencesService.SaveSelectedCity(SettingsActivity.this);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void updateLicensePlates() {
		final ListView lsvLicensePlates = (ListView) findViewById(R.id.lsvLicensePlates);

		adapter = new LicensePlateArrayAdapter(this, ViewHelper.getApplication(this).getData());
		lsvLicensePlates.setAdapter(adapter);
	}

	public void setDefaultLicensePlateOnClickHandler(View v) {
		ViewHelper.getApplication(this).getData().setDefaultLicensePlate((String) v.getTag());
		SharedPreferencesService.SaveDefaultLicensePlate(this);
		adapter.notifyDataSetChanged();
	}

	public void deleteLicensePlateOnClickHandler(View v) {
		showConfirmDelete(v.getTag().toString());
	}

	public void addLicensePlateOnClickHandler(View v) {
		String licensePlate = ((EditText) findViewById(R.id.newLicensePlate)).getText().toString();
		((EditText) findViewById(R.id.newLicensePlate)).setText("");
		ViewHelper.getApplication(this).getData().getLicensePlates().add(licensePlate);
		SharedPreferencesService.SaveLicensePlates(this);
		if (ViewHelper.getApplication(this).getData().getLicensePlates().size() == 1) {
			ViewHelper.getApplication(this).getData().setDefaultLicensePlate(ViewHelper.getApplication(this).getData().getLicensePlates().get(0));
			SharedPreferencesService.SaveDefaultLicensePlate(this);
		}
		adapter.notifyDataSetChanged();
	}

	public void onConfirmParkingPaymentClicked(View view) {
		ViewHelper.getApplication(this).getData().setConfirmParkingPayment(view.getId() == R.id.confirmParkingPaymentYes);
		SharedPreferencesService.SaveConfirmParkingPayment(this);
	}

	private void showConfirmDelete(final String licensePlate) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(getString(R.string.confirm_delete_title));
		dialog.setMessage(String.format(getString(R.string.confirm_delete_description), licensePlate));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm_delete_positive_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteLicensePlate(licensePlate);
				dialog.dismiss();
			}

		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.confirm_delete_negative_caption), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}

		});
		dialog.show();
	}

	private void deleteLicensePlate(final String licensePlate) {
		ViewHelper.getApplication(this).getData().getLicensePlates().remove(licensePlate);
		SharedPreferencesService.SaveLicensePlates(this);
		adapter.notifyDataSetChanged();
	}

}
