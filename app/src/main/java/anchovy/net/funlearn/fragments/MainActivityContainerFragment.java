package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.MainActivityStudent;
import anchovy.net.funlearn.R;

public class MainActivityContainerFragment extends Fragment {

    private static final String JENIS = "jenis";
    private static final String ACC_TYPE = "acc_type";
    private String jenis, accType;


    public MainActivityContainerFragment() {
        // Required empty public constructor
    }

    public static MainActivityContainerFragment newInstance(String jenis, String accType) {
        MainActivityContainerFragment fragment = new MainActivityContainerFragment();
        Bundle args = new Bundle();
        args.putString(JENIS, jenis);
        args.putString(ACC_TYPE, accType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jenis = getArguments().getString(JENIS);
            accType = getArguments().getString(ACC_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_activity_container, container, false);

//        Toast.makeText(getActivity(), jenis, Toast.LENGTH_SHORT).show();
        switch (this.jenis) {
            case "personal" :
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_student_frame_root, new PersonalFragment1(), "personal student root")
                        .commit();
                break;
            case "play" :
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_student_frame_root, new PlayFragment1(), "play student root")
                        .commit();
                break;
            case "class" :
                if (accType.equals("student")) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.main_activity_student_frame_root, ClassFragment1.newInstance("student"), "class student root")
                            .commit();
                } else {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.main_activity_student_frame_root, ClassFragment1.newInstance("teacher"), "class student root")
                            .commit();
                }
                break;
            case "profil" :
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_student_frame_root, new ProfilFragment1(), "profile student root")
                        .commit();
                break;
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
