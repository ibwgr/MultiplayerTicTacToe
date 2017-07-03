package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.res.XmlResourceParser;

import org.junit.Test;
import org.mockito.Mockito;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;

/**
 * Created by rk on 02.07.17.
 */
public class UtilTest {

    @Test
    public void cleanStringShouldCleanupAllSpecialChars() {
        String t1 = "Hallo Max";   String r1 = "Hallo Max";
        String t2 = "Müller";      String r2 = "Mueller";
        String t3 = "O'Brian";     String r3 = "OBrian";
        String t4 = "<script>";    String r4 = "script";
        assertEquals(r1,Util.cleanString(t1));
        assertEquals(r2,Util.cleanString(t2));
        assertEquals(r3,Util.cleanString(t3));
        assertEquals(r4,Util.cleanString(t4));
    }


//    @Test
//    public void getServiceEndpointShouldReturnUrlString() throws Exception {
//        // not directly a unit test, but very important for the app to get the service endpoint!
//
//        // TODO java.lang.NullPointerException?? auf XmlPullParserFactory.newInstance()
//        // TODO Gemaess Stackoverflow geht das so mit Android nicht, da muesste man sich wohl erst einlesen!!
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        System.out.println(factory);
//        factory.setNamespaceAware(true);
//        XmlPullParser xpp = factory.newPullParser();
//        xpp.setInput( new StringReader( "<Config><ServiceEndpoint>https://warm-shelf-33316.herokuapp.com/</ServiceEndpoint></Config>" ) );
//
//        // Mocked MainActivity
//        final MainActivity mockedMainActivity = mock(MainActivity.class);
//
//        Mockito.when(mockedMainActivity.getResources().getXml(anyInt())).thenReturn( (XmlResourceParser)xpp );
//        String serviceEnpoint = Util.getServiceEndpoint(mockedMainActivity);
//        //System.out.println(serviceEnpoint);
//    }

}