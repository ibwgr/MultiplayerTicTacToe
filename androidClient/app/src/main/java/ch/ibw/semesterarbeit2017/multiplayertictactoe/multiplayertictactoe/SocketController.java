package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by rk on 11.06.17.
 */

public class SocketController {

    public static final String PROG = "____SOCKETCONTROLLER";
    public static final String PLAYER_TOKEN_O = "o";
    public static final String PLAYER_TOKEN_X = "x";
    public static final String FIELD_PREFIX = "field";

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
    private GameCountDown counter;
    private String player1Name;
    private String player2Name;
    private String currentUserName;
    private String currentPlayerSymbol;
    private String currentField;
    private int countDownTime;
    private int[] winningFields;
    private Boolean isAllButtonsEnabled;
    private Boolean isMyTurn;
    private Boolean isOthersTurn;
    private Boolean isIhaveWon;
    private Boolean isOtherHasWon;
    private Status gameStatus = Status.STOPPED;
    public void stopCounter() {
        counter.cancel();
    }
    public int getCountDownTime() {
        return countDownTime;
    }
    public void setCountDownTime(int countDownTime) {
        this.countDownTime = countDownTime;
    }
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
    public int[] getWinningFields() {
        return winningFields;
    }
    public void setWinningFields(int[] winningFields) {
        this.winningFields = winningFields;
    }
    //-----------------------------------------------------
    //-----------------------------------------------------
    //-----------------------------------------------------


