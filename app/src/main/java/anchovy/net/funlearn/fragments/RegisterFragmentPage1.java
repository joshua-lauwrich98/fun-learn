package anchovy.net.funlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import anchovy.net.funlearn.R;

public class RegisterFragmentPage1 extends Fragment implements View.OnClickListener, TextWatcher{

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";

    private String email, password, username, fullname;
    private EditText emailInput, passwordInput, usernameInput, fullnameInput;
    private TextInputLayout emailLayout, passwordLayout, usernameLayout, fullnameLayout;

    private Vibrator vib;
    Animation shake;

    private TextView passwordStatus;
    private ProgressBar passwordIndikator;

    private boolean isPasswordVisible;
    private ImageButton showHide;

    public RegisterFragmentPage1() {
        // Required empty public constructor
    }

    public static RegisterFragmentPage1 newInstance(String email) {
        RegisterFragmentPage1 fragment = new RegisterFragmentPage1();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_fragment_page1, container, false);

        emailInput = (EditText)view.findViewById(R.id.login_activity_signup_fragment1_email_input);
        passwordInput = (EditText)view.findViewById(R.id.login_activity_signup_fragment1_password_input);
        usernameInput = (EditText)view.findViewById(R.id.login_activity_signup_fragment1_username_input);
        fullnameInput = (EditText)view.findViewById(R.id.login_activity_signup_fragment1_fullname_input);

        showHide = (ImageButton)view.findViewById(R.id.login_activity_signup_fragment1_hide_show_password);
        showHide.setOnClickListener(this);

        passwordIndikator = (ProgressBar)view.findViewById(R.id.login_activity_login_fragment_proggressbar);

        passwordStatus = (TextView)view.findViewById(R.id.login_activity_login_fragment_text_watcher);

        passwordInput.addTextChangedListener(this);

        emailLayout = (TextInputLayout)view.findViewById(R.id.login_activity_signup_fragment1_email_input_layout);
        passwordLayout = (TextInputLayout)view.findViewById(R.id.login_activity_signup_fragment1_password_input_layout);
        usernameLayout = (TextInputLayout)view.findViewById(R.id.login_activity_signup_fragment1_username_input_layout);
        fullnameLayout = (TextInputLayout)view.findViewById(R.id.login_activity_signup_fragment1_fullname_input_layout);

        Button next = (Button)view.findViewById(R.id.login_activity_signup_fragment1_next_button);
        Button alreadyHave = (Button)view.findViewById(R.id.login_activity_signup_fragment1_login_button);
        next.setOnClickListener(this);
        alreadyHave.setOnClickListener(this);

        if (getArguments() != null) {
            emailInput.setText(getArguments().getString(EMAIL));
            emailInput.setInputType(InputType.TYPE_NULL);
            emailInput.setFocusable(false);
            emailInput.setFocusableInTouchMode(false);
            emailInput.setClickable(false);

            passwordLayout.setVisibility(View.GONE);
            showHide.setVisibility(View.GONE);
            passwordIndikator.setVisibility(View.GONE);
            passwordStatus.setVisibility(View.GONE);
            passwordInput.setText("08081998");
        }

        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        vib = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        isPasswordVisible = false;

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_signup_fragment1_next_button :
                hideKeyboard(view);
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();
                username = usernameInput.getText().toString().trim();
                fullname = fullnameInput.getText().toString().trim();
                submitForm();
                break;
            case R.id.login_activity_signup_fragment1_login_button :
                hideKeyboard(view);
                getActivity().onBackPressed();
                break;
            case R.id.login_activity_signup_fragment1_hide_show_password :
                //cursor position
                int cursorPosition = passwordInput.getSelectionStart();

                //toogle variable
                isPasswordVisible = !isPasswordVisible;

                //change image
                showHide.setImageResource(isPasswordVisible ? R.drawable.visibility_off_icon : R.drawable.visibility_icon);

