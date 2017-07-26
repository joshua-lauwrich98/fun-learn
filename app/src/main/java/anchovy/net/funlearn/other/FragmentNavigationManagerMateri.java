package anchovy.net.funlearn.other;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import anchovy.net.funlearn.BuildConfig;
import anchovy.net.funlearn.R;
import anchovy.net.funlearn.fragments.MateriFragmentFactory;

/**
 * @author msahakyan
 */

public class FragmentNavigationManagerMateri implements MateriNavigationManager {

    private static FragmentNavigationManagerMateri sInstance;

    private FragmentManager mFragmentManager;
    private AppCompatActivity mActivity;

    public static FragmentNavigationManagerMateri obtain(AppCompatActivity activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManagerMateri();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(AppCompatActivity activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    private void showFragment(Fragment fragment, boolean allowStateLoss) {
        FragmentManager fm = mFragmentManager;

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.materi_activity_main_container, fragment);

        ft.addToBackStack(null);

        if (allowStateLoss || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        fm.executePendingTransactions();
    }

    @Override
    public void showFragment(String title) {
        showFragment(MateriFragmentFactory.newInstance(title), false);
    }
}
