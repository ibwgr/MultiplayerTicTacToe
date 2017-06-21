package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import static java.util.Arrays.asList;


public class MainActivity extends AppCompatActivity {

    public static final String PROG = "____MAINACTIVITY";

    private EditText editUserName;
    private TextView displayZeileStatus;
    private TextView displayZeilePlayers;
    private Toolbar toolbar;
    //private Menu menu;
    private ImageView waitingImage;

    private GameButton gameButton0;
    private GameButton gameButton1;
    private GameButton gameButton2;
    private GameButton gameButton3;
    private GameButton gameButton4;
    private GameButton gameButton5;
    private GameButton gameButton6;
    private GameButton gameButton7;
    private GameButton gameButton8;

    private GameInfo gameInfo;
    private String currentPlayer = "";  // todo weg damit!



    public GameInfo getGameInfo() {
        return gameInfo;
    }
    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    /*
    // Test Client http://lastminute.li/ttt/
    --------------------------------------------------------------------
    TODO background je nach groesse mit hdpi usw.
    TODO fixtexte translation ressource
    TODO layout hoch/quer/groessen
    TODO layout grid vielleicht durch table ersetzen, wegen grid-lines
    TODO testing socket mock https://stackoverflow.com/questions/5577274/testing-java-sockets
    TODO disconnect
    TODO socket.off
    --------------------------------------------------------------------
    */

    //
    private SocketController socketController = null; //new SocketController(getApplicationContext(), this);
    //private SocketController socketController = new SocketController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketController = new SocketController(getApplicationContext(), this);
        gameInfo = new GameInfo();
        setUpGame();


        // get the view elements
        editUserName = (EditText) findViewById(R.id.edit_username);
        displayZeileStatus = (TextView) findViewById(R.id.label_displayzeile);
        displayZeilePlayers = (TextView) findViewById(R.id.label_displayplayers);

        // get the OK button
        final Button buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userName = (editUserName.getText().toString());
                Log.w(PROG, "Username (aus Feld): " + userName);
                if (userName.length()>0) {
                    //
                    displayZeileStatus.setText("bitte warten..." );
                    //displayZeileStatus.setText("Hallo " +userName );

                    // Eingabefeld und Button disable
                    buttonOk.setVisibility(View.INVISIBLE);
                    editUserName.setVisibility(View.INVISIBLE);

                    socketController.connect();
                    // username senden  // todo daten mgmt in controller
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketController.send("add_user", obj);

                    displayZeileStatus.setText("Hello " +userName +"\n" + "Waiting for other user to play with...");

                } else {
                    Toast.makeText(getApplicationContext(), "Bitte zuerst einen Usernamen eingeben", Toast.LENGTH_LONG).show();
                }
            }
        });



        // Temporaere Buttons, nur fuer Entwicklulng  // todo alles auskommentieren
        final Button buttonSimWin = (Button) findViewById(R.id.button_sim_win);
        buttonSimWin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
                JSONObject obj = new JSONObject();
                try {
                    obj.put("winner", "hans");
                    obj.put("fields", "[2,4,6]");
                    obj.put("username", "Emma");
                    obj.put("youWon", "yes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socketController.onGameFinishedActionMethod(obj);
            }
        });

//        // Temporaere Buttons, nur fuer Entwicklulng
//        final Button buttonTempEnable = (Button) findViewById(R.id.button_temp_enable_all);
//        buttonTempEnable.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                GameButton.enableAllGameButtons();
//            }
//        });
//        final Button buttonTempRestart = (Button) findViewById(R.id.button_temp_restart);
//        buttonTempRestart.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                // todo ein exit zum server senden  @Dieter, eigentlich dasselbe wie nach timeout
//                setUpGame();
//            }
//        });

        //////////////////////////////////////////////////////////////////////////////////////
        // socket listening
        // here we listen on message events from the server
        // todo gehoert in socketcontroller
        Log.i(PROG, "listening for socket messages from server");
        socketController.getSocket().on("start_game", onStartGame);
        socketController.getSocket().on("user_added", socketController.onUserAdded);
        socketController.getSocket().on("your_turn", onYourTurn);
        socketController.getSocket().on("other_turn", onOtherTurn);
        socketController.getSocket().on("new_move", onNewMove);  // Spielzug des Gegners
        socketController.getSocket().on("game_finished", socketController.onGameFinished);

    } // end on-create lifecycle




    private void setUpGame() {
        // initializing
        currentPlayer = "";                ;

        // als erstes die GameButton Instanzen ermitteln
        //
        gameButton0 = (GameButton) findViewById(R.id.gameButton0);
        gameButton0.setNr(0);
        //gameButton0.setAlpha(0.5f);  // todo neue init methode (auch fuer nochmals-spielen)
        gameButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton1 = (GameButton) findViewById(R.id.gameButton1);
        gameButton1.setNr(1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton2 = (GameButton) findViewById(R.id.gameButton2);
        gameButton2.setNr(2);
        gameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton3 = (GameButton) findViewById(R.id.gameButton3);
        gameButton3.setNr(3);
        gameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton4 = (GameButton) findViewById(R.id.gameButton4);
        gameButton4.setNr(4);
        gameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton5 = (GameButton) findViewById(R.id.gameButton5);
        gameButton5.setNr(5);
        gameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton6 = (GameButton) findViewById(R.id.gameButton6);
        gameButton6.setNr(6);
        gameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton7 = (GameButton) findViewById(R.id.gameButton7);
        gameButton7.setNr(7);
        gameButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //
        gameButton8 = (GameButton) findViewById(R.id.gameButton8);
        gameButton8.setNr(8);
        gameButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(currentPlayer);
            }
        });
        //

        //
        waitingImage = (ImageView) findViewById(R.id.waiting_img);

        // init game
        GameButton.setSocketController(socketController);
        GameButton.setAllGameButtons(asList(gameButton0,gameButton1,gameButton2,gameButton3,
                gameButton4,gameButton5,gameButton6,gameButton7,gameButton8));
        GameButton.disableAllGameButtons();
    }




