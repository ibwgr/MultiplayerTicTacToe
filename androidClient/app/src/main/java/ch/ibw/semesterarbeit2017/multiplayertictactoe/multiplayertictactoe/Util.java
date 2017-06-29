package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

/**
 * Created by rk on 28.06.17.
 */

public class Util {

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}
