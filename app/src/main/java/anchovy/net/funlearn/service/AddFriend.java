package anchovy.net.funlearn.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import anchovy.net.funlearn.GetStartedActivity;
import anchovy.net.funlearn.R;

public class AddFriend extends Service {

    private String opName, opUID;

    public AddFriend() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Request Friend").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                List<Object> friendList = null;

                if (td != null) {
                    friendList = new ArrayList<>(td.values());
                }

                if (friendList != null) {
                    opUID = friendList.get(0).toString();

//                    DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(opUID);
//                    temp.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            opName = dataSnapshot.child("fullname").getValue().toString();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    DatabaseReference temp2 = FirebaseDatabase.getInstance().getReference().child("AAA").child(uid);
//                    temp2.child("status").setValue("yes");
//                    temp2.child("name").setValue(opName);
//                    temp2.child("uid").setValue(opUID);

                    SharedPreferences sp = getSharedPreferences("add_friend_notif", MODE_PRIVATE);
                    if (sp.getBoolean("status", true)) {
                        startNotification();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return Service.START_STICKY;
    }

    public void startNotification () {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(opUID);
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("fullname").getValue() != null) {
                    opName = dataSnapshot.child("fullname").getValue().toString();
                    DatabaseReference temp2 = FirebaseDatabase.getInstance().getReference().child("AAA").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    temp2.child("status").setValue("yes");
                    temp2.child("name").setValue(opName);
                    temp2.child("uid").setValue(opUID);

                    String ticker = getResources().getString(R.string.notification_add_friend_ticker);
                    String title = getResources().getString(R.string.app_name);
                    String content = String.format(Locale.getDefault(), getResources().getString(R.string.notification_add_friend_content), opName);

                    NotificationCompat.Builder notif = new NotificationCompat.Builder(getApplicationContext());
                    notif.setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setTicker(ticker)
                            .setContentTitle(title)
                            .setContentText(ticker)
                            .setWhen(System.currentTimeMillis())
                            .setSubText(content)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(ticker))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(ticker))
                            .setColor(0x75bef9)
                            .setVibrate(new long[] {2, 2, 2})
                            .setLights(0x75bef9, 2000, 1000);

                    if (Build.VERSION.SDK_INT >= 21) {
                        notif.setCategory(Notification.CATEGORY_EVENT)
                                .setPriority(Notification.PRIORITY_MAX);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        notif.setPriority(Notification.PRIORITY_MAX);
                    }

                    Intent i = new Intent(getApplicationContext(), GetStartedActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    notif.setContentIntent(pendingIntent);

                    NotificationManager nn = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notify = new Notification();
                    notify.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;
                    nn.notify(2, notif.build());

                    SharedPreferences.Editor sp = getSharedPreferences("add_friend_notif", MODE_PRIVATE).edit();
                    sp.putBoolean("status", false);
                    sp.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
