package anchovy.net.funlearn.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragmentPage2 extends Fragment implements View.OnClickListener {

    private FragmentTabHost mTabHost;

    private Bundle args;

    private SignupListener mListener;

    public RegisterFragmentPage2() {
        // Required empty public constructor
    }

    public static RegisterFragmentPage2 newInstance(Bundle args) {
        RegisterFragmentPage2 fragment = new RegisterFragmentPage2();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_fragment_page2, container, false);

        args = getArguments();

        mTabHost = (FragmentTabHost)view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.login_activity_student))
                        .setIndicator(getResources().getString(R.string.login_activity_student)),
                        StudentTypeSetupFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.login_activity_teacher))
                        .setIndicator(getResources().getString(R.string.login_activity_teacher)),
                TeacherTypeSetupFragment.class, null);

        Button signup = (Button)view.findViewById(R.id.login_activity_signup_fragment2_signin_button);
        Button alreadyHaveAcc = (Button)view.findViewById(R.id.login_activity_signup_fragment2_login_button);
        signup.setOnClickListener(this);
        alreadyHaveAcc.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignupListener) {
            mListener = (SignupListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_signup_fragment2_login_button:
                hideKeyboard(view);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                getActivity().onBackPressed();
                break;
            case R.id.login_activity_signup_fragment2_signin_button :
                hideKeyboard(view);
                mListener.signUp(args);
                break;
        }
    }

    public interface SignupListener {
        void signUp (Bundle args);
    }

    private void hideKeyboard (View view) {
//        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
//                Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
