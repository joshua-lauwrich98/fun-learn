package anchovy.net.funlearn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.Locale;

import anchovy.net.funlearn.fragments.MainActivityContainerFragment;
import anchovy.net.funlearn.service.AddFriend;

public class MainActivityStudent extends AppCompatActivity {

    private int [] iconData;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private static final String THEME = "theme";
    private AppBarLayout barLayout;
    private int high;
    private int position;
    private String accType;

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

        Intent intent = new Intent(this, AddFriend.class);
        startService(intent);

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

        DatabaseReference request = FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("status").getValue() != null &&
                        dataSnapshot.child("name").getValue() != null &&
                                dataSnapshot.child("uid").getValue() != null && dataSnapshot.child("status").getValue().toString().equals("yes")) {
                    SharedPreferences.Editor a = getSharedPreferences("add_friend", MODE_PRIVATE).edit();
                    a.putString("name", dataSnapshot.child("name").getValue().toString());
                    a.putString("uid", dataSnapshot.child("uid").getValue().toString());
                    a.apply();
                } else {
                    SharedPreferences.Editor a = getSharedPreferences("add_friend", MODE_PRIVATE).edit();
                    a.putString("name", null);
                    a.putString("uid", null);
                    a.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acountType");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accType = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        SharedPreferences a = getSharedPreferences("add_friend", MODE_PRIVATE);
        String uid = a.getString("uid", null);
        String name = a.getString("name", null);
        if (uid != null && name != null) {
            DialogFragment dialog = new CustomDialogAddFriend().newInstance(name, uid);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), null);
        }

        super.onResume();
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
                    return MainActivityContainerFragment.newInstance("personal", accType);
                case 1 :
                    return MainActivityContainerFragment.newInstance("play", accType);
                case 2 :
                    return MainActivityContainerFragment.newInstance("class", accType);
                case 3 :
                    return MainActivityContainerFragment.newInstance("profil", accType);
                default :
                    return MainActivityContainerFragment.newInstance("personal", accType);
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

        FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
    }

    public static class CustomDialogAddFriend extends DialogFragment implements View.OnClickListener {

        private TextView name;
        private String opName;
        private String opUID;

        private static final String OP_NAME = "op_name";
        private static final String OP_UID = "op_uid";

        public static CustomDialogAddFriend newInstance(String opName, String opUID) {
            Bundle args = new Bundle();
            args.putString(OP_NAME, opName);
            args.putString(OP_UID, opUID);
            CustomDialogAddFriend fragment = new CustomDialogAddFriend();
            fragment.setCancelable(false);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                opName = getArguments().getString(OP_NAME);
                opUID = getArguments().getString(OP_UID);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_add_friend, container, false);

            Button yes = (Button) view.findViewById(R.id.main_activity_add_friend_ok_button);
            Button no = (Button) view.findViewById(R.id.main_activity_add_friend_cancel_button);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

            name = (TextView) view.findViewById(R.id.main_activity_add_friend_op_name);
            name.setText(opName);

            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_activity_add_friend_ok_button :
                    FirebaseDatabase.getInstance().getReference().child("Friend List")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(opUID);
                    FirebaseDatabase.getInstance().getReference().child("Friend List")
                            .child(opUID).push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Toast.makeText(getContext(), String.format(Locale.getDefault(), getResources().getString(R.string.dialog_fragment_add_friend_notif), opName), Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference().child("Request Friend").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().equalTo(opUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().setValue(null);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(null);
                    dismiss();
                    SharedPreferences.Editor sp = getContext().getSharedPreferences("add_friend_notif", MODE_PRIVATE).edit();
                    sp.putBoolean("status", true);
                    sp.apply();
                    break;

                case R.id.main_activity_add_friend_cancel_button :
                    FirebaseDatabase.getInstance().getReference().child("Request Friend").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().equalTo(opUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().setValue(null);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(null);
                    FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(null);
                    dismiss();
                    SharedPreferences.Editor sp2 = getContext().getSharedPreferences("add_friend_notif", MODE_PRIVATE).edit();
                    sp2.putBoolean("status", true);
                    sp2.apply();
                    break;
            }
        }
    }
}
