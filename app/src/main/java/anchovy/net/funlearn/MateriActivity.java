package anchovy.net.funlearn;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.List;
import java.util.Map;

import anchovy.net.funlearn.other.FragmentNavigationManagerMateri;
import anchovy.net.funlearn.other.MateriNavigationManager;

public class MateriActivity extends AppCompatActivity {

    private static final String THEME = "theme";
    private static final String JENJANG = "jenjang";
    private static final String PELAJARAN = "pelajaran";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private MateriNavigationManager mNavigationManager;
    private Map<String, List<String>> mExpandableListData;
    private int jenjang, pelajaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = Integer.parseInt(preference.getString(THEME, "1"));

        if (theme == 1){
            setTheme(R.style.FunLearnLightTheme);
        } else {
            setTheme(R.style.FunLearnDarkTheme);
        }

        super.onCreate(savedInstanceState);

        if (theme == 1) {
            if (Build.VERSION.SDK_INT >= 23) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar2, getTheme()));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar2));
                }
            }
        }

        setContentView(R.layout.activity_materi);

        Bundle args = getIntent().getExtras();
        jenjang = args.getInt(JENJANG);
        pelajaran = args.getInt(PELAJARAN);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.materi_activity_drawer_layout);
        mActivityTitle = getTitle().toString();

        mExpandableListView = (ExpandableListView) findViewById(R.id.materi_activity_expandable_list_view);
        mNavigationManager = FragmentNavigationManagerMateri.obtain(this);

        initItems();


    }

    private void initItems() {
        switch (jenjang) {
            case 0 :
                items = getResources().getStringArray(R.array.sd_class_expandable_drawer);
                break;
            case 1 :
                items = getResources().getStringArray(R.array.smp_class_expandable_drawer);
                break;
            case 2 :
                items = getResources().getStringArray(R.array.sma_class_expandable_drawer);
                break;
        }
    }
}
