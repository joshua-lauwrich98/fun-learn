package anchovy.net.funlearn.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateThreadFragment extends Fragment implements View.OnClickListener {

    private static final String UID = "uid";
    private static final int GALERY_CODE = 1;

    private String uid;
    private EditText title, desc;
    private Uri resultUri;
    private ProgressDialog progress;
    private ImageButton addImage;

    public CreateThreadFragment() {
        // Required empty public constructor
    }

    public static CreateThreadFragment newInstance(String uid) {

        Bundle args = new Bundle();
        args.putString(UID, uid);
        CreateThreadFragment fragment = new CreateThreadFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_thread, container, false);

        addImage = (ImageButton) view.findViewById(R.id.login_activity_create_blog_fragment_add_image_button);
        Button postButton = (Button) view.findViewById(R.id.login_activity_create_blog_fragment_submit_button);

        title = (EditText) view.findViewById(R.id.login_activity_create_blog_fragment_post_title_input);
        desc = (EditText) view.findViewById(R.id.login_activity_create_blog_fragment_post_desc_input);

        addImage.setOnClickListener(this);
        postButton.setOnClickListener(this);

        resultUri = null;

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_create_blog_fragment_add_image_button :
                Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeryIntent.setType("image/*");
                startActivityForResult(galeryIntent, GALERY_CODE);
                break;
            case R.id.login_activity_create_blog_fragment_post_desc_input :
                final String titleText = title.getText().toString().trim();
                final String descText = desc.getText().toString().trim();

                if (titleText.isEmpty()) {
                    title.setError(getResources().getString(R.string.dialog_fragment_class_error));
                    break;
                }

                if (descText.isEmpty()) {
                    desc.setError(getResources().getString(R.string.dialog_fragment_class_error));
                    break;
                }

                progress.setMessage(getResources().getString(R.string.class_activity_announcement_fragment_create_post_progress_message));
                progress.show();
                final String randPath = UUID.randomUUID().toString();
                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Class Post").child(uid).child(randPath);

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        final DatabaseReference classRef = FirebaseDatabase.getInstance().getReference()
                                .child("Class List").child(uid);
                        classRef.child("post").child(randPath).child("title").setValue(titleText);
                        classRef.child("post").child(randPath).child("desc").setValue(descText);
                        classRef.child("post").child(randPath).child("photo").setValue(downloadUrl);

                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                classRef.child("post").child(randPath).child("fullname").setValue(dataSnapshot.child("fullname").getValue().toString());
                                progress.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_CODE && resultCode == getActivity().RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityMenuIconColor(getResources().getColor(R.color.colorPrimary))
                    .setAllowRotation(true)
                    .setActivityTitle("Choose Photo")
                    .setBorderLineColor(getResources().getColor(R.color.colorAccent))
                    .start(getContext(), this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                resultUri = result.getUri();
                addImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }
}
