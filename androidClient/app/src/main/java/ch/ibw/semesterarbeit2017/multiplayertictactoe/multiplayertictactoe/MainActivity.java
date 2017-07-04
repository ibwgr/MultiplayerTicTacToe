package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.util.Arrays.asList;


public class MainActivity extends AppCompatActivity {

    public static final String PROG = "____MAINACTIVITY";

    private EditText editUserName;
    private TextView displayZeileStatus;
    private TextView displayPlayerXname, displayPlayerOname;
    private TextView displayPlayerXcountdown, displayPlayerOcountdown;
    private GameButton gameButton0;
    private GameButton gameButton1;
    private GameButton gameButton2;
    private GameButton gameButton3;
    private GameButton gameButton4;
    private GameButton gameButton5;
    private GameButton gameButton6;
    private GameButton gameButton7;
    private GameButton gameButton8;
    private Button buttonOk;
    private SocketController socketController = null;
    private TabHost mTabHost;
    //
    public TabHost getTabHost() {
        return mTabHost;
    }

    /*
    --------------------------------------------------------------------
    // Test Client http://lastminute.li/aaa/   (oder /ttt/)
    --------------------------------------------------------------------
    */

   // Kein disconnect bei onPause, wenn die App nur in den Hintergrund geht, man kann sie ja wieder hervorholen

