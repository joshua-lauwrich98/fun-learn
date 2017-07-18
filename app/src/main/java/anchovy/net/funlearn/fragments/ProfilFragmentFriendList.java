package anchovy.net.funlearn.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.Friend;
import anchovy.net.funlearn.other.FriendViewHolder;

public class ProfilFragmentFriendList extends Fragment implements View.OnClickListener {

//    private OnFragmentInteractionListener mListener;

    private DatabaseReference databaseReference, filterDatabase, friendListRef;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List friendList;
    private ConstraintLayout layout;
    private ImageButton add;

    public ProfilFragmentFriendList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil_fragment_friend_list, container, false);

        layout = (ConstraintLayout) view.findViewById(R.id.layout111);
        layout.requestFocus();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Statistic");
        databaseReference.keepSynced(true);
        filterDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Statistic");
        filterDatabase.keepSynced(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.main_activity_profile_fragment_friend_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = (SearchView) view.findViewById(R.id.main_activity_profile_fragment_search_friend_input);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();

        friendListRef = FirebaseDatabase.getInstance().getReference().child("Friend List").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        friendListRef.keepSynced(true);

        friendListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                if (td != null) {
                    friendList = new ArrayList<>(td.values());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add = (ImageButton) view.findViewById(R.id.main_activity_profile_fragment_add_friend_button);
        add.setOnClickListener(this);

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
                if (friendList != null && friendList.contains(model.getUid())) {

                    viewHolder.time.setVisibility(View.VISIBLE);
                    viewHolder.username.setVisibility(View.VISIBLE);
                    viewHolder.myView.setVisibility(View.VISIBLE);
                    viewHolder.photo.setVisibility(View.VISIBLE);
                    viewHolder.status.setVisibility(View.VISIBLE);

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            if (model.getStatus().equals("online")) {
                                DialogFragment dialog = CustomDialogFriendListDetail.newInstance(
                                        model.getUsername(), model.getFullname(), model.getPhoto(),
                                        model.getTotalGames(), model.getTotalWin(), model.getTotalLose(),
                                        model.getPvpBestScore(), model.getTimeTrialBestScore(), model.getTotalExercise(),
                                        model.getTotalLearn()
                                );
                                dialog.show(getParentFragment().getActivity().getSupportFragmentManager(), null);
//                            }
                        }
                    });

                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setIcon(model.getStatus());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getPhoto());
                }
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
//                filterDatabase.child(firebaseAuth.getCurrentUser().getUid())
//                        .orderByChild("username").equalTo(newText).getRef();

                FirebaseRecyclerAdapter<Friend, FriendViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FriendViewHolder>(
                        Friend.class,
                        R.layout.friend_list_card_view,
                        FriendViewHolder.class,
                        filterDatabase
                ) {
                    @Override
                    protected void populateViewHolder(FriendViewHolder viewHolder, final Friend model, int position) {

                        if (friendList != null && friendList.contains(model.getUid()) && (model.getUsername().contains(newText)||newText.isEmpty())) {

                            viewHolder.time.setVisibility(View.VISIBLE);
                            viewHolder.username.setVisibility(View.VISIBLE);
                            viewHolder.myView.setVisibility(View.VISIBLE);
                            viewHolder.photo.setVisibility(View.VISIBLE);
                            viewHolder.status.setVisibility(View.VISIBLE);

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    if (model.getStatus().equals("online")) {
                                        DialogFragment dialog = CustomDialogFriendListDetail.newInstance(
                                                model.getUsername(), model.getFullname(), model.getPhoto(),
                                                model.getTotalGames(), model.getTotalWin(), model.getTotalLose(),
                                                model.getPvpBestScore(), model.getTimeTrialBestScore(), model.getTotalExercise(),
                                                model.getTotalLearn()
                                        );
                                        dialog.show(getParentFragment().getActivity().getSupportFragmentManager(), null);
//                                    }
                                }
                            });

                            viewHolder.setUsername(model.getUsername());
                            viewHolder.setIcon(model.getStatus());
                            viewHolder.setImage(getActivity().getApplicationContext(), model.getPhoto());
                        }
                    }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);

