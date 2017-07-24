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
 * Created by DarKnight98 on 7/23/2017.
 */

public class ClassMemberViewHolder extends RecyclerView.ViewHolder {

    public View mView;

    public ClassMemberViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public void setPhoto (final Context context, final String url) {
        final ImageView photo = (ImageView)mView.findViewById(R.id.class_member_photo);
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

    public void setUsername (String username) {
        TextView un = (TextView)mView.findViewById(R.id.class_member_username);
        un.setText(username);
    }

    public void setFullname (String fullname) {
        TextView fn = (TextView)mView.findViewById(R.id.class_member_fullname);
        fn.setText(fullname);
    }
}
