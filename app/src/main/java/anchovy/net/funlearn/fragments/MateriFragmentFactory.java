package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MateriFragmentFactory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MateriFragmentFactory extends Fragment {

    private static final String JUDUL = "judul";

    private String judul;


    public MateriFragmentFactory() {
        // Required empty public constructor
    }

    public static MateriFragmentFactory newInstance(String judul) {
        MateriFragmentFactory fragment = new MateriFragmentFactory();
        Bundle args = new Bundle();
        args.putString(JUDUL, judul);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            judul = getArguments().getString(JUDUL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch (judul) {

        }

        return null;
    }

}
