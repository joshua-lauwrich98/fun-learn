package anchovy.net.funlearn.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anchovy.net.funlearn.LoginActivity;
import anchovy.net.funlearn.MainActivityStudent;
import anchovy.net.funlearn.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LOGIN FRAGMENT";
    private EditText email, password;
    private ImageButton showHide;
    private boolean isPasswordVisible;

    private TextInputLayout emailLayout, passwordLayout;
    private Vibrator vib;
    Animation shake;

    private SignInButton googleSignIn;
    private static final int RC_SIGN_IN = 00001;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progress;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button login = (Button)view.findViewById(R.id.login_activity_login_fragment_signin_button);
        Button forgot = (Button)view.findViewById(R.id.login_activity_login_frag_forgot_password_button);
        Button newAcc = (Button)view.findViewById(R.id.login_activity_login_frag_create_account_button);
        forgot.setOnClickListener(this);
        login.setOnClickListener(this);
        newAcc.setOnClickListener(this);

        showHide = (ImageButton)view.findViewById(R.id.login_activity_login_fragment_show_hide_password);
        showHide.setOnClickListener(this);

        email = (EditText)view.findViewById(R.id.login_activity_login_frag_email_input);

        password = (EditText)view.findViewById(R.id.login_activity_login_frag_password_input);

        emailLayout = (TextInputLayout)view.findViewById(R.id.login_activity_login_frag_email_input_layout);
        passwordLayout = (TextInputLayout)view.findViewById(R.id.login_activity_login_frag_password_input_layout);

        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        vib = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        googleSignIn = (SignInButton)view.findViewById(R.id.login_activity_login_fragment_google_signin);
        googleSignIn.setOnClickListener(this);

        progress = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

        isPasswordVisible = false;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
            }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.activity = (LoginActivity) context;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            progress.setMessage(getResources().getString(R.string.login_activity_signin_status));
            progress.show();

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                progress.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkUserExist();
                            progress.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_login_fragment_show_hide_password :
                hideKeyboard(view);
                //cursor position
                int cursorPosition = password.getSelectionStart();

                //toogle variable
                isPasswordVisible = !isPasswordVisible;

                //change image
                showHide.setImageResource(isPasswordVisible ? R.drawable.visibility_off_icon : R.drawable.visibility_icon);

                //apply input type
                password.setInputType(isPasswordVisible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                //returns cursor pos
                password.setSelection(cursorPosition);
                break;
            case R.id.login_activity_login_fragment_signin_button :
                hideKeyboard(view);
                submitForm();
                break;
            case R.id.login_activity_login_frag_create_account_button :
                hideKeyboard(view);
                changeFragmentRegister();
                break;
            case R.id.login_activity_login_frag_forgot_password_button :
                changeFragmentForgotPass();
                break;
            case R.id.login_activity_login_fragment_google_signin :
                hideKeyboard(view);
                signIn();
                break;
        }
    }

    private void submitForm() {
        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);

        if (!checkEmail()) {
            email.setAnimation(shake);
            email.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        if (!checkPassword()) {
            password.setAnimation(shake);
            password.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        progress.setMessage(getResources().getString(R.string.login_activity_signin_status));
        progress.show();

        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);

        signInFirebase();
    }

    private boolean checkEmail () {
        String emailText = email.getText().toString().trim();
        if (emailText.isEmpty() || !isValidEmail(emailText)) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(getResources().getString(R.string.login_activity_error_layout_email));
            email.setError(getResources().getString(R.string.login_activity_error_email));
            requestFocus(email);
            return false;
        }

        emailLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword () {
        String passwordText = password.getText().toString().trim();
        if (passwordText.isEmpty()) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password));
            requestFocus(password);
            return false;
        } else if (passwordText.length() < 8) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password2));
            requestFocus(password);
            return false;
        } else if (passwordText.length() > 25) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getResources().getString(R.string.login_activity_error_layout_password3));
            requestFocus(password);
            return false;
        }

        passwordLayout.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkUserExist () {
        if (firebaseAuth.getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        Intent main = new Intent(getActivity(), MainActivityStudent.class);
                        startActivity(main);
                        getActivity().finish();
                    } else {
                        changeFragment();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void changeFragment () {
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.login_activity_container, RegisterFragmentPage1.newInstance(firebaseAuth.getCurrentUser().getEmail())).commitAllowingStateLoss();
    }

    private void changeFragmentRegister () {
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.login_activity_container, new RegisterFragmentPage1()).commitAllowingStateLoss();
    }

    private void changeFragmentForgotPass () {
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.login_activity_container, new ForgotPasswordFragment()).commitAllowingStateLoss();
    }

    private void signInFirebase() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUserExist();
                progress.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_credential_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard(View view) {
//        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
//                Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );
    }
}