//                if (newText.isEmpty()) {
//                    layout.requestFocus();
//                    searchView.clearFocus();
//                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_activity_profile_fragment_add_friend_button) {
            DialogFragment dialog = new CustomDialogAddFriend();
            dialog.setCancelable(false);
            dialog.show(getParentFragment().getActivity().getSupportFragmentManager(), null);
        }
    }

    public static class CustomDialogFriendListDetail extends DialogFragment {

        private static final String USERNAME = "username";
        private static final String FULLNAME = "fullname";
        private static final String PHOTO = "photo";
        private static final String GAME = "game";
        private static final String WIN = "win";
        private static final String LOSE = "lose";
        private static final String BEST_PVP = "best_pvp";
        private static final String BEST_TIME_TRIAL = "best_time_trial";
        private static final String TOTAL_EX = "total_ex";
        private static final String TOTAL_LEARN = "total_learn";

        private String username, fullname, photo;
        private int game, win, lose, bestPvp, bestTimeTrial, totalEx, totalLearn;

        public static CustomDialogFriendListDetail newInstance(String username, String fullname, String photo, int game, int win, int lose,
                                                                int bestPvp, int bestTimeTrial, int totalEx, int totalLearn) {

            Bundle args = new Bundle();
            args.putString(USERNAME, username);
            args.putString(FULLNAME, fullname);
            args.putString(PHOTO, photo);
            args.putInt(GAME, game);
            args.putInt(WIN, win);
            args.putInt(LOSE, lose);
            args.putInt(BEST_PVP, bestPvp);
            args.putInt(BEST_TIME_TRIAL, bestTimeTrial);
            args.putInt(TOTAL_EX, totalEx);
            args.putInt(TOTAL_LEARN, totalLearn);
            CustomDialogFriendListDetail fragment = new CustomDialogFriendListDetail();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                username = getArguments().getString(USERNAME);
                fullname = getArguments().getString(FULLNAME);
                photo = getArguments().getString(PHOTO);
                game = getArguments().getInt(GAME);
                win = getArguments().getInt(WIN);
                lose = getArguments().getInt(LOSE);
                bestPvp = getArguments().getInt(BEST_PVP);
                bestTimeTrial = getArguments().getInt(BEST_TIME_TRIAL);
                totalEx = getArguments().getInt(TOTAL_EX);
                totalLearn = getArguments().getInt(TOTAL_LEARN);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_fragment_profil_friend_list_detail, container, false);

            final ImageView photoImage = (ImageView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_photo);
            Picasso.with(getContext()).load(photo).networkPolicy(NetworkPolicy.OFFLINE).fit().into(photoImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(getContext()).load(photo).fit().into(photoImage);

                }
            });

            String bestTT = String.format(Locale.getDefault(), getResources().getString(R.string.main_activity_student_profile_statistic_best_time_trial_template), bestTimeTrial);

            int totalLearnNumber = totalLearn ;
            int day = totalLearnNumber/60;
            int minute = totalLearnNumber%60;
            int hour = day%24;
            day /= 24;

            String totalLearn = String.format(Locale.getDefault(), getResources().getString(R.string.main_activity_student_profile_statistic_template_hasil), day, hour, minute);

            TextView usernameText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_username);
            usernameText.setText(username);

            TextView fullnameText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_fullname);
            fullnameText.setText(fullname);

            TextView gameText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_total_games_score);
            gameText.setText(Integer.toString(game));

            TextView winText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_total_win_score);
            winText.setText(Integer.toString(win));

            TextView loseText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_total_lose_score);
            loseText.setText(Integer.toString(lose));

            TextView bestPvpText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_best_pvp_score);
            bestPvpText.setText(Integer.toString(bestPvp));

            TextView bestTimeTrialText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_best_time_trial_score);
            bestTimeTrialText.setText(bestTT);

            TextView totalExText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_total_exercise_score);
            totalExText.setText(Integer.toString(totalEx));

            TextView totalLearnText = (TextView) view.findViewById(R.id.dialog_fragment_profile_friend_list_detail_total_learn_score);
            totalLearnText.setText(totalLearn);

            return view;
        }
    }

    public static class CustomDialogAddFriend extends DialogFragment implements View.OnClickListener {

        private EditText inputID;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_profil_add_friend, container, false);

            Button ok = (Button) view.findViewById(R.id.main_activity_student_dialog_profil_add_friend_ok_button);
            Button cancel = (Button) view.findViewById(R.id.main_activity_student_dialog_profil_add_friend_cancel_button);
            ok.setOnClickListener(this);
            cancel.setOnClickListener(this);

            inputID = (EditText) view.findViewById(R.id.main_activity_student_dialog_profil_add_friend_input);

            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_activity_student_dialog_profil_add_friend_ok_button :
                    String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String opUid = inputID.getText().toString();
                    if (!opUid.isEmpty()) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Request Friend").child(opUid).push().setValue(myUid);
                        Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_profil_add_friend_toast_notif), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "The id is empty!", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.main_activity_student_dialog_profil_add_friend_cancel_button :
                    dismiss();
                    break;
            }
        }
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