//
//
//    private Emitter.Listener onUserAdded = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
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
//                    displayZeilePlayers.setText("Hallo " +userName);
//                }
//            });
//        }
//    };


    // todo gehoert in socketcontroller
    //
    private void handleOnStartGame(Object args[]) {
        Log.i(PROG, "****************** game started");
        displayZeileStatus.setText("Game started");
        JSONObject data = (JSONObject) args[0];
        Log.i(PROG, "******************" +data.toString());
        String player1;
        String player2;
        try {
            player1 = data.getString("player1");
            player2 = data.getString("player2");
        } catch (JSONException e) {
            return;
        }
        Log.i(PROG, "****************** player1: "+player1);
        Log.i(PROG, "****************** player2: "+player2);
        displayZeilePlayers.setText("Player O: " +player1 +"  |  Player X: " +player2);
    }

    // todo gehoert in socketcontroller
    // ich zuerst: ich bin player1, o
    // anderer zuerst: ich bin player2, x
    // muessen wir aber nicht speichern, der server weiss es
    // der letzte beginnt, ist ja zufaellig wer der letzte ist
    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handleOnStartGame(args);
//                    Log.i(PROG, "****************** game started");
//                    displayZeileStatus.setText("Game started");
//
//                    JSONObject data = (JSONObject) args[0];
//                    Log.i(PROG, "******************" +data.toString());
//                    String player1;
//                    String player2;
//                    try {
//                        player1 = data.getString("player1");
//                        player2 = data.getString("player2");
//                    } catch (JSONException e) {
//                        return;
//                    }
//                    Log.i(PROG, "****************** player1: "+player1);
//                    Log.i(PROG, "****************** player2: "+player2);
//
//                    displayZeilePlayers.setText("Player O: " +player1 +"  |  Player X: " +player2);
                }
            });
        }
    };


    // todo gehoert in socketcontroller
    //
    //["other_turn",{"player":"x","username":"Emma"}]
    private Emitter.Listener onYourTurn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** your turn");

                    JSONObject data = (JSONObject) args[0];
                    Log.i(PROG, "******************" +data.toString());
                    String username;
                    String player;
                    try {
                        username = data.getString("username");
                        player = data.getString("player");
                        currentPlayer = player;
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i(PROG, "****************** username: "+username);
                    Log.i(PROG, "****************** player: "+player);  // x oder o

                    displayZeileStatus.setText(username +", your turn (" +player +")");
                    waitingImage.setVisibility(View.INVISIBLE);
                    GameButton.enableAllGameButtons();
                }
            });
        }
    };


    // todo gehoert in socketcontroller
    //With this we listen on the new message event to receive messages from other users.
    //["other_turn",{"player":"x","username":"Emma"}]
    private Emitter.Listener onOtherTurn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** other turn");

                    JSONObject data = (JSONObject) args[0];
                    Log.i(PROG, "******************" +data.toString());
                    String username;
                    String player;
                    try {
                        username = data.getString("username");
                        player = data.getString("player");
                        currentPlayer = player;
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i(PROG, "****************** username: "+username);
                    Log.i(PROG, "****************** player: "+player);

                    displayZeileStatus.setText("Others turn ("+username +" as " +player +") \nplease wait...");
                    waitingImage.setVisibility(View.VISIBLE);
                }
            });
        }
    };


    // todo gehoert in socketcontroller
    // Gegner hat gezogen
    private Emitter.Listener onNewMove = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** new move (gegner)");
                    //displayZeileStatus.setText("Game started");

                    JSONObject data = (JSONObject) args[0];
                    Log.i(PROG, "******************" +data.toString());
                    String player;
                    String field;
                    try {
                        player = data.getString("player");
                        field = data.getString("field");
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i(PROG, "****************** player: "+player);
                    Log.i(PROG, "****************** field: "+field);

                    GameButton g = GameButton.findGameButtonByFieldId(field);
                    if (g != null) {
                        g.clickedByOther(player);
                    }
                }
            });
        }
    };


    // DISPLAY Methoden
    public void displayStatus(String text) {
        displayZeileStatus.setText(text);
    }

}
