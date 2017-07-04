package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment1 extends Fragment implements View.OnClickListener {


    public PlayFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_fragment1, container, false);

        Button pvp = (Button)view.findViewById(R.id.main_activity_student_play_fragment1_pvp_button);
        Button withFriend = (Button) view.findViewById(R.id.main_activity_student_play_fragment1_play_with_friend_button);
        Button leaderboard = (Button) view.findViewById(R.id.main_activity_student_play_fragment1_leaderboard_button);
        pvp.setOnClickListener(this);
        withFriend.setOnClickListener(this);
        leaderboard.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_student_play_fragment1_pvp_button :

                break;
            case R.id.main_activity_student_play_fragment1_play_with_friend_button :

                break;
            case R.id.main_activity_student_play_fragment1_leaderboard_button :

                break;
        }
    }
}
