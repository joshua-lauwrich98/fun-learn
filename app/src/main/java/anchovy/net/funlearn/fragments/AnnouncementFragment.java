package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.R;

public class AnnouncementFragment extends Fragment {

    private static final String JENIS = "jenis";
    private static final String UID = "uid";

    private String jenis, uid;
    private TextView title, content, created, expired;


    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public static AnnouncementFragment newInstance(String jenis, String uid) {
        AnnouncementFragment fragment = new AnnouncementFragment();
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
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        Button edit = (Button) view.findViewById(R.id.class_activity_announcement_fragment_edit_button);
        title = (TextView) view.findViewById(R.id.class_activity_announcement_fragment_title);
        content = (TextView) view.findViewById(R.id.class_activity_announcement_fragment_content);
        created = (TextView) view.findViewById(R.id.class_activity_announcement_fragment_created);
        expired = (TextView) view.findViewById(R.id.class_activity_announcement_fragment_expired);

        if (!jenis.equals("student")) edit.setVisibility(View.VISIBLE);

        DatabaseReference announceRef = FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).child("announcement");
        announceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.child("title").getValue().toString());
                content.setText(dataSnapshot.child("content").getValue().toString());
                created.setText(dataSnapshot.child("created").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }


    public static class CustomDialogEditAnnouncement extends DialogFragment {

        private String uid;

        public static CustomDialogEditAnnouncement newInstance(String uid) {
            Bundle args = new Bundle();
            args.putString(UID, uid);
            CustomDialogEditAnnouncement fragment = new CustomDialogEditAnnouncement();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                uid = getArguments().getString(UID);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_class_announcement_edit,container,false);


            return view;
        }
    }
}
