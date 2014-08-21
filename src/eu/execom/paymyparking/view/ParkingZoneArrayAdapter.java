package eu.execom.paymyparking.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.execom.paymyparking.R;
import eu.execom.paymyparking.model.ParkingZone;

public class ParkingZoneArrayAdapter extends ArrayAdapter<ParkingZone> {

	private final Context context;
	private final List<ParkingZone> parkingZones;

	public ParkingZoneArrayAdapter(Context context, List<ParkingZone> parkingZones) {
		super(context, R.layout.layout_parking_zone_item, parkingZones);
		this.context = context;
		this.parkingZones = parkingZones;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.layout_parking_zone_item, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.zoneName);
		textView.setText(parkingZones.get(position).getName());

		ImageView imageView = (ImageView) rowView.findViewById(R.id.zoneImage);
		if (parkingZones.get(position).getColor() != null) {
			imageView.setImageResource(getImage(parkingZones.get(position).getColor()));
		}
		imageView.setVisibility(parkingZones.get(position).getColor() == null ? View.INVISIBLE : View.VISIBLE);

		return rowView;
	}

	private int getImage(String color) {
		if (color.equals("blue")) {
			return R.drawable.blue;
		} else if (color.equals("brown")) {
			return R.drawable.brown;
		} else if (color.equals("gold")) {
			return R.drawable.gold;
		} else if (color.equals("gray")) {
			return R.drawable.gray;
		} else if (color.equals("green")) {
			return R.drawable.green;
		} else if (color.equals("orange")) {
			return R.drawable.orange;
		} else if (color.equals("purple")) {
			return R.drawable.purple;
		} else if (color.equals("red")) {
			return R.drawable.red;
		} else if (color.equals("slate")) {
			return R.drawable.slate;
		} else if (color.equals("violet")) {
			return R.drawable.violet;
		} else if (color.equals("white")) {
			return R.drawable.white;
		} else if (color.equals("yellow")) {
			return R.drawable.yellow;
		}

		throw new IllegalArgumentException("Color is not supported: " + color);
	}

}
