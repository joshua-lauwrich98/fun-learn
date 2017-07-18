package anchovy.net.funlearn.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import anchovy.net.funlearn.MainActivityStudent;
import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment1 extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private LinearLayout withFriendLay, pvpLay;
    private Button pvp, withFriend, leaderboard;
    private TextView warning;
    private int x, y;
    private boolean pvpIsOpen, withIsOpen;

    private static final String STATUS = "status";
    private String status;

    public PlayFragment1() {
        // Required empty public constructor
    }

    public static PlayFragment1 newInstance(String status) {
        Bundle args = new Bundle();
        args.putString(STATUS, status);
        PlayFragment1 fragment = new PlayFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_fragment1, container, false);

        pvp = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_pvp_button);
        withFriend = (Button) view.findViewById(R.id.main_activity_student_play_fragment1_play_with_friend_button);
        leaderboard = (Button) view.findViewById(R.id.main_activity_student_play_fragment1_leaderboard_button);
        pvp.setOnClickListener(this);
        pvp.setOnTouchListener(this);
        withFriend.setOnClickListener(this);
        withFriend.setOnTouchListener(this);
        leaderboard.setOnClickListener(this);
        leaderboard.setOnTouchListener(this);

        Button pvpKlasik = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_pvp_klasik_button);
        Button pvpTime = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_pvp_timetrial_button);
        Button withFriendKlasik = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_with_friend_klasik_button);
        Button withFriendTime = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_with_friend_timetrial_button);
        pvpKlasik.setOnClickListener(this);
        pvpKlasik.setOnTouchListener(this);
        pvpTime.setOnClickListener(this);
        pvpTime.setOnTouchListener(this);
        withFriendKlasik.setOnClickListener(this);
        withFriendKlasik.setOnTouchListener(this);
        withFriendTime.setOnClickListener(this);
        withFriendTime.setOnTouchListener(this);

        withFriendLay = (LinearLayout) view.findViewById(R.id.main_activity_student_play_fragment1_with_friend_layout);
        pvpLay = (LinearLayout) view.findViewById(R.id.main_activity_student_play_fragment1_pvp_layout);

        ImageButton backWith = (ImageButton) view.findViewById(R.id.main_activity_student_play_fragment1_with_friend_back);
        backWith.setOnClickListener(this);
        ImageButton backPVP = (ImageButton) view.findViewById(R.id.main_activity_student_play_fragment1_pvp_back);
        backPVP.setOnClickListener(this);

        warning = (TextView) view.findViewById(R.id.main_activity_student_play_fragment1_warning);
        warning.setVisibility(View.GONE);

        pvpIsOpen = false;
        withIsOpen = false;

