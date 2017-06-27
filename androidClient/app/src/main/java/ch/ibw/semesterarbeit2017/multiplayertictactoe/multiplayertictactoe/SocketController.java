package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

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


    //-----------------------------------------------------
    //-----------------------------------------------------
    //  vom server erhaltene daten  (gameinfo)
    //------------------------------------------------------
    //------------------------------------------------------
    private String myName;   // from server
    private String player1Name;
    private String player2Name;
    private String currentUserName;
    private String currentPlayerSymbol;
    private String currentField;
    private Boolean isAllButtonsEnabled;
    private Boolean isMyTurn;
    private Boolean isOthersTurn;
    private Boolean isIhaveWon;
    private Boolean isOtherHasWon;
    private Status gameStatus = Status.STOPPED;
    public String getMyName() {
        return myName;
    }
    public void setMyName(String myName) {
        this.myName = myName;
    }
    public Boolean getIsMyTurn() {
        return isMyTurn;
    }
    public void setIsMyTurn(Boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }
    public Boolean getIsOthersTurn() {
        return isOthersTurn;
    }
    public void setIsOthersTurn(Boolean isOthersTurn) {
        this.isOthersTurn = isOthersTurn;
    }
    public Boolean getIsIhaveWon() {
        return isIhaveWon;
    }
    public void setIsIhaveWon(Boolean isIhaveWon) {
        this.isIhaveWon = isIhaveWon;
    }
    public Boolean getIsOtherHasWon() {
        return isOtherHasWon;
    }
    public void setIsOtherHasWon(Boolean isOtherHasWon) {
        this.isOtherHasWon = isOtherHasWon;
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
    public String getCurrentUserName() {
        return currentUserName;
    }
    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
        Log.i(PROG, "****************** current-username: "+currentUserName);
    }
    public String getCurrentPlayerSymbol() {
        return currentPlayerSymbol;  // x oder o
    }
    public void setCurrentPlayerSymbol(String currentPlayerSymbol) {
        Log.i(PROG, "****************** current-player-symbol: "+currentPlayerSymbol);
        this.currentPlayerSymbol = currentPlayerSymbol;  // x oder o
    }
    public Boolean getIsAllButtonsEnabled() {
        return isAllButtonsEnabled;
    }
    public void setIsAllButtonsEnabled(Boolean isAllButtonsEnabled) {
        this.isAllButtonsEnabled = isAllButtonsEnabled;
    }
    public String getCurrentField() {
        return currentField;
    }
    public void setCurrentField(String currentField) {
        Log.i(PROG, "****************** current-field: "+currentField);
        this.currentField = currentField;
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
            return IO.socket("http://192.168.1.33:3100");
            //return IO.socket("https://warm-shelf-33316.herokuapp.com/");
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
            //userName = data.getString("username");
            youWon = data.getString("youWon");
        } catch (JSONException e) {
            return;
        }
        if (winner.equals("draw")) {
            this.setIsIhaveWon(false);
            this.setIsOtherHasWon(false);
            act.displayStatus("This game ended in a draw.\nPlay again?");
        } else if (youWon.equals("yes")) {
            this.setIsIhaveWon(true);
            this.setIsOtherHasWon(false);
            act.displayStatus("You won!\nPlay again?");
        } else {
            this.setIsIhaveWon(false);
            this.setIsOtherHasWon(true);
            act.displayStatus("Sorry, you lost\nPlay again?");
        }
        this.setGameStatus(Status.STOPPED);
        act.showWaitingImage(false);
        act.enableAllGameButtons(false);
        // fuer replay
        act.enableButtonOk();
    }


    //{"time":30,"player":"x","username":"Emma"}
    public Emitter.Listener onYourTurn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onYourTurn");
                    onYourTurnActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onYourTurnActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onYourTurnActionMethod");
        Log.i(PROG, "******************" +data.toString());
        try {
            this.setCurrentUserName(data.getString("username"));
            this.setCurrentPlayerSymbol(data.getString("player")); // x oder o
        } catch (JSONException e) {
            return;
        }
        act.displayStatus(this.getCurrentUserName() +", your turn (" +this.getCurrentPlayerSymbol() +")");
        act.showWaitingImage(false);
        act.enableAllGameButtons(true);
        this.setIsMyTurn(true);
        this.setIsOthersTurn(false);
    }

    // {"time":30,"player":"o","username":"vulkan"}
    public Emitter.Listener onOtherTurn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onYourTurn");
                    onOtherTurnActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onOtherTurnActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onOtherTurnActionMethod");
        Log.i(PROG, "******************" +data.toString());
        try {
            this.setCurrentUserName(data.getString("username"));
            this.setCurrentPlayerSymbol(data.getString("player")); // x oder o
        } catch (JSONException e) {
            return;
        }
        act.displayStatus("Others turn ("+this.getCurrentUserName() +" as " +this.getCurrentPlayerSymbol() +") \nplease wait...");
        act.showWaitingImage(true);
        //act.enableAllGameButtons(false)  //schon beim Buttonclick gesetzt, ist da schneller (w. Latenzzeit Server);
        this.setIsMyTurn(false);
        this.setIsOthersTurn(true);

    }

    // spielzug des gegners
    // {"field":"field2","player":"o"}
    public Emitter.Listener onNewMove = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onNewMove");
                    onNewMoveActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onNewMoveActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onNewMoveActionMethod");
        Log.i(PROG, "******************" +data.toString());
        try {
            this.currentField = data.getString("field");
            this.currentPlayerSymbol = data.getString("player");
        } catch (JSONException e) {
            return;
        }
        GameButton g = GameButton.findGameButtonByFieldId(this.getCurrentField());
        if (g != null) {
            g.clickedByOther();
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
        setMyName(userName);
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
