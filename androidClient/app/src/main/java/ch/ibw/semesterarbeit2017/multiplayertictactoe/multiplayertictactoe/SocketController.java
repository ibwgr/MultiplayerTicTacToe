package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

/**
 * Created by rk on 11.06.17.
 */

public class SocketController {

    public static final String PROG = "____SOCKETCONTROLLER";

//    // da der SocketController selbst keine Activity ist
//    Context ctx;
//    MainActivity act;

    private Socket socket;

    //constructor
    public SocketController() {
        socket = createSocket();
    }

//    //constructor
//    public SocketController(Context ctx, MainActivity act) {
//        this.ctx = ctx;
//        this.act = act;
//        //
//        socket = createSocket();
//    }
    private MainActivity activity;
    public void testSetActivity(MainActivity activity){
        this.activity = activity;
    }
    public void testUseActivity(String text){
        this.activity.displayStatus(text);
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

//
//    public static Emitter.Listener onUserAdded = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            act.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(PROG, "****************** onUserAdded");
//                    JSONObject data = (JSONObject) args[0];
//                    Log.i(PROG, "******************" +data.toString());
//                    String userName;
//                    try {
//                        userName = data.getString("username");
//                    } catch (JSONException e) {
//                        return;
//                    }
//                    act.findViewById(R.id.label_displayzeile);  // TODO weshalb nochmals suchen !!?
//                    TextView displayZeileStatus = (TextView) act.findViewById(R.id.label_displayzeile);
//                    displayZeileStatus.setText("Hallo " +userName);
//                }
//            });
//        }
//    };

}
