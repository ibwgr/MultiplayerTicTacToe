package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>

 * Created by rk on 11.06.17.
 */
public class SocketControllerTest {

    // todo onGameFinishedActionMethod


    @Test
    public void MOCKITO_onOtherTurnActionMethod_shouldSet_OthersTurnAndDisable() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"time":30,"player":"o","username":"vulkan"}
        try {
            obj.put("time", "30");
            obj.put("player", "o");
            obj.put("username", "vulkan");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onOtherTurnActionMethod(obj);
        assert(!socketController.getIsMyTurn());
        assert(socketController.getIsOthersTurn());
        assert(!socketController.getIsAllButtonsEnabled());
    }

    @Test
    public void MOCKITO_onYourTurnActionMethod_shouldSet_MyTurnAndEnable() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"time":30,"player":"x","username":"Emma"}
        try {
            obj.put("time", "30");
            obj.put("player", "x");
            obj.put("username", "Emma");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onYourTurnActionMethod(obj);
        assert(socketController.getIsMyTurn());
        assert(!socketController.getIsOthersTurn());
        assert(socketController.getIsAllButtonsEnabled());
    }

    @Test
    public void MOCKITO_onGameFinishedActionMethod_whenReceivedIwon_shouldSet_IhaveWon() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        try {
            obj.put("winner", "hans");
            obj.put("fields", "[2,4,6]");
            obj.put("username", "Emma");
            obj.put("youWon", "yes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onGameFinishedActionMethod(obj);
        assert(socketController.getIsIhaveWon());
        assert(!socketController.getIsOtherHasWon());
    }

    @Test
    public void MOCKITO_onGameFinishedActionMethod_whenReceivedIlost_shouldSet_IhaveLost() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"winner":"hans","fields":[2,4,6],"username":"Emma","youWon":"no"}
        try {
            obj.put("winner", "hans");
            obj.put("fields", "[2,4,6]");
            obj.put("username", "Emma");
            obj.put("youWon", "no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onGameFinishedActionMethod(obj);
        assert(!socketController.getIsIhaveWon());
        assert(socketController.getIsOtherHasWon());
    }

    @Test
    public void MOCKITO_onStartGameActionMethod_shouldSet_Player1Player2andGameRunning() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        doNothing().when(mockedMainActivity).displayStatus(anyString());
        doNothing().when(mockedMainActivity).displayPlayers(anyString());
        SocketController socketController = new SocketController(null, mockedMainActivity);
        JSONObject obj = new JSONObject();  //{"player1":"Emma","player2":"fritzli"}
        try {
            obj.put("player1", "Emma");
            obj.put("player2", "Fritzli");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Test JSON: " + obj.toString());
        socketController.onStartGameActionMethod(obj);
        assertEquals("Emma",socketController.getPlayer1Name());
        assertEquals("Fritzli",socketController.getPlayer2Name());
        assertEquals(Status.RUNNING,socketController.getGameStatus());
    }

}