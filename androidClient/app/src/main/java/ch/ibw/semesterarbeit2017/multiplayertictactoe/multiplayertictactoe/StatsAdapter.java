package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dieterbiedermann on 01.07.17.
 */

public class StatsAdapter extends ArrayAdapter<StatsItem> {

    public StatsAdapter(Context context, ArrayList<StatsItem> statsItems) {
        super(context, 0, statsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StatsItem statsItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stats_item, parent, false);
        }

        TextView statsTimestamp = (TextView) convertView.findViewById(R.id.stats_timestamp);
        TextView statsPlayer1 = (TextView) convertView.findViewById(R.id.stats_player1);
        TextView statsPlayer2 = (TextView) convertView.findViewById(R.id.stats_player2);
        TextView statsChange = (TextView) convertView.findViewById(R.id.stats_change);

        statsTimestamp.setText(statsItem.getTimestamp());
        statsPlayer1.setText(statsItem.getPlayer1());
        statsPlayer2.setText(statsItem.getPlayer2());
        statsChange.setText(statsItem.getChange());

        // Return the completed view to render on screen
        return convertView;
    }

}