package eu.execom.paymyparking.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class LicensePlatesService {
	
	private static String LICENSE_PLATE = "license_plate";
	
	static public List<String> LoadLicensePlates(InputStream stream) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        
        xpp.setInput(new InputStreamReader(stream));
        
        LicensePlatesParseModel model = new LicensePlatesParseModel();
        
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
                processText(xpp, model);
            }
            eventType = xpp.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        
        return model.licensePlates;
	}
	
	static private void processStartTag(XmlPullParser xpp, LicensePlatesParseModel model) {
		model.setLastTag(xpp.getName());
	}

	static private void processText(XmlPullParser xpp, LicensePlatesParseModel model) {
		String text = xpp.getText();
		if (text.contains("\t") || text.contains("\n")) {
			// Do nothing
		}
		else {
			// Values which needs to be handled
			if (model.getLastTag().equals(LICENSE_PLATE)) {
				model.getLicensePlates().add(text);
			}
		}
	}
}
