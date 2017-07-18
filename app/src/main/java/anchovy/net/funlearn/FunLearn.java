package anchovy.net.funlearn;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by DarKnight98 on 6/24/2017.
 */

public class FunLearn extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient();
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttp3Client);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder = new Picasso.Builder(this).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.downloader(okHttp3Downloader);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