//        if (status != null) {
//            if (status.equals("abandon")) cantMatchmaking();
//        }

        return view;
    }

    private void cantMatchmaking() {
        warning.setVisibility(View.VISIBLE);
        pvp.setClickable(false);
        withFriend.setClickable(false);
        new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long l) {
                warning.setText(String.format(Locale.ENGLISH, getActivity().getResources().getString(R.string.main_activity_student_play2_warning), l/1000));
            }

            @Override
            public void onFinish() {
                warning.setVisibility(View.GONE);
                pvp.setClickable(true);
                withFriend.setClickable(true);
            }
        };
    }

    @Override
    public void onClick(View view) {
        MainActivityStudent acti = (MainActivityStudent)getActivity();
        switch (view.getId()) {
            case R.id.main_activity_student_play_fragment1_pvp_button :
                if (Build.VERSION.SDK_INT >= 21) {
                    if (withIsOpen) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            // get the center for the clipping circle
                            int cx = withFriend.getWidth() / 2;
                            int cy = withFriend.getHeight() / 2;

                            // get the final radius for the clipping circle
                            float finalRadius = (float) Math.hypot(cx, cy);

                            // create the animator for this view (the start radius is zero)
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(withFriendLay, cx, cy, finalRadius, 0);

                            // make the view visible and start the animation
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    withFriend.setVisibility(View.VISIBLE);
                                    withFriendLay.setVisibility(View.GONE);
                                }
                            });
                            anim.start();
                        } else {
                            Animation animation1 =
                                    AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                            withFriend.startAnimation(animation1);
                            withFriend.setVisibility(View.VISIBLE);
                            Animation animation2 =
                                    AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            withFriendLay.startAnimation(animation2);
                            withFriendLay.setVisibility(View.GONE);
                        }
                    }
                    // get the center for the clipping circle
                    int cx = pvpLay.getWidth() / 2;
                    int cy = pvpLay.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(pvpLay, x, y, 0, finalRadius);

                    // make the view visible and start the animation
                    pvpLay.setVisibility(View.VISIBLE);
                    pvp.setVisibility(View.GONE);
                    anim.start();

                } else {
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    pvpLay.startAnimation(animation1);
                    pvpLay.setVisibility(View.VISIBLE);
                    Animation animation2 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    pvp.startAnimation(animation2);
                    pvp.setVisibility(View.GONE);
                }
                withIsOpen = false;
                pvpIsOpen = true;
                break;
            case R.id.main_activity_student_play_fragment1_play_with_friend_button :
                if (Build.VERSION.SDK_INT >= 21) {
                    if(pvpIsOpen) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            // get the center for the clipping circle
                            int cx = pvp.getWidth() / 2;
                            int cy = pvp.getHeight() / 2;

                            // get the final radius for the clipping circle
                            float finalRadius = (float) Math.hypot(cx, cy);

                            // create the animator for this view (the start radius is zero)
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(pvpLay, cx, cy, finalRadius, 0);

                            // make the view visible and start the animation
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    pvp.setVisibility(View.VISIBLE);
                                    pvpLay.setVisibility(View.GONE);
                                }
                            });
                            anim.start();
                        } else {
                            Animation animation1 =
                                    AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                            pvp.startAnimation(animation1);
                            pvp.setVisibility(View.VISIBLE);
                            Animation animation2 =
                                    AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            pvpLay.startAnimation(animation2);
                            pvpLay.setVisibility(View.GONE);
                        }
                    }
                    // get the center for the clipping circle
                    int cx = withFriendLay.getWidth() / 2;
                    int cy = withFriendLay.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(withFriendLay, x, y, 0, finalRadius);

                    // make the view visible and start the animation
                    withFriendLay.setVisibility(View.VISIBLE);
                    withFriend.setVisibility(View.GONE);
                    anim.start();
                } else {
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    withFriendLay.startAnimation(animation1);
                    withFriendLay.setVisibility(View.VISIBLE);
                    Animation animation2 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    withFriend.startAnimation(animation2);
                    withFriend.setVisibility(View.GONE);
                }
                withIsOpen = true;
                pvpIsOpen = false;
                break;
            case R.id.main_activity_student_play_fragment1_with_friend_back :
                if (Build.VERSION.SDK_INT >= 21) {
                    // get the center for the clipping circle
                    int cx = withFriend.getWidth() / 2;
                    int cy = withFriend.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(withFriendLay, cx, cy, finalRadius, 0);

                    // make the view visible and start the animation
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            withFriend.setVisibility(View.VISIBLE);
                            withFriendLay.setVisibility(View.GONE);
                        }
                    });
                    anim.start();
                } else {
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    withFriend.startAnimation(animation1);
                    withFriend.setVisibility(View.VISIBLE);
                    Animation animation2 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    withFriendLay.startAnimation(animation2);
                    withFriendLay.setVisibility(View.GONE);
                }
                withIsOpen = false;
                break;
            case R.id.main_activity_student_play_fragment1_pvp_back :
                if (Build.VERSION.SDK_INT >= 21) {
                    // get the center for the clipping circle
                    int cx = pvp.getWidth() / 2;
                    int cy = pvp.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(pvpLay, cx, cy, finalRadius, 0);

                    // make the view visible and start the animation
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            pvp.setVisibility(View.VISIBLE);
                            pvpLay.setVisibility(View.GONE);
                        }
                    });
                    anim.start();
                } else {
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    pvp.startAnimation(animation1);
                    pvp.setVisibility(View.VISIBLE);
                    Animation animation2 =
                            AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    pvpLay.startAnimation(animation2);
                    pvpLay.setVisibility(View.GONE);
                }
                pvpIsOpen = false;
                break;
            case R.id.main_activity_student_play_fragment1_leaderboard_button :

                break;
            case R.id.main_activity_student_play_fragment1_with_friend_klasik_button :

                break;
            case R.id.main_activity_student_play_fragment1_with_friend_timetrial_button :
                acti.hideAppBar();
                break;
            case R.id.main_activity_student_play_fragment1_pvp_klasik_button :
                acti.hideAppBar();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_student_frame_root, PlayFragmentMatchmaking.newInstance("klasik", "pvp"))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.main_activity_student_play_fragment1_pvp_timetrial_button :
                acti.hideAppBar();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_student_frame_root, PlayFragmentMatchmaking.newInstance("time trial", "pvp"))
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        x = (int)motionEvent.getX();
        y = (int)motionEvent.getY();
        return false;
    }
}
