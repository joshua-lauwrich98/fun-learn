package anchovy.net.funlearn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import anchovy.net.funlearn.service.ClassAnnouncement;

public class AnnouncementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long time = intent.getExtras().getLong("time");
        Intent newServ = new Intent(context, ClassAnnouncement.class);
        newServ.putExtra("time", time);
        context.startService(newServ);
    }
}
