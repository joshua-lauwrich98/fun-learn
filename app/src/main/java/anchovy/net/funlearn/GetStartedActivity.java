package anchovy.net.funlearn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.fragments.GetStartedPageFragment;

public class GetStartedActivity extends AppCompatActivity {

    private SectionPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final String THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = preference.getInt(THEME, 1);

        if (theme == 1) setTheme(R.style.FunLearnLightTheme);

        setContentView(R.layout.activity_get_started);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            }
        }

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    final String uid = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(uid)) {
                                Intent main = new Intent(GetStartedActivity.this, MainActivityStudent.class);
                                startActivity(main);
                                GetStartedActivity.this.finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.get_started_activity_view_pager);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GetStartedPageFragment.newInstance(1);
                case 1:
                    return GetStartedPageFragment.newInstance(2);
                case 2:
                    return GetStartedPageFragment.newInstance(3);
                default:
                    return GetStartedPageFragment.newInstance(1);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
