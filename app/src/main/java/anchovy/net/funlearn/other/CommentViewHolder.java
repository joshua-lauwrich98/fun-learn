package anchovy.net.funlearn.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 7/22/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public TextView date, time, content, fullname;
    public ImageView photo;

    public CommentViewHolder(View itemView) {
        super(itemView);

        this.mView = itemView;
        this.date = (TextView)itemView.findViewById(R.id.post_comment_card_view_date);
        this.time = (TextView)itemView.findViewById(R.id.post_comment_card_view_time);
        this.content = (TextView)itemView.findViewById(R.id.post_comment_card_view_content);
        this.fullname = (TextView)itemView.findViewById(R.id.post_comment_card_view_fullname);
        this.photo = (ImageView)itemView.findViewById(R.id.post_comment_card_view_photo);
    }

    public void setDate (String dateText) {
        date.setText(dateText);
    }

    public void setTime (String timeText) {
        time.setText(timeText);
    }

    public void setContent (String contentText) {
        content.setText(contentText);
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
