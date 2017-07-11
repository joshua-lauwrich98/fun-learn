package anchovy.net.funlearn;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    private static final String THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = preference.getInt(THEME, 1);

        if (theme == 1){
            setTheme(R.style.FunLearnLightTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_play);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            }
        }

        Bundle extra = getIntent().getExtras();

        String opUID = extra.getString("opUID");
        String jenis = extra.getString("jenis");

        final TextView text = (TextView)findViewById(R.id.play_activity_countdown_text);

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long l) {
                if (l > 2000) {
                    text.setText((l / 1000) - 1 + "");
                } else {
                    text.setTextSize(100);
                    text.setText("START");
                }
            }

            @Override
            public void onFinish() {
                getFragmentManager().beginTransaction().replace(R.id.play_activity_container, new PVPKlasikPlayActiDefaultTempFragment()).commit();
            }
        }.start();

    }
}
