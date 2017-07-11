package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link PlayFragmentMatchmakingWithFriend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragmentMatchmakingWithFriend extends Fragment implements View.OnClickListener {
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

    public PlayFragmentMatchmakingWithFriend() {
        // Required empty public constructor
    }

    public static PlayFragmentMatchmakingWithFriend newInstance(String mode, String jenis) {
        PlayFragmentMatchmakingWithFriend fragment = new PlayFragmentMatchmakingWithFriend();
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Temp2").child(jenis);

        databaseReference.setValue(firebaseAuth.getCurrentUser().getUid());

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
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equals("yes")) {

                        } else if (dataSnapshot.getValue().toString().equals("no")) {
                            Toast.makeText(getActivity(), "Your friend declined the invitation!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                if(currentTime < 15) {
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//
//                                String key = childSnapshot.getKey();
//                                String value = childSnapshot.getValue().toString();
//                                String uid = firebaseAuth.getCurrentUser().getUid();
//
//                                if (!key.equals(uid) && value.equals("yes")) {
//                                    opKey = key;
//                                    databaseReference.child(uid).setValue("no");
//                                    FirebaseDatabase.getInstance().getReference().child("Temp").child(key).setValue(key);
//                                    FirebaseDatabase.getInstance().getReference().child("Temp").child(uid).setValue("no");
//                                    timer.stop();
//                                    showDialog();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                    databaseReference.child(uid).setValue("yes");
//                    FirebaseDatabase.getInstance().getReference().child("Temp").orderByChild(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.getValue() != null && !dataSnapshot.getValue().toString().equals("no")) {
//                                opKey = dataSnapshot.getValue().toString();
//                                FirebaseDatabase.getInstance().getReference().child("Temp").child(uid).setValue("no");
//                                databaseReference.child(uid).setValue("no");
//                                timer.stop();
//                                showDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }

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
}
