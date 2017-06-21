package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
/**
 * Created by rk on 11.06.17.
 */
public class SocketControllerTest {

    private String player;

    @Test
    public void einVielEinfachererTestX() throws Exception {
        //
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject1.put("test", "test");
        jsonObject2.put("test", "test");
        System.out.println("Test JSON: " + jsonObject1.get("test"));
        assertEquals(jsonObject1.get("test"), jsonObject2.get("test"));


        JSONObject main = new JSONObject();
        main.put("Command", "CreateNewUser");
        JSONObject user = new JSONObject();
        user.put("FirstName", "John");
        user.put("LastName", "Reese");
        main.put("User", user);
        System.out.println("main : " +main.toString());
    }

    @Test
    public void einVielEinfachererTest() throws Exception {
        MainActivity fakeMainActivity = new MainActivity();
        SocketController socketController = new SocketController(null, fakeMainActivity);
        //
        //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        JSONObject obj = new JSONObject();
        try {
            obj.put("winner", "hans");
            obj.put("fields", "[2,4,6]");
            obj.put("username", "Emma");
            obj.put("youWon", "yes");
            System.out.println("Test JSON: " + obj.getString("youWon"));   // todo weshalb null ????
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());   // todo weshalb null ????
        socketController.onGameFinishedActionMethod(obj);
    }



    @Test
    public void testVersuch() throws Exception {

        Emitter.Listener onAnyEmitter = null;

        Emitter.Listener em = new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                player = "Hans";
            }
        };

        Emitter x = new Emitter();

        // Fake Server Response Object
        JSONObject fakeServerResponseObject = new JSONObject();
        try {
            fakeServerResponseObject.put("username", "Fritz");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Using Mockito
        final Socket socket = mock(Socket.class);
        //when(socket.emit("add_user",anyString())).thenReturn(null);   // todo das bringt ja voellig gar nichts!!

        final SocketController socketController = mock(SocketController.class);

        //when(socketController.getSocket().on("start_game", em)).thenReturn(fakeServerResponseObject);   // todo, das ist sinnvoll, geht so aber nicht
        //socketController.getSocket().on("start_game", em);

        when(socket.on("start_game", em));// todo, das ist sinnvoll, geht so aber nicht
        System.out.println("player : " +player);
        //Assert.assertTrue("Message sent successfully", text.sendTo("localhost", "1234"));
        //Assert.assertEquals("whatever you wanted to send".getBytes(), byteArrayOutputStream.toByteArray());
        //when(userValidator.isValidUsername(anyString())).thenReturn(true);
        //when(userValidator.doesUsernameExist(anyString())).thenReturn(false);

        //socketController.send("add_user", obj);
    }

}