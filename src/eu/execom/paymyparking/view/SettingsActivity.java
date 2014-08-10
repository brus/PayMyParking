package eu.execom.paymyparking.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.Data;

public class SettingsActivity extends Activity {

	private LicensePlateArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
		updateLicensePlates();
		addListeners();
		updateNewEnabled();
		initializeSelectCity();
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
		adapter.notifyDataSetChanged();
	}

	public void deleteLicensePlateOnClickHandler(View v) {
		ViewHelper.getApplication(this).getData().getLicensePlates().remove(v.getTag());
		adapter.notifyDataSetChanged();
	}

	public void addLicensePlateOnClickHandler(View v) {
		String licensePlate = ((EditText) findViewById(R.id.newLicensePlate)).getText().toString();
		((EditText) findViewById(R.id.newLicensePlate)).setText("");
		ViewHelper.getApplication(this).getData().getLicensePlates().add(licensePlate);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
