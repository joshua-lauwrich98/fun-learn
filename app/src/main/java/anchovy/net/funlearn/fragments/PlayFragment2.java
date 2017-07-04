package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment2 extends Fragment {
    private static final String MODE = "mode";

    private String mode;


    public PlayFragment2() {
        // Required empty public constructor
    }

    public static PlayFragment2 newInstance(String mode) {
        PlayFragment2 fragment = new PlayFragment2();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_fragment2, container, false);
    }

}
