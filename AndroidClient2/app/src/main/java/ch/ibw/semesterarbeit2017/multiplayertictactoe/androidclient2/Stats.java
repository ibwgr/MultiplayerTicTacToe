package ch.ibw.semesterarbeit2017.multiplayertictactoe.androidclient2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rk on 29.06.17.
 */

public class Stats extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        //------------------------------------
        System.out.println("...............stats!");

        TextView test = (TextView) rootView.findViewById(R.id.section_test);
        test.setText("halli hallo");


        //------------------------------------
        return rootView;
    }








}
