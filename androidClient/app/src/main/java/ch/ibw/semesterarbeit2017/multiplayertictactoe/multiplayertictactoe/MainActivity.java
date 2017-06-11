package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import static java.util.Arrays.asList;


public class MainActivity extends AppCompatActivity {

    public static final String PROG = "____MAIN";

    private EditText editUserName;
    private TextView displayZeileStatus;
    private TextView displayZeilePlayers;
    private Toolbar toolbar;
    private Menu menu;

    private GameButton gameButton0;
    private GameButton gameButton1;
    private GameButton gameButton2;
    private GameButton gameButton3;
    private GameButton gameButton4;
    private GameButton gameButton5;
    private GameButton gameButton6;
    private GameButton gameButton7;
    private GameButton gameButton8;

    private String currentPlayer = "";


    /*
    // Test Client http://lastminute.li/ttt/
    --------------------------------------------------------------------
    TODO background je nach groesse mit hdpi usw.
    TODO fixtexte translation ressource
    TODO layout hoch/quer/groessen
    TODO layout grid vielleicht durch table ersetzen, wegen grid-lines
    TODO testing socket mock https://stackoverflow.com/questions/5577274/testing-java-sockets
    --------------------------------------------------------------------
    */

    private SocketController socketController = new SocketController();
    //private Socket scSocket = socketController.getSocketController();


//    // Menu icons are inflated just as they were with actionbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        this.menu = menu;
//       // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_commit));
//       // menu.getItem(1).setTitle("Online");
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.miSymbol:
//                Toast.makeText(MainActivity.this, "clicking on symbol", Toast.LENGTH_SHORT).show();
//                return true;
//                //            case R.id.miCompose:
//                //                Toast.makeText(MainActivity.this, "clicking on email", Toast.LENGTH_SHORT).show();
//                //                return true;
//                //            case R.id.miProfile:
//                //                Toast.makeText(MainActivity.this, "clicking on profile", Toast.LENGTH_SHORT).show();
//                //                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpGame();

        // get the values from fields
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
                    // username senden
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketController.send("add_user", obj);

                    displayZeileStatus.setText("Hello " +userName +"\n" + "Waiting for other user to play with...");

//                    Log.i(PROG, "username " +userName +" gesendet");
//                    toolbar.setTitle("Tic-Tac-Toe, User:"+userName);

                    // Test only
                    //GridLayout gameGridLayout = (GridLayout) findViewById(R.id.Game_GridLayout);
                    //gameGridLayout.setBackgroundColor(Color.LTGRAY);
                    //gameGridLayout.setClickable(false);
                    //gameGridLayout.setEnabled(false);
                    //gameGridLayout.setVisibility(View.GONE);
                    /*
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = gameGridLayout.getChildAt(i);
                        child.setEnabled(false);
                    }
                    */

                } else {
                    Toast.makeText(getApplicationContext(), "Bitte zuerst einen Usernamen eingeben", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Temporaere Buttons, nur fuer Entwicklulng
        final Button buttonTempEnable = (Button) findViewById(R.id.button_temp_enable_all);
        buttonTempEnable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GameButton.enableAllGameButtons();
            }
        });
        final Button buttonTempRestart = (Button) findViewById(R.id.button_temp_restart);
        buttonTempRestart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // todo ein exit zum server senden  @Dieter, eigentlich dasselbe wie nach timeout
                setUpGame();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////
        // socket listening
        // here we listen on message events from the server
        // todo gehoert in socketcontroller
        Log.i(PROG, "listening for socket messages from server");
        socketController.getSocket().on("start_game", onStartGame);
        socketController.getSocket().on("user_added", onUserAdded);
        socketController.getSocket().on("your_turn", onYourTurn);
        socketController.getSocket().on("other_turn", onOtherTurn);
        socketController.getSocket().on("new_move", onNewMove);  // Spielzug des Gegners

    } // end on-create lifecycle




    private void setUpGame() {
        // initializing
        currentPlayer = "";

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

        // init game
        GameButton.setSocketController(socketController);
        GameButton.setAllGameButtons(asList(gameButton0,gameButton1,gameButton2,gameButton3,
                gameButton4,gameButton5,gameButton6,gameButton7,gameButton8));
        GameButton.disableAllGameButtons();
    }





    // todo gehoert in socketcontroller
    private Emitter.Listener onUserAdded = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** onUserAdded");
                    JSONObject data = (JSONObject) args[0];
                    Log.i(PROG, "******************" +data.toString());
                    String userName;
                    try {
                        userName = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    displayZeilePlayers.setText("Hallo " +userName);
                }
            });
        }
    };


    // ich zuerst: ich bin player1, o
    // anderer zuerst: ich bin player2, x
    // muessen wir aber nicht speichern, der server weiss es
    // der letzte beginnt   // Todo muesste man nicht eher zufaellig starten @Dieter
    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            });
        }
    };


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

                    GameButton.enableAllGameButtons();
                }
            });
        }
    };

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
                }
            });
        }
    };


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

}
