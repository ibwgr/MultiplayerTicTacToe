package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
        return "GameButton" + this.nr;
    }

    ///////////////////////////////
    public void clicked() {
        Log.w(PROG, "Button clicked: " + this.toString());
        this.setClicked();

        //Log.w(PROG, "Button tag: " + gb.getTag());
        //if (gb.getTag() == null) {

        this.setImageResource(R.drawable.game_fig_x);
        this.setTag(1);
        this.setClickable(false);
        Log.w(PROG, "nun gesperrt");
    }

}
