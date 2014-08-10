package eu.execom.paymyparking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.Data;

public class LicensePlateArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private Data data;
	
	public LicensePlateArrayAdapter(Context context, Data data) {
		super(context, R.layout.layout_settings_lp_item, data.getLicensePlates());
		this.context = context;
		this.data = data;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.layout_settings_lp_item, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.lpCaption);
		textView.setText(data.getLicensePlates().get(position));
		
		ImageButton setDefault = (ImageButton) rowView.findViewById(R.id.setDefaultLicensePlate);
		setDefault.setBackgroundResource(data.getLicensePlates().get(position).equals(data.getDefaultLicensePlate()) ? R.drawable.star_on : R.drawable.star_off);
		setDefault.setTag(data.getLicensePlates().get(position));
		
		ImageButton delete = (ImageButton) rowView.findViewById(R.id.deleteLicensePlate);
		delete.setTag(data.getLicensePlates().get(position));

		return rowView;
	}
}
