package anchovy.net.funlearn;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import java.util.Locale;

/**
 * Created by DarKnight98 on 7/25/2017.
 */

public class SettingActivity extends PreferenceActivity {

    private static final String THEME = "theme";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = Integer.parseInt(preference.getString(THEME, "1"));

        if (theme == 1){
            setTheme(R.style.FunLearnLightTheme);
        } else {
            setTheme(R.style.FunLearnDarkTheme);
        }
        super.onCreate(savedInstanceState);

        if (theme == 1) {
            if (Build.VERSION.SDK_INT >= 23) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar, getTheme()));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar));
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar2, getTheme()));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.navigationBar2));
                }
            }
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    public static class SettingsFragment extends PreferenceFragment {

        private static final String NOTIFICATION_SWITCH_KEY = "notification";
        private static final String NOTIFICATION_RINGHTONE_KEY = "ringtone";
        private static final String NOTIFICATION_VIBRATE_KEY = "vibrate";
        private static final String NOTIFICATION_VOICE_KEY = "voice";
        private static final String NOTIFICATION_LANGUAGE_KEY = "language";
        private static final String NOTIFICATION_THEME_KEY = "theme";
        private static final String NOTIFICATION_LOGOUT_KEY = "logout";
        private static final String NOTIFICATION_CHANGE_PASS_KEY = "change_pass";


        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        // listener implementation
                        if (key.equals(getResources().getString(R.string.preference_display_theme_key))) {
                            getActivity().recreate();
                        } else if (key.equals(getResources().getString(R.string.preference_display_language_key))) {
                            if(prefs.getString(key, "").equals("1")) {
                                Locale myLocale = new Locale("id");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                                getActivity().recreate();
                            } else {
                                Locale myLocale = new Locale("en");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                                getActivity().recreate();
                            }

                        } else if (key.equals(getResources().getString(R.string.preference_notification_ringtone_key))) {
                            Preference pref = findPreference(NOTIFICATION_RINGHTONE_KEY);
                            String text = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(NOTIFICATION_RINGHTONE_KEY, "DEFAULT_RINGTONE_URI");
                            Uri ringtoneUri = Uri.parse(text);
                            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
                            String name = ringtone.getTitle(getActivity());
                            pref.setSummary(name);
                        }
                    }
                };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            Preference pref = findPreference(NOTIFICATION_THEME_KEY);
            String text = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(NOTIFICATION_THEME_KEY, "1");
            if (text.equals("1")) pref.setSummary(getResources().getStringArray(R.array.preference_theme_item)[0]);
            else pref.setSummary(getResources().getStringArray(R.array.preference_theme_item)[1]);

            pref = findPreference(NOTIFICATION_LANGUAGE_KEY);
            text = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(NOTIFICATION_LANGUAGE_KEY, "1");
            if (text.equals("1")) pref.setSummary(getResources().getStringArray(R.array.preference_language_item)[0]);
            else pref.setSummary(getResources().getStringArray(R.array.preference_language_item)[1]);

            pref = findPreference(NOTIFICATION_RINGHTONE_KEY);
            text = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(NOTIFICATION_RINGHTONE_KEY, "DEFAULT_RINGTONE_URI");
            Uri ringtoneUri = Uri.parse(text);
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
            String name = ringtone.getTitle(getActivity());
            pref.setSummary(name);

            pref = findPreference(NOTIFICATION_CHANGE_PASS_KEY);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), ExtraForPreferences.class);
                    intent.putExtra("extra", 0);
                    startActivity(intent);

                    return true;
                }
            });

            pref = findPreference(NOTIFICATION_LOGOUT_KEY);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), ExtraForPreferences.class);
                    intent.putExtra("extra", 1);
                    startActivity(intent);

                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(listener);
        }
    }
}
