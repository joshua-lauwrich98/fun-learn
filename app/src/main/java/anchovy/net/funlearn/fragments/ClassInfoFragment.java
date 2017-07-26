package anchovy.net.funlearn.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.ClassMember;
import anchovy.net.funlearn.other.ClassMemberViewHolder;

public class ClassInfoFragment extends Fragment {

    private static final String JENIS = "jenis";
    private static final String UID = "uid";

    private String jenis, uid;
    private boolean isShow;
    private RecyclerView recyclerView;
    private TextView created, post, member, title;

    public ClassInfoFragment() {
        // Required empty public constructor
    }

    public static ClassInfoFragment newInstance(String jenis, String uid) {
        ClassInfoFragment fragment = new ClassInfoFragment();
        Bundle args = new Bundle();
        args.putString(JENIS, jenis);
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jenis = getArguments().getString(JENIS);
            uid = getArguments().getString(UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_info, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.class_activity_class_info_member_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        isShow = false;

        title = (TextView) view.findViewById(R.id.class_activity_class_info_class_title);
        created = (TextView) view.findViewById(R.id.class_activity_class_info_created_date_output);
        post = (TextView) view.findViewById(R.id.class_activity_class_info_question_total_output);
        member = (TextView) view.findViewById(R.id.class_activity_class_info_member_total_output);

        ImageButton exit = (ImageButton) view.findViewById(R.id.class_activity_class_info_member_list_leave_class);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(getResources().getString(R.string.class_activity_dialog_leave_class_back_title))
                        .setMessage(getResources().getString(R.string.class_activity_dialog_leave_class_back_content))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String uidUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("Class List").child(uid).child("member").child(uidUser).setValue(null);
                                ref.child("Class").child(uidUser).child(uid).setValue(null);

                                ref.child("Class List").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int currMember = Integer.parseInt(dataSnapshot.child("curr").getValue().toString())-1;
                                        if (currMember != 0) {
                                            ref.child("Class List").child(uid).child("curr").setValue(Integer.toString(currMember));
                                        } else {
                                            ref.child("Class List").child(uid).setValue(null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                getActivity().finish();
;                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        final Button show = (Button) view.findViewById(R.id.class_activity_class_info_show_member_button);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShow) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    show.setText(getResources().getString(R.string.class_activity_class_info_fragment_show_member));
                    isShow = false;
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    show.setText(getResources().getString(R.string.class_activity_class_info_fragment_hide_member));
                    isShow = true;
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int curr = Integer.parseInt(dataSnapshot.child("curr").getValue().toString());
                int max = Integer.parseInt(dataSnapshot.child("max").getValue().toString());
                String memberText = curr + " / " + max;

                Map<String, Object> data = (HashMap<String,Object>) dataSnapshot.child("post").getValue();
                int totalPost = 0;
                if (data != null) totalPost= data.size();

                title.setText(dataSnapshot.child("name").getValue().toString());
                created.setText(dataSnapshot.child("created").getValue().toString());
                post.setText(Integer.toString(totalPost));
                member.setText(memberText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference().child("Class List")
                .child(uid).child("member").orderByChild("fullname");
        FirebaseRecyclerAdapter<ClassMember, ClassMemberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClassMember, ClassMemberViewHolder>(
                ClassMember.class,
                R.layout.class_member_list_card_view,
                ClassMemberViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ClassMemberViewHolder viewHolder, final ClassMember model, int position) {
                viewHolder.setPhoto(getContext(), model.getPhoto());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setFullname(model.getFullname());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("Statistic").child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String username, fullname, photo;
                                int game, win, lose, bestPvp, bestTimeTrial, totalEx, totalLearn;

                                username = dataSnapshot.child("username").getValue().toString();
                                fullname = dataSnapshot.child("fullname").getValue().toString();
                                photo = dataSnapshot.child("photo").getValue().toString();

                                game = Integer.parseInt(dataSnapshot.child("totalGames").getValue().toString());
                                win = Integer.parseInt(dataSnapshot.child("totalWin").getValue().toString());
                                lose = Integer.parseInt(dataSnapshot.child("totalLose").getValue().toString());
                                bestPvp = Integer.parseInt(dataSnapshot.child("pvpBestScore").getValue().toString());
                                bestTimeTrial = Integer.parseInt(dataSnapshot.child("timeTrialBestScore").getValue().toString());
                                totalEx = Integer.parseInt(dataSnapshot.child("totalExercise").getValue().toString());
                                totalLearn = Integer.parseInt(dataSnapshot.child("totalLearn").getValue().toString());

                                DialogFragment dialogFragment = ProfilFragmentFriendList.CustomDialogFriendListDetail.newInstance(
                                        username, fullname, photo, game, win, lose, bestPvp, bestTimeTrial, totalEx, totalLearn
                                );
                                dialogFragment.show(getActivity().getSupportFragmentManager(), null);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
