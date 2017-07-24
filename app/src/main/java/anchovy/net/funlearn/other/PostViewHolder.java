package anchovy.net.funlearn.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 7/21/2017.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public Button remove;
    public TextView title, desc, fullname, likeCount, dislikeCount;
    public ImageButton like, dislike;
    public ImageView photo;

    public PostViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        remove = (Button)itemView.findViewById(R.id.class_activity_home_remove_button);
        title = (TextView)itemView.findViewById(R.id.class_activity_home_post_title);
        desc = (TextView)itemView.findViewById(R.id.class_activity_home_post_desc);
        fullname = (TextView)itemView.findViewById(R.id.class_activity_home_post_fullname);
        like = (ImageButton) itemView.findViewById(R.id.class_activity_home_post_like_button);
        dislike = (ImageButton) itemView.findViewById(R.id.class_activity_home_post_dislike_button);
        photo = (ImageView) itemView.findViewById(R.id.class_activity_home_image_button_post);
        likeCount = (TextView) itemView.findViewById(R.id.class_activity_home_post_like_count);
        dislikeCount = (TextView) itemView.findViewById(R.id.class_activity_home_post_dislike_count);
    }

    public void setTitle (String titleText) {
        title.setText(titleText);
    }

    public void setDesc (String descText) {
        desc.setText(descText);
    }

    public void setFullname (String fullnameText) {
        fullname.setText(fullnameText);
    }

    public void setPhoto (final Context context, final String url) {
        Picasso.with(context).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(photo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(url).into(photo);
            }
        });
    }
}
