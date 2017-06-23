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

    //private String currentPlayer = "";





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

    private SocketController socketController = null; //new SocketController(getApplicationContext(), this);


//    // Nein, hier nicht disconnecten, wenn die App nur in den Hintergrund geht, man kann sie ja wieder hervorholen
//    @Override
//    protected void onStop() {
//        super.onStop();
//        socketController.disconnect();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketController.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketController = new SocketController(getApplicationContext(), this);
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
                    // username senden
                    JSONObject obj = new JSONObject();    // todo das sollte in den socketcontroller (wie im GameButton)
                    try {
                        obj.put("username", userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketController.send("add_user", obj);
                    displayZeileStatus.setText("Hello " +userName +"\n" + "...waiting for server...");
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
        // Temporaere Buttons, nur fuer Entwicklulng  // todo alles auskommentieren
        final Button buttonSimUnent = (Button) findViewById(R.id.button_sim_unent);
        buttonSimUnent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //{"winner":"draw","fields":[2,4,6],"username":"Emma","youWon":"no"}
                JSONObject obj = new JSONObject();
                try {
                    obj.put("winner", "draw");
                    obj.put("fields", "[2,4,6]");
                    obj.put("username", "Emma");
                    obj.put("youWon", "yes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socketController.onGameFinishedActionMethod(obj);
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

    } // end on-create lifecycle




    private void setUpGame() {
        // initializing
             
        // als erstes die GameButton Instanzen ermitteln
        //
        gameButton0 = (GameButton) findViewById(R.id.gameButton0);
        gameButton0.setNr(0);
        //gameButton0.setAlpha(0.5f);  // todo neue init methode (auch fuer nochmals-spielen)
        gameButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton1 = (GameButton) findViewById(R.id.gameButton1);
        gameButton1.setNr(1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton2 = (GameButton) findViewById(R.id.gameButton2);
        gameButton2.setNr(2);
        gameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton3 = (GameButton) findViewById(R.id.gameButton3);
        gameButton3.setNr(3);
        gameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton4 = (GameButton) findViewById(R.id.gameButton4);
        gameButton4.setNr(4);
        gameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton5 = (GameButton) findViewById(R.id.gameButton5);
        gameButton5.setNr(5);
        gameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton6 = (GameButton) findViewById(R.id.gameButton6);
        gameButton6.setNr(6);
        gameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton7 = (GameButton) findViewById(R.id.gameButton7);
        gameButton7.setNr(7);
        gameButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
            }
        });
        //
        gameButton8 = (GameButton) findViewById(R.id.gameButton8);
        gameButton8.setNr(8);
        gameButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameButton) v).clicked();;
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








    // DISPLAY Methoden
    public void displayStatus(String text) {
        displayZeileStatus.setText(text);
    }
    // DISPLAY Methoden
    public void displayPlayers(String text) {
        displayZeilePlayers.setText(text);
    }
    public void showWaitingImage(boolean toShow) {
        if (toShow) {
            waitingImage.setVisibility(View.VISIBLE);
        } else {
            waitingImage.setVisibility(View.INVISIBLE);
        }
    }
    public void enableAllGameButtons(boolean toEnable) {
        if (toEnable) {
            GameButton.enableAllGameButtons();
        } else {
            GameButton.disableAllGameButtons();
        }
    }
}