    //
    public Socket createSocket() {
        Log.i(PROG, "socking...");
        String serviceEndpoint = Util.getServiceEndpoint(act);
        if (serviceEndpoint != null) {
            try {
                return IO.socket(serviceEndpoint);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            act.displayStatus("configuration error.\nplease check configfile in res/xml");
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


    //------------------------------------------------------------------------
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
        this.setGameStatus(Status.STOPPED);
        act.displayStatus("Sorry, technical problems\ndisconnect from server");
        act.enableAllGameButtons(false);
        act.clearCountDownDisplay();
    }


    public Emitter.Listener onConnectFailed = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onConnectFailed");
                    onConnectFailedActionMethod();
                }
            });
        }
    };
    private void onConnectFailedActionMethod() {
        this.setGameStatus(Status.STOPPED);
        act.displayStatus("Sorry, technical problems\nserver-connection failed");
        act.enableAllGameButtons(false);
        act.clearCountDownDisplay();
    }

    public Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onError");
                    onErrorActionMethod();
                }
            });
        }
    };
    private void onErrorActionMethod() {
        this.setGameStatus(Status.STOPPED);
        act.displayStatus("Sorry, technical problems\ntechnical error");
        act.enableAllGameButtons(false);
        act.clearCountDownDisplay();
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
        //{"winner":"TestFritz","username":"Emma","youWon":"no"}   <== falls der andere z.b. aufgegeben hat
        String winner ="";
        String youWon ="";
        Boolean hasFields = false;
        try {
            winner = data.getString("winner");
            youWon = data.getString("youWon");
            JSONArray fields = data.getJSONArray("fields");
            if (fields != null) {
                hasFields = true;
                this.setWinningFields( new int[]{(int)fields.get(0), (int)fields.get(1), (int)fields.get(2)} );
                Log.i(PROG, "****************** Winning Field1: " +this.getWinningFields()[0]);
                Log.i(PROG, "****************** Winning Field2: " +this.getWinningFields()[1]);
                Log.i(PROG, "****************** Winning Field3: " +this.getWinningFields()[2]);
            }
        } catch (JSONException e) {
            Log.i(PROG, "****************** JSON EXCEPTION! " );
            e.printStackTrace();
        }
        if (winner.equals("draw")) {
            this.setIsIhaveWon(false);
            this.setIsOtherHasWon(false);
            act.displayStatus("This game ended in a draw.\nPlay again?");
        } else if (youWon.equals("yes")) {
            this.setIsIhaveWon(true);
            this.setIsOtherHasWon(false);
            this.stopCounter();
            act.displayStatus("You won!\nPlay again?");
        } else {
            this.setIsIhaveWon(false);
            this.setIsOtherHasWon(true);
            act.displayStatus("Sorry, you lost\nPlay again?");
        }
        this.setGameStatus(Status.STOPPED);
        //act.showWaitingImage(false);
        act.enableAllGameButtons(false);
        act.clearCountDownDisplay();
        // fuer replay
        act.enableButtonOk();
        if (hasFields) {
            act.animateWinningFiedlds(this.getWinningFields());
        }
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
            this.setCountDownTime(data.getInt("time"));
        } catch (JSONException e) {
            return;
        }
        String message = this.getCurrentUserName() +", your turn, watch the time";  //  +(" +this.getCurrentPlayerSymbol() +")";
        act.displayStatus(message);
        //act.showWaitingImage(false);
        act.enableAllGameButtons(true);
        act.clearCountDownDisplay();
        this.setIsMyTurn(true);
        this.setIsOthersTurn(false);
        //
        // 10000 is the starting number (in milliseconds)
        // 1000 is the number to count down each time (in milliseconds)
        counter = new GameCountDown(this.getCountDownTime()*1000, 1000);
        counter.start();
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
        act.displayStatus("Waiting for "+this.getCurrentUserName() +"..."); // as " +this.getCurrentPlayerSymbol() +") \nplease wait...");
        //act.showWaitingImage(true);
        //act.enableAllGameButtons(false)  //schon beim Buttonclick gesetzt, ist da schneller (w. Latenzzeit Server);
        act.clearCountDownDisplay();
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

    //{"username":"Emma"}
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


    //{"msg":"Name is too long"}  ... oder already used
    public Emitter.Listener onUserNameValidation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onUserNameValidation");
                    onUserNameValidationActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onUserNameValidationActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onUserNameValidationActionMethod");
        Log.i(PROG, "******************" +data.toString());
        String msg;
        try {
            msg = data.getString("msg");
        } catch (JSONException e) {
            return;
        }
        act.displayStatus(msg);
        act.enableButtonOk();
        act.enableEingabefeld();
    }


    //{"player1":"kkkkkkkkk","player2":"uuuu"}
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
//        act.displayPlayers("Player O: " +this.getPlayer1Name() +"  |  Player X: " +this.getPlayer2Name());
        act.displayPlayers(this.getPlayer1Name() ,this.getPlayer2Name());
        
    }


    /* STATISTICS
        {
            "boardList":[
            {
                "timestamp":"6\/28\/2017, 9:59:58 PM",
                    "player1":"Emma",
                    "player2":"hans",
                    "status":"playing",
                    "change":"new"
            },
            {
                "timestamp":"6\/28\/2017, 9:41:10 PM",
                    "player1":"Emma",
                    "player2":"hans",
                    "status":"hans",
                    "change":""
            },
       ]
       */
    public Emitter.Listener onStatsUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onStatsUpdate");
                    onStatsUpdateActionMethod((JSONObject) args[0]);
                }
            });
        }
    };
    public void onStatsUpdateActionMethod(JSONObject data) {
        Log.i(PROG, "****************** onStatsUpdateActionMethod");
        Log.i(PROG, "******************" +data.toString());
        String statsTimestamp;
        String statsP1;
        String statsP2;
        String statsStatus; // playing oder der Name eines Spielers (winner)
        String statsChange; // new/update
        String output = "";
        ArrayList<StatsItem> statsItems = new ArrayList<>();
        try {
            JSONArray boardList = data.getJSONArray("boardList");
            Log.i(PROG, "******************" +boardList.toString());
            for (int i = 0; i < boardList.length(); i++) {
                JSONObject jsonobject = boardList.getJSONObject(i);
                statsTimestamp = jsonobject.getString("timestamp").replace(",","");
                statsP1 = jsonobject.getString("player1");
                statsP2 = jsonobject.getString("player2");
                statsStatus = jsonobject.getString("status");
                statsChange = jsonobject.getString("change");
                //Log.i(PROG, "****************** timestamp:" +statsTimestamp);
                if (statsStatus.equals(statsP1)) {
                    statsP1 += "\u2713";  //"\u2713"   siehe https://unicode-table.com/en/
                    statsP2 += " ";
                } else if (statsStatus.equals(statsP2)) {
                    statsP2 += "\u2713";
                    statsP1 += " ";
                } //else playing
                //output += statsTimestamp +": " +statsP1 +" " +statsP2  +" ("+statsChange +")\n";
                statsItems.add(new StatsItem(statsTimestamp, statsP1, statsP2, statsChange));
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        act.displayStatistics(statsItems);
    }







    ////////////////////////////////////////////////////////////////////////////
    //inner class
    // countdowntimer is an abstract class, so extend it and fill in methods
    public class GameCountDown extends CountDownTimer {

        public GameCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // nicht relevant, server beendet das spiel
            //act.displayStatus("TIMER done!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // todo ein extra feld fuer den timer machen!
            //act.displayStatus(currentPlayerSymbol +", time left: " + millisUntilFinished / 1000);
            Long secsL = ((millisUntilFinished / 1000));
            String secs;
            if (secsL < 10) {
                secs = "0"+Long.toString(secsL);
            } else {
                secs = Long.toString(secsL);
            }
            if (getCurrentPlayerSymbol().equals("x")) {
                act.displayCountdownPlayerX(secs);
            } else if (getCurrentPlayerSymbol().equals("o")) {
                act.displayCountdownPlayerO(secs);
            }
        }
    }


}
