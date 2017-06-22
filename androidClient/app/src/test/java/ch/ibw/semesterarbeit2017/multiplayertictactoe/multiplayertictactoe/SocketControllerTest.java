package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
/**
 * Created by rk on 11.06.17.
 */
public class SocketControllerTest {

    private String player;

    @Test
    public void MOCKITO_onGameFinishedActionMethod_whenInformationIwon_shouldSet_IhaveWon() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        try {
            obj.put("winner", "hans");
            obj.put("fields", "[2,4,6]");
            obj.put("username", "Emma");
            obj.put("youWon", "yes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onGameFinishedActionMethod(obj);
        assert(socketController.getiHaveWon());
        assert(!socketController.getOtherHasWon());
    }

    @Test
    public void MOCKITO_onGameFinishedActionMethod_whenInformationIlost_shouldSet_IhaveLost() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        try {
            obj.put("winner", "hans");
            obj.put("fields", "[2,4,6]");
            obj.put("username", "Emma");
            obj.put("youWon", "no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onGameFinishedActionMethod(obj);
        assert(!socketController.getiHaveWon());
        assert(socketController.getOtherHasWon());
    }


//
//    @Test
//    public void testVersuch() throws Exception {
//
//        Emitter.Listener onAnyEmitter = null;
//
//        Emitter.Listener em = new Emitter.Listener() {
//            @Override
//            public void call(Object... objects) {
//                player = "Hans";
//            }
//        };
//
//        Emitter x = new Emitter();
//
//        // Fake Server Response Object
//        JSONObject fakeServerResponseObject = new JSONObject();
//        try {
//            fakeServerResponseObject.put("username", "Fritz");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Using Mockito
//        final Socket socket = mock(Socket.class);
//        //when(socket.emit("add_user",anyString())).thenReturn(null);   // todo das bringt ja voellig gar nichts!!
//
//        final SocketController socketController = mock(SocketController.class);
//
//        //when(socketController.getSocket().on("start_game", em)).thenReturn(fakeServerResponseObject);   // todo, das ist sinnvoll, geht so aber nicht
//        //socketController.getSocket().on("start_game", em);
//
//        when(socket.on("start_game", em));// todo, das ist sinnvoll, geht so aber nicht
//        System.out.println("player : " +player);
//        //Assert.assertTrue("Message sent successfully", text.sendTo("localhost", "1234"));
//        //Assert.assertEquals("whatever you wanted to send".getBytes(), byteArrayOutputStream.toByteArray());
//        //when(userValidator.isValidUsername(anyString())).thenReturn(true);
//        //when(userValidator.doesUsernameExist(anyString())).thenReturn(false);
//
//        //socketController.send("add_user", obj);
//    }

}