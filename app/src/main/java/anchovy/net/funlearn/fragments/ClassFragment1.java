package anchovy.net.funlearn.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import anchovy.net.funlearn.ClassActivity;
import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.Class;
import anchovy.net.funlearn.other.ClassViewHolder;
import anchovy.net.funlearn.other.Friend;
import anchovy.net.funlearn.other.FriendViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment1 extends Fragment implements View.OnClickListener {

    private static final String JENIS = "jenis";
    private String jenis;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    public static ClassFragment1 newInstance(String jenis) {

        Bundle args = new Bundle();
        args.putString(JENIS, jenis);
        ClassFragment1 fragment = new ClassFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    public ClassFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            jenis = getArguments().getString(JENIS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_class_fragment1, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.main_activity_student_class_fragment_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Class").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        databaseReference.keepSynced(true);

        Button add = (Button) view.findViewById(R.id.main_activity_student_class_fragment_add_class_button);
        Button join = (Button) view.findViewById(R.id.main_activity_student_class_fragment_join_class_button);
        add.setOnClickListener(this);
        join.setOnClickListener(this);

        if (jenis.equals("student")) add.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Class, ClassViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Class, ClassViewHolder> (
                Class.class,
                R.layout.class_list_card_view,
                ClassViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final ClassViewHolder viewHolder, final Class model, int position) {
                viewHolder.setButton(model.getName());

                if (jenis.equals("student")) viewHolder.copy.setVisibility(View.GONE);

                viewHolder.copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.copyUid(getContext(), model.getUid());
                    }
                });

                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent classActi = new Intent(getContext(), ClassActivity.class);
                        classActi.putExtra("JENIS", jenis);
                        classActi.putExtra("UID", model.getUid());
                        classActi.putExtra("TITLE", model.getName());
                        startActivity(classActi);
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_student_class_fragment_add_class_button :
                DialogFragment addDialog = new CustomDialogAddClass();
                addDialog.setCancelable(false);
                addDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                addDialog.show(getActivity().getSupportFragmentManager(), null);
                break;

            case R.id.main_activity_student_class_fragment_join_class_button :
                DialogFragment joinDialog = new CustomDialogJoinClass();
                joinDialog.setCancelable(false);
                joinDialog.show(getActivity().getSupportFragmentManager(), null);
                break;
        }
    }

    public static class CustomDialogJoinClass extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_fragment_class_join, container, false);

            final EditText input = (EditText) view.findViewById(R.id.main_activity_student_dialog_class_join_input);

            Button ok = (Button) view.findViewById(R.id.main_activity_student_dialog_class_join_ok_button);
            Button cancel = (Button) view.findViewById(R.id.main_activity_student_dialog_class_join_cancel_button);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String uid = input.getText().toString().trim();
                    final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (uid.isEmpty()) {
                        input.setError(getResources().getString(R.string.dialog_fragment_class_error));
                        return;
                    }

                    DatabaseReference classRef = FirebaseDatabase.getInstance().getReference().child("Class List");
                    classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.hasChild(uid)) {
                                Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_class_join_error), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                int curr = Integer.parseInt(dataSnapshot.child(uid).child("curr").getValue().toString());
                                int max = Integer.parseInt(dataSnapshot.child(uid).child("max").getValue().toString());

                                if (dataSnapshot.child(uid).child("member").hasChild(myUid)) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_class_join_already), Toast.LENGTH_SHORT).show();
                                } else if (curr >= max) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_class_max_reach), Toast.LENGTH_SHORT).show();
                                } else {
                                    final DatabaseReference ref = dataSnapshot.child(uid).child("member").child(myUid).getRef();
                                    DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("Users").child(myUid);
                                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ref.child("fullname").setValue(dataSnapshot.child("fullname").getValue().toString());
                                            ref.child("photo").setValue(dataSnapshot.child("photo").getValue().toString());
                                            ref.child("uid").setValue(myUid);
                                            ref.child("username").setValue(dataSnapshot.child("username").getValue().toString());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    final DatabaseReference ref2 = dataSnapshot.child(uid).child("curr").getRef();
                                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ref2.setValue(Integer.toString(Integer.parseInt(dataSnapshot.getValue().toString())+1));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Class")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    ref3.child(uid).child("uid").setValue(uid);
                                    ref3.child(uid).child("name").setValue(dataSnapshot.child(uid).child("name").getValue().toString());

                                    dismiss();
                                    Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_class_success), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            return view;
        }
    }

    public static class CustomDialogAddClass extends DialogFragment {

        private TextInputLayout name, max;
        private EditText nameInput, maxInput;
        private Vibrator vib;
        Animation shake;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_fragment_class_add,container,false);

            getDialog().setTitle(getResources().getString(R.string.main_activity_student_class_fragment_add_class));

            name = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_add_name_layout);
            max = (TextInputLayout) view.findViewById(R.id.dialog_fragment_class_add_member_layout);

            nameInput = (EditText) view.findViewById(R.id.dialog_fragment_class_add_name);
            maxInput = (EditText) view.findViewById(R.id.dialog_fragment_class_add_member);

            shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            vib = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

            Button ok = (Button) view.findViewById(R.id.main_activity_class_fragment_add_ok_button);
            Button cancel = (Button) view.findViewById(R.id.main_activity_class_fragment_add_cancel_button);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            return view;
        }

        public void submitForm() {
            name.setErrorEnabled(false);
            max.setErrorEnabled(false);

            if (!checkName()) {
                nameInput.setAnimation(shake);
                nameInput.startAnimation(shake);
                vib.vibrate(120);
                return;
            }

            if (!checkMax()) {
                maxInput.setAnimation(shake);
                maxInput.startAnimation(shake);
                vib.vibrate(120);
                return;
            }

            max.setErrorEnabled(false);
            name.setErrorEnabled(false);

            String uid = UUID.randomUUID().toString();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            final DatabaseReference classList = databaseReference.child("Class List").child(uid);
            classList.child("name").setValue(nameInput.getText().toString());
            classList.child("uid").setValue(uid);
            classList.child("max").setValue(maxInput.getText().toString());
            classList.child("curr").setValue("1");
            classList.child("announcement").child("title").setValue("WELCOME !!!");
            classList.child("announcement").child("content").setValue("NO CONTENT");

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c.getTime());
            classList.child("created").setValue(formattedDate);

            classList.child("announcement").child("created").setValue(formattedDate);

            final DatabaseReference classList1 = classList.child("member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    classList1.child("fullname").setValue(dataSnapshot.child("fullname").getValue().toString());
                    classList1.child("photo").setValue(dataSnapshot.child("photo").getValue().toString());
                    classList1.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    classList1.child("username").setValue(dataSnapshot.child("username").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "FAILED!", Toast.LENGTH_SHORT).show();
                }
            });

            DatabaseReference classOnly = databaseReference.child("Class").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(uid);
            classOnly.child("uid").setValue(uid);
            classOnly.child("name").setValue(nameInput.getText().toString());

            dismiss();
            Toast.makeText(getContext(), getResources().getString(R.string.dialog_fragment_class_success), Toast.LENGTH_SHORT).show();
        }

        private boolean checkName () {
            String nameText = nameInput.getText().toString().trim();
            if (nameText.isEmpty()) {
                name.setErrorEnabled(true);
                name.setError(getResources().getString(R.string.dialog_fragment_class_error));
                requestFocus(nameInput);
                return false;
            }

            name.setErrorEnabled(false);
            return true;
        }

        private boolean checkMax () {
            String passwordText = maxInput.getText().toString().trim();
            if (passwordText.isEmpty()) {
                max.setErrorEnabled(true);
                max.setError(getResources().getString(R.string.dialog_fragment_class_error));
                requestFocus(maxInput);
                return false;
            }

            if (Integer.parseInt(passwordText) > 20) {
                max.setErrorEnabled(true);
                max.setError(getResources().getString(R.string.dialog_fragment_class_error2));
                requestFocus(maxInput);
                return false;
            }

            max.setErrorEnabled(false);
            return true;
        }

        private void requestFocus(View view) {
            if (view.requestFocus()) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }
}
