package anchovy.net.funlearn.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClassAnnouncement extends Service {

    CountDownTimer timer;
    long currTime;
    boolean status = true;

    public ClassAnnouncement() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long time = intent.getExtras().getLong("time");
        final String uid = intent.getExtras().getString("uid");
//        Toast.makeText(this, uid + " " + Long.toString(time), Toast.LENGTH_SHORT).show();
        timer = new CountDownTimer(time, 60000) {
            @Override
            public void onTick(long l) {
//                Toast.makeText(ClassAnnouncement.this, Long.toString(l), Toast.LENGTH_SHORT).show();
                currTime = l;
            }

            @Override
            public void onFinish() {
                try {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Class List").child(uid).child("announcement");
                    ref.child("content").setValue("-");
                    ref.child("created").setValue("-");
                    ref.child("expired").setValue("-");
                    status = false;

                    timer.cancel();
                    stopSelf();
                } catch (Exception e) {
                    stopSelf();
                    e.printStackTrace();
                }
            }
        }.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (status) {
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Intent intent = new Intent("anchovy.net.funlearn.receiver");
                intent.putExtra("time", currTime);
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
