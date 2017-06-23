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
    //  vom server erhaltene daten  (gameinfo)
    //------------------------------------------------------
    //------------------------------------------------------
    private String myName;   // from server
    private String player1Name;
    private String player2Name;
    private Boolean myTurn;
    private Boolean othersTurn;
    private Status gameStatus = Status.STOPPED;
    private Boolean iHaveWon;
    private Boolean otherHasWon;
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
    public String getPlayer1Name() {
        return player1Name;
    }
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }
    public String getPlayer2Name() {
        return player2Name;
    }
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }
    public Status getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(Status gameStatus) {
        this.gameStatus = gameStatus;
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


    public Emitter.Listener onDisconnectFromServer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onDisconnectFromServer");
                    onDisconnectFromServerActionMethod();
                }
            });
        }
    };
    private void onDisconnectFromServerActionMethod() {
        //  todo "sorry, technisches problem"
        //  todo spiel beenden
        //  todo screen aufraeumen
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


    public Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onStartGame");
                    onStartGameActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onStartGameActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onStartGameActionMethod");
        Log.i(PROG, "******************" +data.toString());
        // ich zuerst: ich bin player1, o
        // anderer zuerst: ich bin player2, x
        // muessen wir aber nicht speichern, der server weiss es
        // der letzte beginnt, ist ja zufaellig wer der letzte ist
        //
        act.displayStatus("Game started");
        try {
            this.setPlayer1Name(data.getString("player1"));
            this.setPlayer2Name(data.getString("player2"));
        } catch (JSONException e) {
            return;
        }
        Log.i(PROG, "****************** player1: "+this.getPlayer1Name());
        Log.i(PROG, "****************** player2: "+this.getPlayer2Name());
        this.setGameStatus(Status.RUNNING);
        act.displayPlayers("Player O: " +this.getPlayer1Name() +"  |  Player X: " +this.getPlayer2Name());
        
    }



}
