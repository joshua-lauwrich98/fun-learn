package anchovy.net.funlearn.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 7/11/2017.
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public View myView;
    public ImageView photo, status;
    public TextView username, time;

    public FriendViewHolder(View itemView) {
        super(itemView);

        this.myView = itemView;
        this.photo = (ImageView)itemView.findViewById(R.id.user_photo);
        this.status = (ImageView)itemView.findViewById(R.id.user_status_icon);
        this.username = (TextView) itemView.findViewById(R.id.user_username);
        this.time = (TextView) itemView.findViewById(R.id.user_status);

        myView.setVisibility(View.GONE);
        photo.setVisibility(View.GONE);
        status.setVisibility(View.GONE);
        username.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
    }

    public void setUsername (String username) {
        this.username.setText(username);
    }

    public void setImage (Context context, String url) {
        Picasso.with(context).load(url).into(photo);
    }

    public void setIcon (String status) {
        if (status.equals("online")) {
            this.status.setImageResource(R.drawable.status_online);
            this.time.setText("Online");
        } else {
            this.status.setImageResource(R.drawable.status_offline);
            this.time.setText("Offline");
        }
    }
}
