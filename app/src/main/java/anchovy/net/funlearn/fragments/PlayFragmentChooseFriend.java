package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.Friend;
import anchovy.net.funlearn.other.FriendViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragmentChooseFriend extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List friendList;
    private String jenis;

    public PlayFragmentChooseFriend() {
        // Required empty public constructor
    }

    public static PlayFragmentChooseFriend newInstance(String jenis) {
        Bundle args = new Bundle();
        args.putString("JENIS", jenis);
        PlayFragmentChooseFriend fragment = new PlayFragmentChooseFriend();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.jenis = getArguments().getString("JENIS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_fragment_choose_friend, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.main_activity_recycler_view_choose_friend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Statistic");

        final DatabaseReference friendListRef = FirebaseDatabase.getInstance().getReference().child("Friend List").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        friendListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                friendList = new ArrayList<>(td.values());
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

        FirebaseRecyclerAdapter<Friend, FriendViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FriendViewHolder>(
                Friend.class,
                R.layout.friend_list_card_view,
                FriendViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(FriendViewHolder viewHolder, final Friend model, int position) {

                if (friendList.contains(model.getUid())) {

                    viewHolder.time.setVisibility(View.VISIBLE);
                    viewHolder.username.setVisibility(View.VISIBLE);
                    viewHolder.myView.setVisibility(View.VISIBLE);
                    viewHolder.photo.setVisibility(View.VISIBLE);
                    viewHolder.status.setVisibility(View.VISIBLE);

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getStatus().equals("online")) {
                                getFragmentManager().beginTransaction().replace(R.id.main_activity_student_frame_root, PlayFragmentMatchmakingWithFriend.newInstance(jenis, model.getUid())).commit();
                            }
                        }
                    });

                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setIcon(model.getStatus());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getPhoto());
                }
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
