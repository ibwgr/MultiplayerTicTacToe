package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    @Override
    public String toString() {
        return "GameButton" + this.nr +" clicked:"+isClicked;
    }

    ///////////////////////////////
    public void clicked(String amZug) {
        Log.w(PROG, "Button clicked: " + this.toString());
        this.setClicked();
        if (amZug == Game.AMZUGICH) {
            this.setBackgroundResource(R.drawable.gf_o);
        } else {
            this.setBackgroundResource(R.drawable.gf_x);
        }
        //this.setTag(1);
        this.setClickable(false);
        Log.w(PROG, "nun gesperrt");
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
            System.out.println("..... gamebutton " +gamebutton);
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
}
