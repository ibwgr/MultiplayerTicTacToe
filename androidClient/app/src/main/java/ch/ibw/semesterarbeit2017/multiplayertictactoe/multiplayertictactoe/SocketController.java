package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by rk on 11.06.17.
 */

public class SocketController {

    public static final String PROG = "____SOCKETCONTROLLER";

    private Socket socket;

    //constructor
    public SocketController() {
        socket = createSocket();
    }

    //
    public Socket createSocket() {
        Log.i(PROG, "socking...");
        try {
            //return IO.socket("https://warm-shelf-33316.herokuapp.com/");          // Test Client http://lastminute.li/ttt/
            return IO.socket("http://192.168.1.39:3100");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    //
    public void connect() {
        socket.connect();
        Log.i(PROG, socket.toString());
    }

    //
    public void send(String event, JSONObject json) {
        Log.i(PROG, "send/emit data to sever: " +json);
        socket.emit(event, json);
    }

    // getters/setters
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
