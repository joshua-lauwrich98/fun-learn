package anchovy.net.funlearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.Friend;
import anchovy.net.funlearn.other.FriendViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilFragmentFriendList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfilFragmentFriendList extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DatabaseReference databaseReference, filterDatabase;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private MaterialSearchView searchView;
    private List friendList;

    public ProfilFragmentFriendList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil_fragment_friend_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friend List").child(firebaseAuth.getCurrentUser().getUid());
        filterDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Friend List")
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .orderByChild("username").getRef();

        recyclerView = (RecyclerView)view.findViewById(R.id.main_activity_profile_fragment_friend_list_recycler_view);

        searchView = (MaterialSearchView) view.findViewById(R.id.main_activity_profile_fragment_search_friend_input);

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

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
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

//                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (model.getStatus().equals("online")) {
//                                        getFragmentManager().beginTransaction().replace(R.id.main_activity_student_frame_root, PlayFragmentMatchmakingWithFriend.newInstance(jenis, model.getUid())).commit();
//                                    }
//                                }
//                            });

                            viewHolder.setUsername(model.getUsername());
                            viewHolder.setIcon(model.getStatus());
                            viewHolder.setImage(getActivity().getApplicationContext(), model.getPhoto());
                        }
                    }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
