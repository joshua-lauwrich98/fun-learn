package anchovy.net.funlearn;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anchovy.net.funlearn.fragments.AnnouncementFragment;
import anchovy.net.funlearn.fragments.ClassInfoFragment;
import anchovy.net.funlearn.fragments.CreateThreadFragment;
import anchovy.net.funlearn.fragments.HomeThreadFragment;

public class ClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateThreadFragment.DrawerLocked {

    private DrawerLayout drawer;
    private String jenis, uid, title;
    private FloatingActionButton fab;
    private boolean status, status2;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).commit();

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + title + "</font>")));

        fab = (FloatingActionButton)findViewById(R.id.class_activity_add_floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, CreateThreadFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
                status = true;
                fab.setVisibility(View.GONE);
            }
        });

        status = false;
        status2 = true;

        FirebaseDatabase.getInstance().getReference().child("Class List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(uid)) {
                    Toast.makeText(ClassActivity.this, getResources().getString(R.string.disband_class_announcement), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.class_activity_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (status) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getResources().getString(R.string.class_activity_dialog_create_post_back_title))
                        .setMessage(getResources().getString(R.string.class_activity_dialog_create_post_back_content))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                while(getSupportFragmentManager().getBackStackEntryCount() != 1) {
                                    getSupportFragmentManager().popBackStackImmediate();
                                }
                                status = false;
                                status2 = true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).commit();
                                fab.setVisibility(View.VISIBLE);
                                getSupportFragmentManager().popBackStackImmediate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

//                super.onBackPressed();
                return;
            } else {
                if (!status2) {
                    while(getSupportFragmentManager().getBackStackEntryCount() != 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    status = false;
                    status2 = true;
                    getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
                    fab.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStackImmediate();
                    return;
                }
            }
            super.onBackPressed();
            if (fab.getVisibility() == View.GONE) {
                fab.setVisibility(View.VISIBLE);
            }
            try {
                if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() - 1) instanceof CreateThreadFragment) {
                    fab.setVisibility(View.GONE);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                fab.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }

    public void isShow (boolean status) {
        if (status) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
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
            if(drawer.isDrawerOpen(Gravity.END)) {
                drawer.closeDrawer(Gravity.END);
            } else {
                drawer.openDrawer(Gravity.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.class_announcement) {
            fab.setVisibility(View.VISIBLE);
            status = false;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        } else if (id == R.id.class_assignment) {
            fab.setVisibility(View.VISIBLE);
            status = false;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        } else if (id == R.id.class_home) {
            fab.setVisibility(View.VISIBLE);
            status = false;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, HomeThreadFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        } else if (id == R.id.class_make_question) {
            fab.setVisibility(View.GONE);
            status = true;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, CreateThreadFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        } else if (id == R.id.class_info) {
            fab.setVisibility(View.VISIBLE);
            status = false;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, ClassInfoFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        } else if (id == R.id.class_disband) {
            DialogFragment dialogFragment = CustomDialogDisbandClass.newInstance(uid);
            dialogFragment.setCancelable(false);
            dialogFragment.show(getSupportFragmentManager(), null);
        } else {
            fab.setVisibility(View.VISIBLE);
            status = false;
            status2 = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.class_activity_container, AnnouncementFragment.newInstance(jenis, uid)).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.class_activity_drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void setDrawerLocked(boolean shouldLock) {
        if(shouldLock){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else{
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
        this.status2 = status;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
    }

    public static class CustomDialogDisbandClass extends DialogFragment {

        private static final String UID = "uid";
        private String uid;

        public static CustomDialogDisbandClass newInstance(String uid) {

            Bundle args = new Bundle();
            args.putString(UID, uid);
            CustomDialogDisbandClass fragment = new CustomDialogDisbandClass();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                uid = getArguments().getString(UID);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_fragment_disband_class_confirm, container, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            Button ok = (Button) view.findViewById(R.id.dialog_fragment_disband_class_ok_button);
            Button cancel = (Button) view.findViewById(R.id.dialog_fragment_disband_class_cancel_button);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ref  = FirebaseDatabase.getInstance().getReference().child("Class List").child(uid);
                    ref.child("member").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Map<String, Object>> objectMap = (HashMap<String, Map<String, Object>>) dataSnapshot.getValue();
                            if (objectMap != null) {
                                List<String> td = new ArrayList<>(objectMap.keySet());
                                for (int i = 0; i < td.size(); i++) {
                                    FirebaseDatabase.getInstance().getReference().child("Class").child(td.get(i)).child(uid).setValue(null);
                                    FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).child("member").child(td.get(i)).setValue(null);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild("member")) {
                                FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).setValue(null);
//                                FirebaseDatabase.getInstance().getReference().child("Class").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        .child(uid).setValue(null);
                                try {
                                    getActivity().finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            return view;
        }
    }
}
