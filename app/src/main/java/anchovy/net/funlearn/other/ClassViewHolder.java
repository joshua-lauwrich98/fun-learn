package anchovy.net.funlearn.other;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 7/11/2017.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder {

    public View myView;
    public Button button;
    public ImageButton copy;

    public ClassViewHolder(View itemView) {
        super(itemView);

        this.myView = itemView;
        this.button = (Button) itemView.findViewById(R.id.class_list_card_view_button);
        this.copy = (ImageButton) itemView.findViewById(R.id.class_list_copy_uid);
    }

    public void setButton (String text) {
        button.setText(text);
    }

    public void copyUid (Context context, String uid) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(uid, uid);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, context.getResources().getString(R.string.main_activity_student_profile_own_data_copy), Toast.LENGTH_SHORT).show();
    }
}
