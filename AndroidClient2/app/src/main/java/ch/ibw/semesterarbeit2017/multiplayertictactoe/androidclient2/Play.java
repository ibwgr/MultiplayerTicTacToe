package ch.ibw.semesterarbeit2017.multiplayertictactoe.androidclient2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.util.Arrays.asList;

/**
 * Created by rk on 29.06.17.
 */

public class Play extends Fragment{

    public static final String PROG = "____MAINACTIVITY(PLAY)";

    private EditText editUserName;
    private TextView displayZeileStatus;
    private TextView displayZeilePlayers;
    private TextView displayStatistics;
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

    //private String currentPlayer = "";
    private Button buttonOk;


    /*
    // Test Client http://lastminute.li/aaa/   (oder /ttt/)
    --------------------------------------------------------------------
    TODO background je nach groesse mit hdpi usw.
    TODO fixtexte translation ressource
    TODO gamebuttons optik
    TODO layout hoch/quer/groessen
    TODO layout grid vielleicht durch table ersetzen, wegen grid-lines
    TODO on username_validation
    TODO on connect_failed
    TODO on error
    TODO on stats_update
    TODO bei spielende anzeigen welche 3 buttons gewonnen haben
    --------------------------------------------------------------------
    */

    private SocketController socketController = null; //new SocketController(getApplicationContext(), this);


//    // Nein, hier nicht disconnecten, wenn die App nur in den Hintergrund geht, man kann sie ja wieder hervorholen
//    @Override
//    protected void onStop() {
//        super.onStop();
//        socketController.disconnect();
//    }

    //todo
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        socketController.disconnect();
//    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);
        //------------------------------------
        System.out.println("...............play!");


        socketController = new SocketController(this);
        socketController.connect();
        setUpInitalGame();

        // get the view elements
        editUserName = (EditText) rootView.findViewById(R.id.edit_username);
        displayZeileStatus = (TextView) rootView.findViewById(R.id.label_displayzeile);
        displayZeilePlayers = (TextView) rootView.findViewById(R.id.label_displayplayers);
        displayStatistics = (TextView) rootView.findViewById(R.id.label_statistics);

        // get the OK/Start button
        buttonOk = (Button) rootView.findViewById(R.id.button_ok);
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
                    Log.w(PROG, "Username (aus Feld): " + userName);
                    if (userName.length() > 0) {
                        //
                    } else {
                        Toast.makeText(getContext(), "Bitte zuerst einen Usernamen eingeben", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    userName = socketController.getMyName();
                }
                // wenn username eingegeben oder es ist ein restart nach spielelnde
                displayStatus("bitte warten...");
                disableButtonOk();
                disableEingabefeld();
                if (socketController.getGameStatus().equals(Status.STOPPED)) {
                    setUpReplayGame();
                }

                // username senden, egal ob erstes spiel oder restart, ist ein registrieren am server
                JSONObject obj = new JSONObject();    // todo das sollte in den socketcontroller (wie im GameButton)
                try {
                    obj.put("username", userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socketController.send("add_user", obj);
                displayStatus("Hello " + userName + "\n" + "...waiting for server...");

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////
        // socket listening
        // here we listen on message events from the server
        //////////////////////////////////////////////////////////////////////////////////////
        Log.i(PROG, "listening for socket messages from server");
        socketController.getSocket().on("start_game", socketController.onStartGame);
        socketController.getSocket().on("user_added", socketController.onUserAdded);
        socketController.getSocket().on("your_turn", socketController.onYourTurn);
        socketController.getSocket().on("other_turn", socketController.onOtherTurn);
        socketController.getSocket().on("new_move", socketController.onNewMove);  // Spielzug des Gegners
        socketController.getSocket().on("game_finished", socketController.onGameFinished);
        socketController.getSocket().on("disonnect", socketController.onDisconnectFromServer);  // disconnect from server received!
        socketController.getSocket().on("stats_update", socketController.onStatsUpdate);

        //------------------------------------
        return rootView;
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
        gameButton0 = (GameButton) rootView.findViewById(R.id.gameButton0);
        gameButton0.setNr(0);
        //gameButton0.setAlpha(0.5f);  // todo neue init methode (auch fuer nochmals-spielen)
        gameButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton1 = (GameButton) rootView.findViewById(R.id.gameButton1);
        gameButton1.setNr(1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton2 = (GameButton) rootView.findViewById(R.id.gameButton2);
        gameButton2.setNr(2);
        gameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton3 = (GameButton) rootView.findViewById(R.id.gameButton3);
        gameButton3.setNr(3);
        gameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton4 = (GameButton) rootView.findViewById(R.id.gameButton4);
        gameButton4.setNr(4);
        gameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton5 = (GameButton) rootView.findViewById(R.id.gameButton5);
        gameButton5.setNr(5);
        gameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton6 = (GameButton) rootView.findViewById(R.id.gameButton6);
        gameButton6.setNr(6);
        gameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton7 = (GameButton) rootView.findViewById(R.id.gameButton7);
        gameButton7.setNr(7);
        gameButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton8 = (GameButton) rootView.findViewById(R.id.gameButton8);
        gameButton8.setNr(8);
        gameButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //

        //
        waitingImage = (ImageView) rootView.findViewById(R.id.waiting_img);

        // init game
        GameButton.setSocketController(socketController);
        GameButton.setAllGameButtons(asList(gameButton0,gameButton1,gameButton2,gameButton3,
                gameButton4,gameButton5,gameButton6,gameButton7,gameButton8));
        GameButton.disableAllGameButtons();
    }








    // DISPLAY Methoden
    public void displayStatus(String text) {
        displayZeileStatus.setText(text);
    }
    // DISPLAY Methoden
    public void displayPlayers(String text) {
        displayZeilePlayers.setText(text);
    }
    public void displayStatistics(String text) {
        displayStatistics.setText(text);
    }
    public void showWaitingImage(boolean toShow) {
        if (toShow) {
            //waitingImage.setVisibility(View.VISIBLE);
        } else {
            //waitingImage.setVisibility(View.INVISIBLE);
        }
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

}
