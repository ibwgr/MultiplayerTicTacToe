package semesterarbeit2017.nds.ibw.ch.multiplayertictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PROG = "____MAIN";

    private EditText editUserName;
    private TextView displayZeile;

    private ImageButton gameButton1;
    private ImageButton gameButton2;
    private ImageButton gameButton3;
    private ImageButton gameButton4;
    private ImageButton gameButton5;
    private ImageButton gameButton6;
    private ImageButton gameButton7;
    private ImageButton gameButton8;
    private ImageButton gameButton9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the values from fields
        editUserName = (EditText) findViewById(R.id.edit_username);
        displayZeile = (TextView) findViewById(R.id.label_displayzeile);

        //
        gameButton1 = (ImageButton) findViewById(R.id.gameButton1);
        gameButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // welcher button bin ich
                ImageButton imageButton = (ImageButton) v;
                Log.w(PROG, "Button clicked: " + imageButton.toString());
                Log.w(PROG, "Button tag: " + imageButton.getTag());
                if (imageButton.getTag() == null) {
                    // ist noch frei
                    imageButton.setImageResource(R.drawable.game_fig_x);
                    imageButton.setTag(1);
                    imageButton.setClickable(false);
                    Log.w(PROG, "Button tag: " + imageButton.getTag());
                }
            }
        });

        // get the OK button
        final Button buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userName = (editUserName.getText().toString());
                Log.w(PROG, "Username (aus Feld): " + userName);
                if (userName.length()>0) {
                    // TODO Username anzeigen
                    displayZeile.setText("Hallo " +userName );
                    // TODO Eingabefeld und Button disalbe
                    buttonOk.setVisibility(View.INVISIBLE);
                    editUserName.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Bitte zuerst einen Usernamen eingeben", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
