package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;

import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static final String PROG = "____MAIN";

    private EditText editUserName;
    private TextView displayZeile;

    private GameButton gameButton1;
    private GameButton gameButton2;
    private GameButton gameButton3;
    private GameButton gameButton4;
    private GameButton gameButton5;
    private GameButton gameButton6;
    private GameButton gameButton7;
    private GameButton gameButton8;
    private GameButton gameButton9;

    private String amZug = Const.AMZUGICH;


    private Socket mSocket;
    {
        try {
            Log.i(PROG, "socking...");
            mSocket = IO.socket("http://192.168.1.39:3100");
        } catch (URISyntaxException e) {}
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TODO  Besser http://www.vogella.com/tutorials/AndroidActionBar/article.html


        // https://guides.codepath.com/android/Using-the-App-Toolbar#using-toolbar-as-actionbar
        ///home/rk/Android/Sdk/platforms/android-25/data/res/drawable-mdpi
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        toolbar.setTitle("Multi");
        toolbar.setSubtitle("Tic");


        // get the values from fields
        editUserName = (EditText) findViewById(R.id.edit_username);
        displayZeile = (TextView) findViewById(R.id.label_displayzeile);

        //
        gameButton1 = (GameButton) findViewById(R.id.gameButton1);
        gameButton1.setNr(1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton2 = (GameButton) findViewById(R.id.gameButton2);
        gameButton2.setNr(2);
        gameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton3 = (GameButton) findViewById(R.id.gameButton3);
        gameButton3.setNr(3);
        gameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton4 = (GameButton) findViewById(R.id.gameButton4);
        gameButton4.setNr(4);
        gameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton5 = (GameButton) findViewById(R.id.gameButton5);
        gameButton5.setNr(5);
        gameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton6 = (GameButton) findViewById(R.id.gameButton6);
        gameButton6.setNr(6);
        gameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton7 = (GameButton) findViewById(R.id.gameButton7);
        gameButton7.setNr(7);
        gameButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton8 = (GameButton) findViewById(R.id.gameButton8);
        gameButton8.setNr(8);
        gameButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });
        //
        gameButton9 = (GameButton) findViewById(R.id.gameButton9);
        gameButton9.setNr(9);
        gameButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked(amZug);
            }
        });


        // get the OK button
        final Button buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userName = (editUserName.getText().toString());
                Log.w(PROG, "Username (aus Feld): " + userName);
                if (userName.length()>0) {
                    // TODO Username anzeigen
                    displayZeile.setText("Hallo " +userName );
                    // TODO Eingabefeld und Button disalbe
                    buttonOk.setVisibility(View.INVISIBLE);
                    editUserName.setVisibility(View.INVISIBLE);

                    mSocket.connect();
                    Log.i(PROG, mSocket.toString());

                    // username senden
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("add_user", obj);

                    Log.i(PROG, "username " +userName +" gesendet");



                    // Test only
                    GridLayout gameGridLayout = (GridLayout) findViewById(R.id.Game_GridLayout);
                    gameGridLayout.setBackgroundColor(Color.LTGRAY);
                    gameGridLayout.setClickable(false);
                    gameGridLayout.setEnabled(false);
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


        // socket listening
        Log.i(PROG, "listening for socket messages from server");

        mSocket.on("start_game", onStartGame);

        //bei start_game kommen keine weiteren daten

        // aber hier:
        //["other_turn",{"player":"x","username":"Emma"}]
        mSocket.on("your_turn", onYourTurn);
        mSocket.on("other_turn", onOtherTurn);//todo eigene meth.

    } // end on-create lifecycle





    //With this we listen on the new message event to receive messages from other users.
    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(PROG, "****************** game started");
                    displayZeile.setText("Game started");
                }
            });
        }
    };




    //With this we listen on the new message event to receive messages from other users.
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
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i(PROG, "****************** username: "+username);
                    Log.i(PROG, "****************** player: "+player);

                    displayZeile.setText(username +", your turn (" +player +")");
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
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i(PROG, "****************** username: "+username);
                    Log.i(PROG, "****************** player: "+player);

                    displayZeile.setText("Others turn ("+username +" as " +player +")");
                }
            });
        }
    };

}
