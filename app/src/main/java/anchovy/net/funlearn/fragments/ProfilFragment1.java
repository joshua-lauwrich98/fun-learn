package anchovy.net.funlearn.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment1 extends Fragment implements View.OnClickListener {

    private DatabaseReference usernameReference, photoReference;
    private TextView username;
    private ImageView photo;
    private Uri resultUri;

    private static final int GALERY_CODE = 1;

    public ProfilFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_fragment1, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.main_activity_profile_fragment_detail_container, new ProfilFragmentFriendList())
                .commit();

        resultUri = null;

        username = (TextView) view.findViewById(R.id.main_activity_profile_fragment_change_username);
        username.setOnClickListener(this);

        usernameReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username");
        usernameReference.keepSynced(true);

        usernameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        photo = (ImageView) view.findViewById(R.id.main_activity_profile_fragment_change_photo);
        photo.setOnClickListener(this);


        photoReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");
        photoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String url = dataSnapshot.getValue().toString();
                if (getActivity() != null) {
                    Picasso.with(getContext()).load(url).placeholder(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp)).error(R.drawable.ic_add_a_photo_black_24dp).networkPolicy(NetworkPolicy.OFFLINE).into(photo, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(url).placeholder(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp)).error(R.drawable.ic_add_a_photo_black_24dp).into(photo);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getActivity() != null) Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton friend = (ImageButton) view.findViewById(R.id.main_activity_profile_fragment_friend_list_button);
        friend.setOnClickListener(this);


        ImageButton statistic = (ImageButton) view.findViewById(R.id.main_activity_profile_fragment_statistic_button);
        statistic.setOnClickListener(this);

        ImageButton ownData = (ImageButton) view.findViewById(R.id.main_activity_profile_fragment_own_profile_button);
        ownData.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_profile_fragment_change_username :
                DialogFragment dialog = new CustomDialogFragmentUsername();
                dialog.show(getActivity().getSupportFragmentManager(), null);
                break;
            case R.id.main_activity_profile_fragment_change_photo :
                DialogFragment dialog2 = new CustomDialogChangePhoto();
                dialog2.show(getActivity().getSupportFragmentManager(), null);
                break;
            case R.id.main_activity_profile_fragment_friend_list_button :
                getChildFragmentManager().beginTransaction().replace(R.id.main_activity_profile_fragment_detail_container, new ProfilFragmentFriendList())
                        .commit();
                break;
            case R.id.main_activity_profile_fragment_statistic_button :
                getChildFragmentManager().beginTransaction().replace(R.id.main_activity_profile_fragment_detail_container, new ProfilFragmentStatistic())
                        .commit();
                break;
            case R.id.main_activity_profile_fragment_own_profile_button :
                getChildFragmentManager().beginTransaction().replace(R.id.main_activity_profile_fragment_detail_container, new ProfilFragmentOwnData())
                        .commit();
                break;
        }
    }

    public static class CustomDialogChangePhoto extends DialogFragment implements View.OnClickListener {

        private static final int GALERY_REQ_CODE = 01;
        private static final int CAMERA_REQ_CODE = 02;
        private ProgressDialog progDialog;

        private DatabaseReference photoUser, photoStatistic;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_fragment_profil_photo_change, container, false);

            TextView galeri = (TextView)view.findViewById(R.id.main_activity_student_dialog_profil_change_photo_galeri_button);
            TextView camera = (TextView)view.findViewById(R.id.main_activity_student_dialog_profil_change_photo_camera_button);
            galeri.setOnClickListener(this);
            camera.setOnClickListener(this);

            photoUser = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("photo");
            photoUser.keepSynced(true);
            photoStatistic = FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("photo");
            photoStatistic.keepSynced(true);

            progDialog = new ProgressDialog(getContext());

            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_activity_student_dialog_profil_change_photo_galeri_button :
                    Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galeryIntent.setType("image/*");
                    startActivityForResult(galeryIntent, GALERY_REQ_CODE);
                    break;

                case R.id.main_activity_student_dialog_profil_change_photo_camera_button :
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
                    break;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == GALERY_REQ_CODE && resultCode == getActivity().RESULT_OK) {
                Uri imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityMenuIconColor(getResources().getColor(R.color.colorPrimary))
                        .setAllowRotation(true)
                        .setActivityTitle("Photo")
                        .setBorderLineColor(getResources().getColor(R.color.colorAccent))
                        .start(getContext(), this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    progDialog.setMessage(getResources().getString(R.string.dialog_fragment_profil_photo_change_progress_dialog_message));
                    progDialog.setCancelable(false);
                    progDialog.show();
                    Uri resultUri = result.getUri();
                    StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Profile Photo").child(resultUri.getLastPathSegment());
                    filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            photoUser.setValue(downloadUri.toString());
                            photoStatistic.setValue(downloadUri.toString());
                            progDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progDialog.dismiss();
                            e.printStackTrace();
                        }
                    });
                    dismiss();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getActivity(), result.getError().getMessage(), Toast.LENGTH_LONG).show();
                    dismiss();
                }
            } else if (requestCode == CAMERA_REQ_CODE && resultCode == getActivity().RESULT_OK) {
                Uri uri = data.getData();
                CropImage.activity(uri)
                        .setActivityMenuIconColor(getResources().getColor(R.color.colorPrimary))
                        .setAllowRotation(true)
                        .setActivityTitle("Photo")
                        .setBorderLineColor(getResources().getColor(R.color.colorAccent))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
        }
    }

    public static class CustomDialogFragmentUsername extends DialogFragment implements View.OnClickListener {

        private DatabaseReference databaseReference;
        private EditText usernameInput;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setCancelable(false);
            View view = inflater.inflate(R.layout.dialog_fragment_profil_username_change, container, false);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            usernameInput = (EditText) view.findViewById(R.id.main_activity_student_dialog_profil_change_username_input);

            Button ok = (Button) view.findViewById(R.id.main_activity_student_dialog_profil_change_username_ok_button);
            Button cancel = (Button) view.findViewById(R.id.main_activity_student_dialog_profil_change_username_cancel_button);
            ok.setOnClickListener(this);
            cancel.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_activity_student_dialog_profil_change_username_ok_button :
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String username = usernameInput.getText().toString();
                    if (!username.isEmpty()) {
                        databaseReference.child("Users").child(uid).child("username").setValue(username);
                        databaseReference.child("Statistic").child(uid).child("username").setValue(username);
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "The username is empty!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.main_activity_student_dialog_profil_change_username_cancel_button :
                    dismiss();
                    break;
            }
        }
    }
}
