package anchovy.net.funlearn.fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import anchovy.net.funlearn.R;
import anchovy.net.funlearn.other.Comment;
import anchovy.net.funlearn.other.CommentViewHolder;
import anchovy.net.funlearn.other.Friend;
import anchovy.net.funlearn.other.FriendViewHolder;
import anchovy.net.funlearn.other.Post;
import anchovy.net.funlearn.other.PostViewHolder;


public class HomeThreadFragment extends Fragment {
    private static final String JENIS = "jenis";
    private static final String UID = "uid";

    private String jenis, uid;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    public HomeThreadFragment() {
        // Required empty public constructor
    }

    public static HomeThreadFragment newInstance(String jenis, String uid) {
        HomeThreadFragment fragment = new HomeThreadFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_thread, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.class_activity_home_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Class List")
                .child(uid).child("post");
        databaseReference.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.class_post_list_card_view,
                PostViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setFullname(model.getFullname());

                try {
                    if (!model.getPhoto().equals("null"))
                        viewHolder.setPhoto(getContext(), model.getPhoto());
                    else viewHolder.photo.setVisibility(View.GONE);

                    if (jenis.equals("teacher")) viewHolder.remove.setVisibility(View.VISIBLE);

                    final String path = model.getPath();
                    final String uidUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    final DatabaseReference like = FirebaseDatabase.getInstance().getReference().child("Class List")
                            .child(uid).child("post").child(path).child("like");
                    final DatabaseReference dislike = FirebaseDatabase.getInstance().getReference().child("Class List")
                            .child(uid).child("post").child(path).child("dislike");

                    like.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(uidUser)) {
                                viewHolder.dislike.setEnabled(false);
                                viewHolder.dislike.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                                viewHolder.like.setEnabled(true);
                                viewHolder.like.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                            } else {
                                dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(uidUser)) {
                                            viewHolder.dislike.setEnabled(true);
                                            viewHolder.dislike.setImageResource(R.drawable.ic_thumb_down_red_24dp);
                                            viewHolder.like.setEnabled(false);
                                            viewHolder.like.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                                        } else {
                                            viewHolder.like.setEnabled(true);
                                            viewHolder.like.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                                            viewHolder.dislike.setEnabled(true);
                                            viewHolder.dislike.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getContext(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
                        }
                    });

                    viewHolder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            like.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(uidUser)) {
                                        like.child(uidUser).setValue(null);
                                    } else {
                                        like.child(uidUser).setValue("ADMIN");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                    viewHolder.dislike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uidUser)) {
                                        dislike.child(uidUser).setValue(null);
                                    } else {
                                        dislike.child(uidUser).setValue("ADMIN");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                    like.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            viewHolder.likeCount.setText(Integer.toString(td.size()-1));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            viewHolder.dislikeCount.setText(Integer.toString(td.size()-1));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child(model.getPath()).setValue(null);
                            FirebaseStorage.getInstance().getReference().child("Class Post")
                                    .child(uid).child(model.getPath()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
    //                                        Toast.makeText(getContext(), "REMOVED!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
    //                                Toast.makeText(getContext(), "FAIL!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogFragment dialogFragment = CustomDialogCommentPost.newInstance(uid, model.getPath());
                            dialogFragment.setCancelable(true);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), null);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CustomDialogCommentPost extends DialogFragment {

        private String uid, postUid;
        private RecyclerView recyclerView;
        private ImageView photo;
        private TextView title, content, fullname;
        private DatabaseReference databaseReference, commentRef;
        private EditText inputComment;

        private static final String POST_UID = "post_uid";

        public static CustomDialogCommentPost newInstance(String uid, String postUid) {

            Bundle args = new Bundle();
            args.putString(UID, uid);
            args.putString(POST_UID, postUid);
            CustomDialogCommentPost fragment = new CustomDialogCommentPost();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                uid = getArguments().getString(UID);
                postUid = getArguments().getString(POST_UID);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_class_post_comment, container, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.dialog_fragment_class_post_detail_recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);

            photo = (ImageView) view.findViewById(R.id.dialog_fragment_class_post_detail_photo);

            title = (TextView) view.findViewById(R.id.dialog_fragment_class_post_detail_title);
            content = (TextView) view.findViewById(R.id.dialog_fragment_class_post_detail_content);
            fullname = (TextView) view.findViewById(R.id.dialog_fragment_class_post_detail_fullname);

            inputComment = (EditText) view.findViewById(R.id.dialog_fragment_class_post_detail_input_comment);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).child("post").child(postUid);
            databaseReference.keepSynced(true);

            commentRef = databaseReference.child("comment");
            commentRef.keepSynced(true);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child("photo").getValue().toString().equals("null")) {
                        Picasso.with(getContext()).load(dataSnapshot.child("photo").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).into(photo, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getContext()).load(dataSnapshot.child("photo").getValue().toString()).into(photo);
                            }
                        });
                    } else {
                        photo.setVisibility(View.GONE);
                    }

                    title.setText(dataSnapshot.child("title").getValue().toString());
                    content.setText(dataSnapshot.child("desc").getValue().toString());
                    fullname.setText(dataSnapshot.child("fullname").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Button post = (Button) view.findViewById(R.id.dialog_fragment_class_post_detail_post_comment_button);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String text = inputComment.getText().toString();
                    if (!text.isEmpty()) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("Class List")
                                        .child(uid).child("post").child(postUid).child("comment").push();

                                commentRef.child("photo").setValue(dataSnapshot.child("photo").getValue().toString());
                                commentRef.child("content").setValue(text);
                                commentRef.child("fullname").setValue(dataSnapshot.child("fullname").getValue().toString());

                                Calendar c = Calendar.getInstance();

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c.getTime());

                                SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                                String formattedTime = df1.format(c.getTime());

                                commentRef.child("time").setValue(formattedTime);
                                commentRef.child("date").setValue(formattedDate);

                                inputComment.setText("");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();

            Window window = getDialog().getWindow();
            Point size = new Point();

            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            int width = size.x;
//            (int) (width * 0.9)
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
        }

        @Override
        public void onStart() {
            super.onStart();

            FirebaseRecyclerAdapter<Comment, CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(
                    Comment.class,
                    R.layout.class_post_comment_list_card_view,
                    CommentViewHolder.class,
                    commentRef
            ) {
                @Override
                protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {
                    try {
                        if (model.getContent().equals("ADMIN")) {
                            viewHolder.mView.setVisibility(View.GONE);
                            viewHolder.content.setVisibility(View.GONE);
                            viewHolder.fullname.setVisibility(View.GONE);
                            viewHolder.photo.setVisibility(View.GONE);
                            viewHolder.date.setVisibility(View.GONE);
                            viewHolder.time.setVisibility(View.GONE);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    viewHolder.setDate(model.getDate());
                    viewHolder.setTime(model.getTime());
                    viewHolder.setPhoto(getContext(), model.getPhoto());
                    viewHolder.setContent(model.getContent());
                    viewHolder.setFullname(model.getFullname());
                }
            };

            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }
    }
}
