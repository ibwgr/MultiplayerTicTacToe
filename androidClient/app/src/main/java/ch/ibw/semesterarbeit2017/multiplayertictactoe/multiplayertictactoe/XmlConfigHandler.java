package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.app.Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by rk on 03.10.17.
 */

public class XmlConfigHandler {

    public static String readValueFromXmlElement(String tag, Activity activity) {
        String serviceEndpoint = "";
        XmlPullParser xpp = activity.getResources().getXml(R.xml.tictactoe_config);
        try {
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    //Log.i(PROG, "****************** xpp.getName() : " +xpp.getName());
                    if (xpp.getName().equals(tag)) {
                        serviceEndpoint = xpp.nextText();
                        //Log.i(PROG, "****************** serviceEndpoint : " +serviceEndpoint);
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serviceEndpoint;
    }
}