                //apply input type
                passwordInput.setInputType(isPasswordVisible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                //returns cursor pos
                passwordInput.setSelection(cursorPosition);
                break;
        }
    }

    private void submitForm() {
        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        usernameLayout.setErrorEnabled(false);
        fullnameLayout.setErrorEnabled(false);

        if (!checkEmail()) {
            emailInput.setAnimation(shake);
            emailInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        if (!checkPassword()) {
            passwordInput.setAnimation(shake);
            passwordInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        if (!checkUsername()) {
            usernameInput.setAnimation(shake);
            usernameInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        if (!checkFullname()) {
            fullnameInput.setAnimation(shake);
            fullnameInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        usernameLayout.setErrorEnabled(false);
        fullnameLayout.setErrorEnabled(false);
        changeFragment();
    }

    private boolean checkEmail () {
        if (email.isEmpty() || !isValidEmail(email)) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(getResources().getString(R.string.login_activity_error_layout_email));
            emailInput.setError(getResources().getString(R.string.login_activity_error_email));
            requestFocus(emailInput);
            return false;
        }

        emailLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword () {
        if (password.isEmpty()) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password));
            requestFocus(passwordInput);
            return false;
        } else if (password.length() < 8) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password2));
            requestFocus(passwordInput);
            return false;
        } else if (password.length() > 25) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password3));
            requestFocus(passwordInput);
            return false;
        }

        passwordLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkUsername() {
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setErrorEnabled(true);
            usernameLayout.setError(getResources().getString(R.string.login_activity_error_layout_username));
            requestFocus(usernameInput);
            return false;
        }

        usernameLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkFullname () {
        if (TextUtils.isEmpty(fullname)) {
            fullnameLayout.setErrorEnabled(true);
            fullnameLayout.setError(getResources().getString(R.string.login_activity_error_layout_fullname));
            requestFocus(fullnameInput);
            return false;
        } else if (fullname.length() < 2) {
            fullnameLayout.setErrorEnabled(true);
            fullnameLayout.setError(getResources().getString(R.string.login_activity_error_layout_fullname2));
            requestFocus(fullnameInput);
            return false;
        }

        fullnameLayout.setErrorEnabled(false);
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void changeFragment() {
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        args.putString(PASSWORD, password);
        args.putString(USERNAME, username);
        args.putString(FULLNAME, fullname);

        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.login_activity_container, RegisterFragmentPage2.newInstance(args))
                .addToBackStack("tag").commitAllowingStateLoss();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() < 10) {
            if (Build.VERSION.SDK_INT < 21) {
//                Drawable wrapDrawable = DrawableCompat.wrap(passwordIndikator.getIndeterminateDrawable());
//                DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.password_weak));
//                passwordIndikator.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
                passwordIndikator.getIndeterminateDrawable().setColorFilter(
                        getResources().getColor(R.color.password_weak),
                        PorterDuff.Mode.SRC_IN);
                passwordIndikator.getProgressDrawable().setColorFilter(getResources().getColor(R.color.password_weak), PorterDuff.Mode.SRC_IN);
            } else {
//                passwordIndikator.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.password_weak), PorterDuff.Mode.SRC_IN);
                passwordIndikator.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.password_weak)));
            }
            passwordStatus.setText(R.string.login_activity_status_weak);
        } else if (editable.length() < 17) {
            if (Build.VERSION.SDK_INT < 21) {
//                Drawable wrapDrawable = DrawableCompat.wrap(passwordIndikator.getIndeterminateDrawable());
//                DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.password_normal));
//                passwordIndikator.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));

                passwordIndikator.getIndeterminateDrawable().setColorFilter(
                        getResources().getColor(R.color.password_normal),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                passwordIndikator.getProgressDrawable().setColorFilter(getResources().getColor(R.color.password_normal), PorterDuff.Mode.SRC_IN);
            } else {
//                passwordIndikator.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.password_normal), PorterDuff.Mode.SRC_IN);
                passwordIndikator.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.password_normal)));
            }
            passwordStatus.setText(R.string.login_activity_status_normal);
        } else {
            if (Build.VERSION.SDK_INT < 21) {
//                Drawable wrapDrawable = DrawableCompat.wrap(passwordIndikator.getIndeterminateDrawable());
//                DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.password_strong));
//                passwordIndikator.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));

                passwordIndikator.getIndeterminateDrawable().setColorFilter(
                        getResources().getColor(R.color.password_strong),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                passwordIndikator.getProgressDrawable().setColorFilter(getResources().getColor(R.color.password_strong), PorterDuff.Mode.SRC_IN);
            } else {
//                passwordIndikator.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.password_strong), PorterDuff.Mode.SRC_IN);
                passwordIndikator.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.password_strong)));
            }
            passwordStatus.setText(R.string.login_activity_status_strong);
        }
        passwordIndikator.setProgress(editable.length());
    }

    private void hideKeyboard(View view) {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
//                Activity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}