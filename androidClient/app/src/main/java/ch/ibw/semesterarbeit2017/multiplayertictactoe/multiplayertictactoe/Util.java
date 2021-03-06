package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by rk on 28.06.17.
 * Clean Code, 3.10.2017
 */

public class Util {

    public static final String PROG = "____UTIL";



    //-------------------------------------------------------
    public static String getServiceEndpointOLD(Activity activity) {
        String serviceEndpoint = "";
        XmlPullParser xpp = activity.getResources().getXml(R.xml.tictactoe_config);
        try {
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    //Log.i(PROG, "****************** xpp.getName() : " +xpp.getName());
                    if (xpp.getName().equals("ServiceEndpoint")) {
                        serviceEndpoint = xpp.nextText();
                        Log.i(PROG, "****************** serviceEndpoint : " +serviceEndpoint);
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  serviceEndpoint;
    }

    //-------------------------------------------------------
    public static String getServiceEndpoint(Activity activity) {
        String serviceEndpoint = XmlConfigHandler.readValueFromXmlElement("ServiceEndpoint", activity);
        return  serviceEndpoint;
    }
    public static String getIrgendWasAnderes(Activity activity) {
        String serviceEndpoint = XmlConfigHandler.readValueFromXmlElement("xxxxxxxxxxxxxxx", activity);
        return  serviceEndpoint;
    }

    //-------------------------------------------------------
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    //-------------------------------------------------------
    // clean up a string, replace Umlaute, replace Special-Chars, we don't want to send uncleaned content to the server!
    public static String cleanString(String str) {
        String out = str;
        out = replaceUmlaut(out);
        out = out.replaceAll("[^a-zA-Z0-9\\s]", "");
        return out;

    }
    private static String replaceUmlaut(String input) {
        //replace all lower Umlauts
        String output = input.replace("ü", "ue")
                .replace("ö", "oe")
                .replace("ä", "ae")
                .replace("ß", "ss");
        //first replace all capital umlaute in a non-capitalized context (e.g. Übung)
        output = output.replace("Ü(?=[a-zäöüß ])", "Ue")
                .replace("Ö(?=[a-zäöüß ])", "Oe")
                .replace("Ä(?=[a-zäöüß ])", "Ae");
        //now replace all the other capital umlaute
        output = output.replace("Ü", "UE")
                .replace("Ö", "OE")
                .replace("Ä", "AE");
        return output;
    }

}
