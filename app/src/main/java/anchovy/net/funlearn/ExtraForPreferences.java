package anchovy.net.funlearn;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ExtraForPreferences extends AppCompatActivity {

    Session session;
    String username, password, email, uid;
    boolean status = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        password = "null";

        int a = getIntent().getExtras().getInt("extra");

        if (a == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.preference_account_change_pass))
                    .setMessage(getResources().getString(R.string.preference_account_change_pass_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendResetPassword();
                            if (status) {
                                Toast.makeText(ExtraForPreferences.this, "You logged in with a google account. Password reset is not available.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ExtraForPreferences.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.preference_account_logout))
                    .setMessage(getResources().getString(R.string.preference_account_logout_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Intent intent = new Intent(ExtraForPreferences.this, GetStartedActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
    }
    private void sendResetPassword () {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.uid = uid;
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue().toString();
                password = dataSnapshot.child("password").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (password.equals("null")) {
            status = true;
            return;
        }

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
//                Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }
}
