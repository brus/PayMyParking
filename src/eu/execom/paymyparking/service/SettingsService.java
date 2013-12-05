package eu.execom.paymyparking.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import eu.execom.paymyparking.model.City;
import eu.execom.paymyparking.model.Settings;

public class SettingsService {
	
	private static String LICENSE_PLATE = "license_plate";
	private static String CITY = "city";
	
	static public Settings LoadSettings(InputStream stream, List<City> cities) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        
        xpp.setInput(new InputStreamReader(stream));
        
        SettingsParseModel model = new SettingsParseModel();
        
        int eventType = xpp.getEventType();
        do {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                // Do nothing
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                // Do nothing
            } else if(eventType == XmlPullParser.START_TAG) {
            	processStartTag(xpp, model);
            } else if(eventType == XmlPullParser.END_TAG) {
            	// Do nothing
            } else if(eventType == XmlPullParser.TEXT) {
                processText(xpp, model, cities);
            }
            eventType = xpp.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        
        return model.getSettings();
	}
	
	static private void processStartTag(XmlPullParser xpp, SettingsParseModel model) {
		model.setLastTag(xpp.getName());
	}

	static private void processText(XmlPullParser xpp, SettingsParseModel model, List<City> cities) {
		String text = xpp.getText();
		if (text.contains("\t") || text.contains("\n")) {
			// Do nothing
		}
		else {
			// Values which needs to be handled
			if (model.getLastTag().equals(LICENSE_PLATE)) {
				model.getSettings().setLicensePlate(text);
			}
			else if (model.getLastTag().equals(CITY)) {
				model.getSettings().setSelectedCity(getCity(cities, text));
			}
		}
	}

	private static City getCity(List<City> cities, String cityName) {
		for (City city : cities) {
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}
}
