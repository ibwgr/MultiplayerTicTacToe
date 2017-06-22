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

    // da der SocketController selbst keine Activity ist
    Context ctx;
    MainActivity act;

    private Socket socket;

    //constructor
    public SocketController() {
    }
    // todo hier die Mainactivity bekommen,  oder wie es der Dozent vorschlaegt: die findViewById redundant nochmals suchen...
    public SocketController(Context ctx, MainActivity act) {
        this.ctx = ctx;
        this.act = act;
        //
        socket = createSocket();
    }
//    // todo wieder loeschen
//    private MainActivity activity;
//    public void testSetActivity(MainActivity activity){
//        this.activity = activity;
//    }
//    public void testUseActivity(String text){
//        this.activity.displayStatus(text);
//    }





    //-----------------------------------------------------
    //-----------------------------------------------------
    //  vom server erhaltene daten
    //------------------------------------------------------
    //------------------------------------------------------
    String myInitialName; // init
    String myName;   // from server
    Boolean myTurn;
    Boolean othersTurn;
    int gameStatus; // 0 1  todo enum
    Boolean iHaveWon;
    Boolean otherHasWon;
    public String getMyName() {
        return myName;
    }
    public void setMyName(String myName) {
        this.myName = myName;
    }
    public Boolean getMyTurn() {
        return myTurn;
    }
    public void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
    }
    public Boolean getOthersTurn() {
        return othersTurn;
    }
    public void setOthersTurn(Boolean othersTurn) {
        this.othersTurn = othersTurn;
    }
    public int getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }
    public Boolean getiHaveWon() {
        return iHaveWon;
    }
    public void setiHaveWon(Boolean iHaveWon) {
        this.iHaveWon = iHaveWon;
    }
    public Boolean getOtherHasWon() {
        return otherHasWon;
    }
    public void setOtherHasWon(Boolean otherHasWon) {
        this.otherHasWon = otherHasWon;
    }
    public String getMyInitialName() {
        return myInitialName;
    }
    public void setMyInitialName(String myInitialName) {
        this.myInitialName = myInitialName;
    }
    //-----------------------------------------------------
    //-----------------------------------------------------
    //-----------------------------------------------------



    //
    public Socket createSocket() {
        Log.i(PROG, "socking...");
        try {
            //return IO.socket("https://warm-shelf-33316.herokuapp.com/");          // Test Client http://lastminute.li/aaa/
            //return IO.socket("http://192.168.1.39:3100");
            return IO.socket("https://warm-shelf-33316.herokuapp.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    //
    public void connect() {
        Log.i(PROG, "****************** connect()");
        socket.connect();
        //Log.i(PROG, socket.toString());
    }

    //
    public void disconnect() {
        Log.i(PROG, "****************** disconnect()");
        if (socket.connected()) {
            socket.disconnect();
        }
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




    public Emitter.Listener onGameFinished = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onGameFinished");
                    onGameFinishedActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onGameFinishedActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onGameFinishedActionMethod");
        Log.i(PROG, "******************" +data.toString());
        //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        String winner;
        String[] fields;
        String userName;
        String youWon;
        try {
            winner = data.getString("winner");
            //fields[] = data.getJSONArray("fields");  // todo optisch anzeigen!
            userName = data.getString("username");
            youWon = data.getString("youWon");
            System.out.println("youWon: " +youWon);
        } catch (JSONException e) {
            return;
        }
        if (youWon.equals("yes")) {
            this.setiHaveWon(true);
            act.displayStatus("You won!");
        } else {
            this.setOtherHasWon(true);
            act.displayStatus("Sorry, you lost");
        }
    }


    public Emitter.Listener onUserAdded = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onUserAdded");
                    onUserAddedActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onUserAddedActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onUserAddedActionMethod");
        Log.i(PROG, "******************" +data.toString());
        String userName;
        try {
            userName = data.getString("username");
        } catch (JSONException e) {
            return;
        }
        //act.getGameInfo().setMyName(userName);
        act.displayStatus("Hello " +userName +"\n" + "Waiting for other user to play with...");
    }






}
