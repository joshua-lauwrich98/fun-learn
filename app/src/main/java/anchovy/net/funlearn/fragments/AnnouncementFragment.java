package anchovy.net.funlearn.fragments;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.service.ClassAnnouncement;

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
                try {
                    title.setText(dataSnapshot.child("title").getValue().toString());
                    content.setText(dataSnapshot.child("content").getValue().toString());
                    String createdText = String.format(Locale.getDefault(),
                            getResources().getString(R.string.class_activity_announcement_fragment_created_text_templat),
                            dataSnapshot.child("created").getValue().toString());
                    created.setText(createdText);

                    try {
                        String expiredText = String.format(Locale.getDefault(),
                                getResources().getString(R.string.class_activity_announcement_fragment_created_text_template),
                                dataSnapshot.child("expired").getValue().toString());
                        expired.setText(expiredText);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = CustomDialogEditAnnouncement.newInstance(uid);
                dialog.setCancelable(false);
                dialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        return view;
    }


    public static class CustomDialogEditAnnouncement extends DialogFragment {

        private String uid;
        private TextInputLayout titleLay, contentLay, dayLay, hourLay, minLay;
        private EditText title, content, day, hour, min;

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

            titleLay = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_announcement_edit_title_layout);
            contentLay = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_announcement_edit_content_layout);
            dayLay = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_announcement_edit_day_layout);
            hourLay = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_announcement_edit_hour_layout);
            minLay = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_announcement_edit_minute_layout);

            title = (EditText) view.findViewById(R.id.dialog_fragment_class_announcement_edit_title);
            content = (EditText) view.findViewById(R.id.dialog_fragment_class_announcement_edit_content);
            day = (EditText) view.findViewById(R.id.dialog_fragment_class_announcement_edit_day);
            hour = (EditText) view.findViewById(R.id.dialog_fragment_class_announcement_edit_hour);
            min = (EditText) view.findViewById(R.id.dialog_fragment_class_announcement_edit_minute);

            Button ok = (Button) view.findViewById(R.id.dialog_fragment_class_announcement_edit_ok_button);
            Button cancel = (Button) view.findViewById(R.id.dialog_fragment_class_announcement_edit_cancel_button);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });

            return view;
        }

        public void submitForm() {
            String titleText = title.getText().toString().trim();
            String contentText = content.getText().toString().trim();
            String hourText = hour.getText().toString();
            String dayText = day.getText().toString();
            String minuteText = min.getText().toString();

            titleLay.setErrorEnabled(false);
            contentLay.setErrorEnabled(false);
            dayLay.setErrorEnabled(false);
            hourLay.setErrorEnabled(false);
            minLay.setErrorEnabled(false);

            if (contentText.isEmpty()) {
                contentLay.setErrorEnabled(true);
                contentLay.setError(getResources().getString(R.string.dialog_fragment_class_error));
                return;
            }

            if (hourText.isEmpty()) {
                hourLay.setErrorEnabled(true);
                hourLay.setError(getResources().getString(R.string.dialog_fragment_class_error));
                return;
            }

            if (dayText.isEmpty()) {
                dayLay.setErrorEnabled(true);
                dayLay.setError(getResources().getString(R.string.dialog_fragment_class_error));
                return;
            }

            if (minuteText.isEmpty()) {
                minLay.setErrorEnabled(true);
                minLay.setError(getResources().getString(R.string.dialog_fragment_class_error));
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).child("announcement");
            if (!titleText.isEmpty()) ref.child("title").setValue(titleText);
//            else Toast.makeText(getActivity(), "EMPTY", Toast.LENGTH_SHORT).show();
            ref.child("content").setValue(contentText);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c.getTime());
            ref.child("created").setValue(formattedDate);

            try {
                c.setTime(df.parse(formattedDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c.add(Calendar.DATE, Integer.parseInt(dayText));
            formattedDate = df.format(c.getTime());

            ref.child("expired").setValue(formattedDate);

            long time = (Long.parseLong(dayText) * 24 * 1000 * 3600) +
                        (Long.parseLong(hourText) * 3600 * 1000) +
                        (Long.parseLong(minuteText) * 60 * 1000);

            Intent intent = new Intent(getContext(), ClassAnnouncement.class);
            intent.putExtra("time", time);
            intent.putExtra("uid", uid);
            getActivity().startService(intent);
            dismiss();
        }
    }
}
