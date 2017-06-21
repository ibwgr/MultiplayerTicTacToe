package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

/**
 * Created by rk on 21.06.17.
 */

public class GameInfo {
    String myName;
    Boolean myTurn;
    Boolean othersTurn;
    int gameStatus; // 0 1  todo enum
    Boolean iHaveWon;
    Boolean otherHasWon;


    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public Boolean getMyTurn() {
        return myTurn;
    }

    public void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Boolean getOthersTurn() {
        return othersTurn;
    }

    public void setOthersTurn(Boolean othersTurn) {
        this.othersTurn = othersTurn;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Boolean getiHaveWon() {
        return iHaveWon;
    }

    public void setiHaveWon(Boolean iHaveWon) {
        this.iHaveWon = iHaveWon;
    }

    public Boolean getOtherHasWon() {
        return otherHasWon;
    }

    public void setOtherHasWon(Boolean otherHasWon) {
        this.otherHasWon = otherHasWon;
    }
}
