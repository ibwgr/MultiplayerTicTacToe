package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

/**
 * Created by dieterbiedermann on 01.07.17.
 */

public class StatsItem {
    //statsTimestamp +": " +statsP1 +" " +statsP2  +" ("+statsChange +")\n";

    private String timestamp;
    private String player1;
    private String player2;
    private String change;

    public StatsItem(String timestamp, String player1, String player2, String change) {
        this.timestamp = timestamp;
        this.player1 = player1;
        this.player2 = player2;
        this.change = change;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }
}
