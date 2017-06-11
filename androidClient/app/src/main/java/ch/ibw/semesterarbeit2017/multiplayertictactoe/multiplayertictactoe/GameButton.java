package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rk on 03.06.17.
 */

public class GameButton extends ImageButton {

    public static final String PROG = "____GAMEBUTTON";

    private int nr;
    private boolean isClicked = false;  //per default nicht gelickt
    private boolean isX;
    private boolean isO;


    public GameButton(Context context) {
        super(context);
    }

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getNr() {
        return nr;
    }
    public String getNrFieldId() {
        return Const.FIELD_PREFIX+getNr();   //field0, field1, ...
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked() {
        isClicked = true;
    }

    public boolean isX() {
        return isX;
    }

    public void setX(boolean x) {
        isX = x;
    }

    public boolean isO() {
        return isO;
    }

    public void setO(boolean o) {
        isO = o;
    }

    ///////////////////////////////
    public void setGraphicO(){
        this.setBackgroundResource(R.drawable.game_fig_o);
    }
    public void setGraphicX(){
        this.setBackgroundResource(R.drawable.game_fig_x);
    }
    public void setGraphicInit(){
        this.setBackgroundResource(R.drawable.gf_init);
    }

    ///////////////////////////////

    @Override
    public String toString() {
        return "GameButton" + this.nr +" clicked:"+isClicked;
    }

    ///////////////////////////////
    public void clicked(String currentPlayerToken) {
        Log.w(PROG, "Button clicked: " + this.toString() +" , playerToken:"+currentPlayerToken);
        if (currentPlayerToken.equals(Const.PLAYER_TOKEN_O)) {
            setGraphicO();
        } else {
            setGraphicX();
        }
        this.setClicked();
        // dieser darf waehrend des spiels nie mehr geklicked werden
        this.setClickable(false);
        // spielzug beendet
        disableAllGameButtons();
        //
        // dem server den spielzug mitteilen
        // username senden
        JSONObject obj = new JSONObject();
        try {
            obj.put("player", currentPlayerToken);
            obj.put("field", getNrFieldId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketController.send("player_action", obj);  // mein Spielzug
        Log.w(PROG, "spielzug beendet, feld:"+this.getNrFieldId() +", playerToken:"+currentPlayerToken);
    }
    ///////////////////////////////
    public void clickedByOther(String currentPlayerToken) {
        Log.w(PROG, "Button clicked by other: " + this.toString() +" , playerToken:"+currentPlayerToken);
        if (currentPlayerToken.equals(Const.PLAYER_TOKEN_O)) {
            setGraphicO();
        } else {
            setGraphicX();
        }
        this.setClicked();
        // dieser darf waehrend des spiels nie mehr geklicked werden
        this.setClickable(false);
    }


    //////////////////////////////////////////////////
    private static SocketController socketController;

    public static SocketController getSocketController() {
        return socketController;
    }

    public static void setSocketController(SocketController socketController) {
        GameButton.socketController = socketController;
    }

    //////////////////////////////////////////////////
    private static List<GameButton> allGameButtons;

    public static List<GameButton> getAllGameButtons() {
        return allGameButtons;
    }

    public static void setAllGameButtons(List<GameButton> allGameButtons) {
        GameButton.allGameButtons = allGameButtons;
        System.out.println("........ liste: " +allGameButtons.size());
    }

    public static void enableAllGameButtons(){
        for (GameButton gamebutton : allGameButtons){
            // only enable if button is unclicked
            //System.out.println("..... gamebutton " +gamebutton);
            if (!gamebutton.isClicked()) {
                gamebutton.setEnabled(true);
            }
        }
    }

    public static void disableAllGameButtons() {
        for (GameButton gamebutton : allGameButtons){
            if (gamebutton.isEnabled()) {
                gamebutton.setEnabled(false);
            }
        }
    }

    public static GameButton findGameButtonByFieldId(String fieldId) {
        Log.w(PROG, "......findGameButtonByFieldId: " +fieldId);
        for (GameButton gameButton : allGameButtons) {
            if (gameButton.getNrFieldId().equals(fieldId)) {
                return gameButton;
            }
        }
        return null;
    }
}
