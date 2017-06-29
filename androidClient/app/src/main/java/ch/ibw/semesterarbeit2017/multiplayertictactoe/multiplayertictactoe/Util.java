package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by rk on 28.06.17.
 */

public class Util {

    public static final String PROG = "____UTIL";

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }


    public static String getServiceEndpoint(Activity activity) {
        String serviceEndpoint = "";
        XmlPullParser xpp = activity.getResources().getXml(R.xml.tictactoe_config);
        try {
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    Log.i(PROG, "****************** xpp.getName() : " +xpp.getName());
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

//
//
////        Resources res = activity.getResources();
////        XmlResourceParser xrp = res.getXml(R.xml.tictactoe_config);
//
//        XmlPullParser xpp = activity.getResources().getXml(R.xml.tictactoe_config);
//
//        XPath xpath = XPathFactory.newInstance().newXPath();
//        try {
//            String askFor2 = "//Config/ServiceEndpoint/text()";
//            //NodeList creaturesNodes = (NodeList) xpath.evaluate(askFor2, xpp, XPathConstants.NODESET);
//            //Log.i(PROG, "****************** creaturesNodes : " +creaturesNodes);
//            //serviceEndpoint = creaturesNodes.toString();
//            serviceEndpoint = xpath.evaluate(askFor2, xpp);
//            Log.i(PROG, "****************** serviceEndpoint : " +serviceEndpoint);
//        } catch (XPathExpressionException e) {
//            e.printStackTrace();
//        }
//
//
        return  serviceEndpoint;
    }
}