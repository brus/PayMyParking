package eu.execom.paymyparking.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import eu.execom.paymyparking.model.CitiesParseModel;
import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.ParkingZone;

public class CitiesService {
	
	private static String CITY = "city";
	private static String CITY_NAME = "city_name";
	private static String ZONE = "zone";
	private static String ZONE_NAME = "zone_name";
	private static String ZONE_NUMBER = "zone_number";
	private static String ZONE_COLOR = "zone_color";

	static public List<City> LoadCities(InputStream stream) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        
        xpp.setInput(new InputStreamReader(stream));

        CitiesParseModel citiesModel = new CitiesParseModel();
        
        int eventType = xpp.getEventType();
        do {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                // Do nothing
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                // Do nothing
            } else if(eventType == XmlPullParser.START_TAG) {
            	processStartTag(xpp, citiesModel);
            } else if(eventType == XmlPullParser.END_TAG) {
            	// Do nothing
            } else if(eventType == XmlPullParser.TEXT) {
                processText(xpp, citiesModel);
            }
            eventType = xpp.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        
        return citiesModel.getCities();
	}
	
	static private void processStartTag(XmlPullParser xpp, CitiesParseModel citiesModel) {
		if (xpp.getName().equals(CITY))
		{
			citiesModel.setCurrentCity(new City());
			citiesModel.getCities().add(citiesModel.getCurrentCity());
			citiesModel.setCurrentParkingZone(null);
			citiesModel.setLastTag(null);
		}
		else if (xpp.getName().equals(ZONE))
		{
			citiesModel.setCurrentParkingZone(new ParkingZone());
			citiesModel.getCurrentCity().getParkingZones().add(citiesModel.getCurrentParkingZone());
			citiesModel.setLastTag(null);
		}
		else {
			citiesModel.setLastTag(xpp.getName());
		}
	}

	static private void processText(XmlPullParser xpp, CitiesParseModel citiesModel) {
		String text = xpp.getText();
		if (text.contains("\t") || text.contains("\n")) {
			// Do nothing
		}
		else {
			// Values which needs to be handled
			if (citiesModel.getLastTag().equals(CITY_NAME)) {
				citiesModel.getCurrentCity().setName(text);
			}
			else if (citiesModel.getLastTag().equals(ZONE_NAME)) {
				citiesModel.getCurrentParkingZone().setName(text);
			}
			else if (citiesModel.getLastTag().equals(ZONE_NUMBER)) {
				citiesModel.getCurrentParkingZone().setPhoneNumber(text);
			}
			else if (citiesModel.getLastTag().equals(ZONE_COLOR)) {
				citiesModel.getCurrentParkingZone().setColor(text);
			}
		}
	}
}