    @Override
    protected void onStop() {
        super.onStop();
        socketController.disconnect();
        enableEingabefeld();
        enableButtonOk();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketController.disconnect();
        enableEingabefeld();
        enableButtonOk();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---------------------------------------------------------------------
        // TABS
        //---------------------------------------------------------------------
        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        //Lets add the first Tab
        TabHost.TabSpec mSpec = mTabHost.newTabSpec("play");
        mSpec.setContent(R.id.play_Tab);
        mSpec.setIndicator("play");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("stats");
        mSpec.setContent(R.id.stats_Tab);
        mSpec.setIndicator("stats");
        mTabHost.addTab(mSpec);
        //Lets add the third Tab
        mSpec = mTabHost.newTabSpec("about");
        mSpec.setContent(R.id.about_Tab);
        mSpec.setIndicator("about");
        mTabHost.addTab(mSpec);

        RelativeLayout layoutMain = (RelativeLayout) findViewById(R.id.layout_main);
        layoutMain.setOnTouchListener(new OnSwipeTouchListener(this));

        //---------------------------------------------------------------------
        // SOCKETCONTROLLER
        //---------------------------------------------------------------------
        socketController = new SocketController(getApplicationContext(), this, Util.getServiceEndpoint(this));
        socketController.connect();
        setUpInitalGame();

        // get the view elements
        editUserName = (EditText) findViewById(R.id.edit_username);
        displayZeileStatus = (TextView) findViewById(R.id.label_displayzeile);
        displayPlayerXname = (TextView) findViewById(R.id.player_x_name);
        displayPlayerOname = (TextView) findViewById(R.id.player_o_name);
        displayPlayerXcountdown= (TextView) findViewById(R.id.player_x_countdown);
        displayPlayerOcountdown= (TextView) findViewById(R.id.player_o_countdown);

        // get the OK/Start button
        buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //
                // Button wird benoetigt fuer:
                //  a) start
                //  b) restart nach spielende
                //
                String userName = "";
                if (socketController.getGameStatus().equals(Status.NEW)) {
                    userName = (editUserName.getText().toString());
                    // cleanup userName for security reasons
                    userName = Util.cleanString(userName);
                    Log.w(PROG, "Username (aus Feld, cleaned): " + userName);
                    if (userName.length() > 0) {
                        //
                    } else {
                        Toast.makeText(getApplicationContext(), "Bitte zuerst einen Usernamen eingeben", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {  // hat sich bereits registriert
                    userName = socketController.getMyName();
                }
                // wenn username eingegeben oder es ist ein restart nach spielende
                displayStatus("bitte warten...");
                disableButtonOk();
                disableEingabefeld();
                hideKeyboard();
                if (socketController.getGameStatus().equals(Status.STOPPED)) {
                    setUpReplayGame();
                }

                // username senden, egal ob erstes spiel oder restart, ist ein registrieren am server
                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socketController.send("add_user", obj);
                // jetzt koennte vom Server aber noch (irgendwann) zurueckkommen, dass der username nicht ok sei!!
                displayStatus("...waiting for server...");
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////
        // socket listening
        // here we listen on message events from the server
        //////////////////////////////////////////////////////////////////////////////////////
        Log.i(PROG, "listening for socket messages from server");
        socketController.getSocket().on("user_added", socketController.onUserAdded);
        socketController.getSocket().on("username_validation", socketController.onUserNameValidation);
        socketController.getSocket().on("start_game", socketController.onStartGame);
        socketController.getSocket().on("your_turn", socketController.onYourTurn);
        socketController.getSocket().on("other_turn", socketController.onOtherTurn);
        socketController.getSocket().on("new_move", socketController.onNewMove);  // Spielzug des Gegners
        socketController.getSocket().on("game_finished", socketController.onGameFinished);
        socketController.getSocket().on("disonnect", socketController.onDisconnectFromServer);  // disconnect from server received!
        socketController.getSocket().on("connect_failed", socketController.onConnectFailed);
      //socketController.getSocket().on("error", socketController.onError);
        socketController.getSocket().on("stats_update", socketController.onStatsUpdate);

    } // end on-create lifecycle



    private void setUpReplayGame() {
        // initializing
        socketController.setGameStatus(Status.NEW);
        gameButton0.reInitializeButton();
        gameButton1.reInitializeButton();
        gameButton2.reInitializeButton();
        gameButton3.reInitializeButton();
        gameButton4.reInitializeButton();
        gameButton5.reInitializeButton();
        gameButton6.reInitializeButton();
        gameButton7.reInitializeButton();
        gameButton8.reInitializeButton();
    }

    private void setUpInitalGame() {
        // initializing
        socketController.setGameStatus(Status.NEW);
             
        // als erstes die GameButton Instanzen ermitteln
        //
        gameButton0 = (GameButton) findViewById(R.id.gameButton0);
        gameButton0.setNr(0);
        gameButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton1 = (GameButton) findViewById(R.id.gameButton1);
        gameButton1.setNr(1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton2 = (GameButton) findViewById(R.id.gameButton2);
        gameButton2.setNr(2);
        gameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton3 = (GameButton) findViewById(R.id.gameButton3);
        gameButton3.setNr(3);
        gameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton4 = (GameButton) findViewById(R.id.gameButton4);
        gameButton4.setNr(4);
        gameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton5 = (GameButton) findViewById(R.id.gameButton5);
        gameButton5.setNr(5);
        gameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton6 = (GameButton) findViewById(R.id.gameButton6);
        gameButton6.setNr(6);
        gameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton7 = (GameButton) findViewById(R.id.gameButton7);
        gameButton7.setNr(7);
        gameButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //
        gameButton8 = (GameButton) findViewById(R.id.gameButton8);
        gameButton8.setNr(8);
        gameButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();
            }
        });
        //

        // init game
        GameButton.setSocketController(socketController);
        GameButton.setAllGameButtons(asList(gameButton0,gameButton1,gameButton2,gameButton3,
                gameButton4,gameButton5,gameButton6,gameButton7,gameButton8));
        GameButton.disableAllGameButtons();
    }


    //-------------------------------------------------------------------------
    // um das aufgepoppte keyboard verschwinden zu lassen
    public void hideKeyboard() {
       Util.hideSoftKeyboard(this);
    }


    //-------------------------------------------------------------------------
    // DISPLAY Methoden
    public void displayStatus(String text) {
        displayZeileStatus.setText(text);
    }
    public void displayCountdownPlayerO(String text) {
        if (socketController.getIsMyTurn()) {
            displayPlayerOcountdown.setText(text);
        }
    }
    public void displayCountdownPlayerX(String text) {
        if (socketController.getIsMyTurn()) {
            displayPlayerXcountdown.setText(text);
        }
    }
    public void clearCountDownDisplay() {
        displayPlayerXcountdown.setText("   ");
        displayPlayerOcountdown.setText("   ");
    }
    public void displayPlayers(String playerO, String playerX) {
        displayPlayerOname.setText(playerO);
        displayPlayerXname.setText(playerX);
    }
    public void displayStatistics(ArrayList<StatsItem> statsItems) {
        StatsAdapter statsAdapter = new StatsAdapter(this, statsItems);
        ListView listStats = (ListView) findViewById(R.id.list_stats);
        listStats.setAdapter(statsAdapter);
    }
    public void enableAllGameButtons(boolean toEnable) {
        if (toEnable) {
            GameButton.enableAllGameButtons();
        } else {
            GameButton.disableAllGameButtons();
        }
    }
    public void disableButtonOk() {
        buttonOk.setVisibility(View.INVISIBLE);
    }
    public void enableButtonOk() {
        buttonOk.setVisibility(View.VISIBLE);
    }
    public void disableEingabefeld() {
        editUserName.setVisibility(View.INVISIBLE);
    }
    public void enableEingabefeld() {
        editUserName.setVisibility(View.VISIBLE);
    }

    //-------------------------------------------------------------------------
    public void animateWinningFiedlds(int[] winningFields) {
        int wf1 = winningFields[0];
        int wf2 = winningFields[1];
        int wf3 = winningFields[2];
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        for (int i=0; i<10; i++) {
            if (i==0 && (i == wf1 || i == wf2 || i == wf3)) gameButton0.startAnimation(shake);
            if (i==1 && (i == wf1 || i == wf2 || i == wf3)) gameButton1.startAnimation(shake);
            if (i==2 && (i == wf1 || i == wf2 || i == wf3)) gameButton2.startAnimation(shake);
            if (i==3 && (i == wf1 || i == wf2 || i == wf3)) gameButton3.startAnimation(shake);
            if (i==4 && (i == wf1 || i == wf2 || i == wf3)) gameButton4.startAnimation(shake);
            if (i==5 && (i == wf1 || i == wf2 || i == wf3)) gameButton5.startAnimation(shake);
            if (i==6 && (i == wf1 || i == wf2 || i == wf3)) gameButton6.startAnimation(shake);
            if (i==7 && (i == wf1 || i == wf2 || i == wf3)) gameButton7.startAnimation(shake);
            if (i==8 && (i == wf1 || i == wf2 || i == wf3)) gameButton8.startAnimation(shake);
        }
    }
}
