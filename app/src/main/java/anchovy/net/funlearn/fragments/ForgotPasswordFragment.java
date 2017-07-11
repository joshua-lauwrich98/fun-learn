package anchovy.net.funlearn.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import anchovy.net.funlearn.LoginActivity;
import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout emailLayout, usernameLayout;
    private EditText emailInput, usernameInput;
    private DatabaseReference databaseReference;

    private String email, username, password, uid;

    private Session session;

    private Vibrator vib;
    Animation shake;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        emailLayout = (TextInputLayout)view.findViewById(R.id.login_activity_reset_password_email_input_layout);
        usernameLayout = (TextInputLayout)view.findViewById(R.id.login_activity_reset_password_username_input_layout);

        emailInput = (EditText)view.findViewById(R.id.login_activity_reset_password_email_input);
        usernameInput = (EditText)view.findViewById(R.id.login_activity_reset_password_username_input);

        Button confirm = (Button)view.findViewById(R.id.login_activity_reset_password_confirm_button);
        confirm.setOnClickListener(this);

        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        vib = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_activity_reset_password_confirm_button); {
            email = emailInput.getText().toString().trim();
            username = usernameInput.getText().toString().trim();
            cekInput();
        }
    }

    private void cekInput () {
        emailLayout.setErrorEnabled(false);
        usernameLayout.setErrorEnabled(false);

        if (!checkEmail()) {
            emailInput.setAnimation(shake);
            emailInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        if (!checkUsername()) {
            usernameInput.setAnimation(shake);
            usernameInput.startAnimation(shake);
            vib.vibrate(120);
            return;
        }

        emailLayout.setErrorEnabled(false);
        usernameLayout.setErrorEnabled(false);
        submitForm();
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    private void submitForm () {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                List<Object> values = new ArrayList<>(td.values());

                for (int i = 0; i < values.size(); i++) {
                    String [] data = values.get(i).toString().split(",");
//                    Toast.makeText(getActivity(), values.get(i).toString() + "", Toast.LENGTH_SHORT).show();
                    String email1 = data[3].substring(7);
                    String password1 = data[1].substring(10);
                    password = password1;
                    String username1 = data[2].substring(10);
                    uid = Long.toHexString(Double.doubleToLongBits(Math.random()));
//                    Toast.makeText(getActivity(), email1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), password1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), username1, Toast.LENGTH_SHORT).show();
//                    final FirebaseAuth firebase = FirebaseAuth.getInstance();
//                    firebase.signInWithEmailAndPassword(email1, password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            uid = firebase.getCurrentUser().getUid();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    firebase.signOut();

//                    Toast.makeText(getActivity(), email1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), password, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), username1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), data[3].length() + "", Toast.LENGTH_SHORT).show();

                    if (username1.equals(username) && email1.equals(email)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                        dialog.setTitle(getResources().getString(R.string.login_activity_forgot_password))
                                .setMessage(String.format(Locale.ENGLISH, getResources().getString(R.string.login_activity_forgot_password_success), email))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        getActivity().onBackPressed();
                                    }
                                }).show();

                        sendResetPassword();
                    } else {
                        if (i == values.size()-1) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_forgot_password_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    private void sendResetPassword () {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", "true");
        props.put("mail.smtp.user", "sup.funlearn@gmail.com");
        props.put("mail.smtp.password", "bebasterserah69");
        props.put("mail.smtp.host", "smtp.gmail.com");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sup.funlearn@gmail.com", "bebasterserah69");
            }
        });

        session.setDebug(true);
        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String content = "We received a request to reset the password of your account with username " + username;

            if (!password.equals("null")) {
                content += " and current password ";
                for (int i = 0; i < password.length(); i++) {
                    if (i == 0) content += password.charAt(i);
                    else if (i == password.length()-1) content += password.charAt(password.length()-1);
                    else content += "*";
                }
            }
            content += ".\n\nYou can reset your password by clicking the link below.\nfunlearn.anchovy-studios.net/" + uid +
                    "\n\nIf this is not you, kindly ignore this email or contact one of the following: \n" +
                    "Email : sup.funlearn@gmail.com (with subject \"Question)\"\nPhone : +62 813 2217 2750\n" +
                    "\t\t\tSincerely,\n\n\n\t\tAnchovy Studios\n\n";

            content += "----------------------------------------------------------------------------------------------\n\n";

            content += "Kami menerima permintaan untuk mengatur ulang kata sandi pada akun Anda dengan username " + username;

            if (!password.equals("null")) {
                content += " dan password ";
                for (int i = 0; i < password.length(); i++) {
                    if (i == 0) content += password.charAt(i);
                    else if (i == password.length()-1) content += password.charAt(password.length()-1);
                    else content += "*";
                }
            }
            content += ".\n\nAnda bisa mengganti password Anda dengan menggunakan link di bawah ini.\nfunlearn.anchovy-studios.net/" + uid +
                    "\n\nJika ini bukan Anda, silahkan abaikan email ini atau hubungi salah satu kontak di bawah ini: \n" +
                    "Email : sup.funlearn@gmail.com (with subject \"Question)\"\nPhone : +62 813 2217 2750\n" +
                    "\t\t\tSincerely,\n\n\n\t\tAnchovy Studios\n\n";

            content += "----------------------------------------------------------------------------------------------\n\n";

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sup.funlearn@gmail.com"));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("[NO REPLY] Reset Password | Mengatur Ulang Kata Sandi");
                message.setContent(content, "text/plain; charset=utf-8");

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }
}