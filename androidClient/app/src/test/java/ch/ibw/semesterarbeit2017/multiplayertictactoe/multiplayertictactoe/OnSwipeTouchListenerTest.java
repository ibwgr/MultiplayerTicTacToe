package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.widget.RelativeLayout;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by rk on 01.07.17.
 */
public class OnSwipeTouchListenerTest {

    @Test
    public void getNextTabRightOn1ShouldReturn2() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        //when(mockedMainActivity.getTabHost().getCurrentTab()).thenReturn(0);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(mockedMainActivity);
        assertEquals(onSwipeTouchListener.getNextTabRight(1), 2);
    }
    @Test
    public void getNextTabRightOn2ShouldReturn0() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        //when(mockedMainActivity.getTabHost().getCurrentTab()).thenReturn(0);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(mockedMainActivity);
        assertEquals(onSwipeTouchListener.getNextTabRight(2), 0);
    }

    @Test
    public void getNextTabLeftOn1ShouldReturn0() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        //when(mockedMainActivity.getTabHost().getCurrentTab()).thenReturn(0);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(mockedMainActivity);
        assertEquals(onSwipeTouchListener.getNextTabLeft(1), 0);
    }
    @Test
    public void getNextTabLeftOn0ShouldReturn2() throws Exception {
        final MainActivity mockedMainActivity = mock(MainActivity.class);
        //when(mockedMainActivity.getTabHost().getCurrentTab()).thenReturn(0);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(mockedMainActivity);
        assertEquals(onSwipeTouchListener.getNextTabLeft(0), 2);
    }
}