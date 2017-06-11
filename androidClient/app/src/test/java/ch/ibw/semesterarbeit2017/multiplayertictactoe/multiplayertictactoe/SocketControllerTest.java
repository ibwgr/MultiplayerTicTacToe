package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;


import static org.mockito.Mockito.*;
/**
 * Created by rk on 11.06.17.
 */
public class SocketControllerTest {

    private String player;

    @Test
    public void createSocket() throws Exception {



        Emitter.Listener onAnyEmitter = null;

        Emitter.Listener em = new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                player = "Hans";
            }
        };

        // Fake Server Response Object
        JSONObject fakeServerResponseObject = new JSONObject();
        try {
            fakeServerResponseObject.put("username", "Fritz");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Using Mockito
        final Socket socket = mock(Socket.class);
        when(socket.emit("add_user",anyString())).thenReturn(null);   // todo das bringt ja voellig gar nichts!!

//      when(socket.on("start_game", onAnyEmitter)).thenReturn(fakeServerResponseObject);   // todo, das ist sinnvoll, geht so aber nicht
        when(socket.on("start_game", em));// todo, das ist sinnvoll, geht so aber nicht
        System.out.println("player : " +player);
        //Assert.assertTrue("Message sent successfully", text.sendTo("localhost", "1234"));
        //Assert.assertEquals("whatever you wanted to send".getBytes(), byteArrayOutputStream.toByteArray());
        //when(userValidator.isValidUsername(anyString())).thenReturn(true);
        //when(userValidator.doesUsernameExist(anyString())).thenReturn(false);

        //socketController.send("add_user", obj);




    }

}