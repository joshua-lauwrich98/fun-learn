package anchovy.net.funlearn.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.MainActivityStudent;
import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragmentMatchmaking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragmentMatchmaking extends Fragment implements View.OnClickListener {
    private static final String MODE = "mode";
    private static final String JENIS = "jenis";

    private String mode;
    private String jenis;
    private String opKey;

    private Chronometer timer;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private long currentTime;
    private boolean myAbandon, opAbandon;

    public PlayFragmentMatchmaking() {
        // Required empty public constructor
    }

    public static PlayFragmentMatchmaking newInstance(String mode, String jenis) {
        PlayFragmentMatchmaking fragment = new PlayFragmentMatchmaking();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        args.putString(JENIS, jenis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(MODE);
            jenis = getArguments().getString(JENIS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_matchmaking, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        if (mode.equals("klasik")) databaseReference = FirebaseDatabase.getInstance().getReference().child("Klasik");
        else databaseReference = FirebaseDatabase.getInstance().getReference().child("Time Trial");

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue("searching");

        Button cancel = (Button) view.findViewById(R.id.main_activity_student_play_fragment_matchmaking_cancel_button);
        cancel.setOnClickListener(this);

        timer = (Chronometer) view.findViewById(R.id.main_activity_student_play_fragment_matchmaking_chronometer);
        startTimer();

        opAbandon = false;
        myAbandon = false;

        return view;
    }

    private void startTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        final String uid = firebaseAuth.getCurrentUser().getUid();
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(final Chronometer chronometer) {
                currentTime = Integer.parseInt(chronometer.getText().toString().substring(3,5));
                if(currentTime < 15) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                String key = childSnapshot.getKey();
                                String value = childSnapshot.getValue().toString();
                                String uid = firebaseAuth.getCurrentUser().getUid();

                                if (!key.equals(uid) && value.equals("yes")) {
                                    opKey = key;
                                    databaseReference.child(uid).setValue("no");
                                    FirebaseDatabase.getInstance().getReference().child("Temp").child(key).setValue(key);
                                    FirebaseDatabase.getInstance().getReference().child("Temp").child(uid).setValue("no");
                                    timer.stop();
                                    showDialog();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    databaseReference.child(uid).setValue("yes");
                    FirebaseDatabase.getInstance().getReference().child("Temp").orderByChild(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null && !dataSnapshot.getValue().toString().equals("no")) {
                                opKey = dataSnapshot.getValue().toString();
                                FirebaseDatabase.getInstance().getReference().child("Temp").child(uid).setValue("no");
                                databaseReference.child(uid).setValue("no");
                                timer.stop();
                                showDialog();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        timer.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_activity_student_play_fragment_matchmaking_cancel_button) {
            timer.stop();
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(null);
            if (!getFragmentManager().popBackStackImmediate()) getActivity().onBackPressed();
            MainActivityStudent acti = (MainActivityStudent)getParentFragment().getActivity();
            acti.showAppBar();
        }
    }

    private void showDialog() {
        final android.support.v4.app.DialogFragment dialog = new DialogFragment();
        View view = dialog.getView();
        dialog.setCancelable(false);

        final TextView opStat = (TextView)view.findViewById(R.id.main_activity_student_dialog_matchmaking_opponent_ready);
        final TextView myStat = (TextView)view.findViewById(R.id.main_activity_student_dialog_matchmaking_self_ready);
        final TextView text = (TextView)view.findViewById(R.id.main_activity_student_dialog_matchmaking_timer_text);
        final Button ready = (Button)view.findViewById(R.id.main_activity_student_dialog_matchmaking_ready_button);
        final Button cancel = (Button)view.findViewById(R.id.main_activity_student_dialog_matchmaking_cancel_button);

        final String uid = firebaseAuth.getCurrentUser().getUid();

        final DatabaseReference opTemp = FirebaseDatabase.getInstance().getReference().child("Temp").child(opKey);
        final DatabaseReference myTemp = FirebaseDatabase.getInstance().getReference().child("Temp").child(uid);

        opTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals("yes")) {
                    opStat.setBackgroundResource(R.drawable.status_ready);
                } else if (dataSnapshot.getValue().equals("abandon")) {
                    opAbandon = true;
                    opStat.setBackgroundResource(R.drawable.status_not_ready);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStat.setBackgroundResource(R.drawable.status_ready);
                myTemp.setValue("yes");
                ready.setEnabled(false);
                cancel.setEnabled(false);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAbandon = true;
                myStat.setBackgroundResource(R.drawable.status_not_ready);
                myTemp.setValue("abandon");
                ready.setEnabled(false);
                cancel.setEnabled(false);
            }
        });

        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                text.setText(l/1000 + "");
            }

            @Override
            public void onFinish() {
                if (myAbandon) {
                    opTemp.setValue(null);
                    myTemp.setValue(null);
                    if (mode.equals("klasik")) {
                        FirebaseDatabase.getInstance().getReference().child("Klasik").child(uid).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Klasik").child(opKey).setValue(null);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Time Trial").child(uid).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Time Trial").child(opKey).setValue(null);
                    }

                    dialog.dismiss();
                    if (!getFragmentManager().popBackStackImmediate()) getActivity().onBackPressed();
                    MainActivityStudent acti = (MainActivityStudent)getParentFragment().getActivity();
                    acti.showAppBar();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.main_activity_student_frame_root, PlayFragment1.newInstance("abandon"))
                            .commit();
                }

                if (opAbandon) {
                    opTemp.setValue(null);
                    myTemp.setValue(null);
                    if (mode.equals("klasik")) {
                        FirebaseDatabase.getInstance().getReference().child("Klasik").child(uid).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Klasik").child(opKey).setValue(null);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Time Trial").child(uid).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Time Trial").child(opKey).setValue(null);
                    }

                    dialog.dismiss();
                    if (!getFragmentManager().popBackStackImmediate()) getActivity().onBackPressed();
                    MainActivityStudent acti = (MainActivityStudent)getParentFragment().getActivity();
                    acti.showAppBar();
                }

                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }.start();

        dialog.show(getChildFragmentManager(), "");
    }

    public static class DialogFragment extends android.support.v4.app.DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.custom_dialog_main_activity_student_matchmaking, container, false);
        }
    }
}
