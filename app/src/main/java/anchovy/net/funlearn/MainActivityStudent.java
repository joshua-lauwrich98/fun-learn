package anchovy.net.funlearn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import anchovy.net.funlearn.fragments.MainActivityContainerFragment;

public class MainActivityStudent extends AppCompatActivity {

    private int [] iconData;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private static final String THEME = "theme";
    private AppBarLayout barLayout;
    private int high;
    private int position;

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

        setContentView(R.layout.activity_main_student);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            }
        }

        iconData = new int[]{R.drawable.ic_assignment_black_24dp, R.drawable.ic_videogame_asset_black_24dp,
            R.drawable.ic_school_black_24dp, R.drawable.ic_account_box_black_24dp};

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.main_activity_view_pager_container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(position);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_activity_tabs);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                position = tab.getPosition();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(iconData[i]);
        }

        barLayout = (AppBarLayout)findViewById(R.id.main_activity_app_bar);
    }

    public void hideAppBar () {
        high = barLayout.getHeight();
        barLayout.animate().translationY(barLayout.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        barLayout.setVisibility(View.GONE);
                    }
                }).start();
    }

    public void showAppBar () {
        barLayout.animate().translationY(barLayout.getHeight() - barLayout.getMeasuredHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        barLayout.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0 :
                    return MainActivityContainerFragment.newInstance("personal");
                case 1 :
                    return MainActivityContainerFragment.newInstance("play");
                case 2 :
                    return MainActivityContainerFragment.newInstance("class");
                case 3 :
                    return MainActivityContainerFragment.newInstance("profil");
                default :
                    return MainActivityContainerFragment.newInstance("personal");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.main_activity_student_tab_title1);
                case 1:
                    return getResources().getString(R.string.main_activity_student_tab_title2);
                case 2:
                    return getResources().getString(R.string.main_activity_student_tab_title3);
                case 3:
                    return getResources().getString(R.string.main_activity_student_tab_title4);
            }
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference().child("Statistic").child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseDatabase.getInstance().getReference().child("Statistic").child("status").setValue("offline");
    }
}
