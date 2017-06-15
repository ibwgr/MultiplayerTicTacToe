package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import static org.junit.Assert.*;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import static org.mockito.Mockito.*;
/**
 * Created by rk on 15.06.17.
 */
public class MainActivityTest {

    Emitter.Listener em = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
          //  player = "Hans";
        }
    };

    @Test
    public void testVersuch() throws Exception {

        // Using Mockito
        final SocketController socketController = mock(SocketController.class);


        Answer<String> answer = new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                //String string = invocation.getArgumentAt(0, String.class);
                return "hallo";
            }
        };

        // choose your preferred way
        //when(dummy.stringLength("dummy")).thenAnswer(answer);
        //doAnswer(answer).when(dummy).stringLength("dummy");

        //when(socketController.getSocket().on("start_game", em)).thenReturn((Emitter) em);   // todo, das ist sinnvoll, geht so aber nicht
        //socketController.getSocket().on("start_game", em);

        //when(socket.on("start_game", em));// todo, das ist sinnvoll, geht so aber nicht
        //System.out.println("player : " + player);
        //Assert.assertTrue("Message sent successfully", text.sendTo("localhost", "1234"));
        //Assert.assertEquals("whatever you wanted to send".getBytes(), byteArrayOutputStream.toByteArray());
        //when(userValidator.isValidUsername(anyString())).thenReturn(true);
        //when(userValidator.doesUsernameExist(anyString())).thenReturn(false);

        //socketController.send("add_user", obj);

    }


}