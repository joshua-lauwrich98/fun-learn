package anchovy.net.funlearn.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragmentOwnData extends Fragment {


    public ProfilFragmentOwnData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_fragment_own_data, container, false);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.keepSynced(true);

        TextView uidText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_uid);
        final TextView emailText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_email);
        final TextView fullnameText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_fullname);
        final TextView accTypeText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_account_type);
        final TextView schoolText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_school);
        final TextView gradeText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_grade);
        final TextView codeText = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_code);

        final TextView schoolTextTitle = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_school_title);
        final TextView gradeTextTitle = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_grade_title);
        final TextView codeTextTitle = (TextView) view.findViewById(R.id.main_activity_profile_fragment_own_profile_code_title);

        ImageButton copy = (ImageButton) view.findViewById(R.id.main_activity_profile_fragment_own_profile_copy_button);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getParentFragment().getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(uid, uid);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), getResources().getString(R.string.main_activity_student_profile_own_data_copy), Toast.LENGTH_SHORT).show();
            }
        });

        final Button rankUp = (Button) view.findViewById(R.id.main_activity_profile_fragment_own_profile_rank_up_button);
        rankUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new CustomDialogRankUpConfirmation();
                dialog.setCancelable(false);
                dialog.show(getParentFragment().getActivity().getSupportFragmentManager(), null);
            }
        });

        uidText.setText(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailText.setText(dataSnapshot.child("email").getValue().toString());
                fullnameText.setText(dataSnapshot.child("fullname").getValue().toString());
                String acc = dataSnapshot.child("acountType").getValue().toString();
                accTypeText.setText(acc);

                if (acc.equals("student")) {
                    schoolText.setText(dataSnapshot.child("jenjang").getValue().toString());
                    gradeText.setText(dataSnapshot.child("kelas").getValue().toString());
                    schoolText.setVisibility(View.VISIBLE);
                    gradeText.setVisibility(View.VISIBLE);
                    rankUp.setVisibility(View.VISIBLE);
                    schoolTextTitle.setVisibility(View.VISIBLE);
                    gradeTextTitle.setVisibility(View.VISIBLE);
                    codeText.setVisibility(View.INVISIBLE);
                    codeTextTitle.setVisibility(View.INVISIBLE);
                } else {
                    codeText.setText(dataSnapshot.child("code").getValue().toString());
                    schoolText.setVisibility(View.INVISIBLE);
                    gradeText.setVisibility(View.INVISIBLE);
                    rankUp.setVisibility(View.INVISIBLE);
                    gradeTextTitle.setVisibility(View.INVISIBLE);
                    schoolTextTitle.setVisibility(View.INVISIBLE);
                    codeText.setVisibility(View.VISIBLE);
                    codeTextTitle.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    public static class CustomDialogRankUpConfirmation extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_fragment_profil_own_data_rank_up_confirmation, container,false);

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            Button ya = (Button) view.findViewById(R.id.dialog_fragment_profile_own_data_yes_button);
            Button no = (Button) view.findViewById(R.id.dialog_fragment_profile_own_data_cancel_button);

            ya.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String school = dataSnapshot.child("jenjang").getValue().toString();
                            int grade = Integer.parseInt(dataSnapshot.child("kelas").getValue().toString());

                            if (school.equals("SD")) {
                                if (grade == 6) {
                                    databaseReference.child("jenjang").setValue("SMP");
                                    databaseReference.child("kelas").setValue("1");
                                } else {
                                    grade++;
                                    databaseReference.child("kelas").setValue(Integer.toString(grade));
                                }
                            } else if (school.equals("SMP")) {
                                if (grade == 3) {
                                    databaseReference.child("jenjang").setValue("SMA");
                                    databaseReference.child("kelas").setValue("1");
                                } else {
                                    grade++;
                                    databaseReference.child("kelas").setValue(Integer.toString(grade));
                                }
                            } else {
                                if (grade == 3) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_rank_up_max), Toast.LENGTH_LONG).show();
                                } else {
                                    grade++;
                                    databaseReference.child("kelas").setValue(Integer.toString(grade));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    dismiss();
                }
            });


            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            return view;
        }
    }

}
