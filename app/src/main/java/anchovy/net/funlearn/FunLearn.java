package anchovy.net.funlearn;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

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

//        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
//        int theme = Integer.parseInt(preference.getString("theme", "1"));
//
//        if (theme == 1){
//            setTheme(R.style.FunLearnLightTheme);
//        } else {
//            setTheme(R.style.FunLearnDarkTheme);
//        }

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
