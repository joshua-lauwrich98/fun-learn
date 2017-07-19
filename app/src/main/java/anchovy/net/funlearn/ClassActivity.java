package anchovy.net.funlearn;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import anchovy.net.funlearn.fragments.AnnouncementFragment;

public class ClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String jenis, uid, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            }
        }

        Bundle args = getIntent().getExtras();
        if (args != null) {
            jenis = args.getString("JENIS");
            uid = args.getString("UID");
            title = args.getString("TITLE");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_activity_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.class_activity_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.class_activity_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (jenis.equals("student")) navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setVisible(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + title + "</font>")));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.class_activity_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (id == R.id.action_settings) {
            if(drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.class_announcement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();
        } else if (id == R.id.class_assignment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();
        } else if (id == R.id.class_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();
        } else if (id == R.id.class_make_question) {
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, new AnnouncementFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.class_activity_drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
