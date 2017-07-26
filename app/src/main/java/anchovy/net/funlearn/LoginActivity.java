package anchovy.net.funlearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import anchovy.net.funlearn.fragments.LoginFragment;
import anchovy.net.funlearn.fragments.RegisterFragmentPage1;
import anchovy.net.funlearn.fragments.RegisterFragmentPage2;
import anchovy.net.funlearn.fragments.StudentTypeSetupFragment;
import anchovy.net.funlearn.fragments.TeacherTypeSetupFragment;

public class LoginActivity extends AppCompatActivity implements StudentTypeSetupFragment.StudentTypeListener, TeacherTypeSetupFragment.TeacherTypeListener, RegisterFragmentPage2.SignupListener{

    private String jenjang, kelas, kode, rec, password, username, fullname;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String THEME = "theme";

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.login_activity_container, LoginFragment.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        try {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0)
                startActivity(new Intent(this, GetStartedActivity.class));
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (IllegalStateException e) {
            while(getSupportFragmentManager().popBackStackImmediate()) {

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.login_activity_container, new RegisterFragmentPage1()).commit();
        }
    }

    @Override
    public void submitData(String data) {
        try {
            Integer.parseInt(data);
            kelas = data;
        } catch (NumberFormatException e) {
            jenjang = data;
        }

//        Toast.makeText(this, jenjang + " " + kelas, Toast.LENGTH_SHORT).show();
        kode = null;
    }

    @Override
    public void teacherTypeSubmit(String code) {
        kode = code;
    }

    @Override
    public void signUp(Bundle args) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.login_activity_signin_status));
        progress.show();

        final String kodeX = kode;
        final String jenjangX = jenjang;
        final String kelasX = kelas;
        final String photo = "https://firebasestorage.googleapis.com/v0/b/fun-learn-652b6.appspot.com/o/anchovy%20logo.png?alt=media&token=cad3c2fb-8543-4b5f-b7ee-c4372ec0809c";

        if (firebaseAuth.getCurrentUser() == null) {
            final String email = args.getString(EMAIL);
            final String password = args.getString(PASSWORD);
            final String fullname = args.getString(FULLNAME);
            final String username = args.getString(USERNAME);
            rec = email;
            this.password = password;
            this.fullname = fullname;
            this.username = username;

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(firebaseAuth.getCurrentUser().getUid());
                    databaseReference.child("email").setValue(email);
                    databaseReference.child("password").setValue(password);
                    databaseReference.child("username").setValue(username);
                    databaseReference.child("fullname").setValue(fullname);
                    databaseReference.child("photo").setValue(photo);

                    String type = "";

                    if (kode == null) {
                        databaseReference.child("acountType").setValue("student");
                        databaseReference.child("jenjang").setValue(jenjangX);
                        databaseReference.child("kelas").setValue(kelasX);
                        databaseReference.child("code").setValue("null");
                        type = "student";
                    } else {
                        databaseReference.child("acountType").setValue("teacher");
                        databaseReference.child("code").setValue(kodeX);
                        databaseReference.child("jenjang").setValue("null");
                        databaseReference.child("kelas").setValue("null");
                        type = "teacher";
                    }

                    assignStatistic(email, username, fullname, type, photo);

                    sendWelcomeEmail();

                    while(getSupportFragmentManager().popBackStackImmediate()){}

                    Intent main = new Intent(LoginActivity.this, MainActivityStudent.class);
                    startActivity(main);
                    LoginActivity.this.finish();
                    progress.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage() + getResources().getString(R.string.login_activity_internet_error), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        } else {
            final String email = firebaseAuth.getCurrentUser().getEmail();
            final String fullname = args.getString(FULLNAME);
            final String username = args.getString(USERNAME);

            rec = email;
            this.password = "null";
            this.fullname = fullname;
            this.username = username;

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference.child("email").setValue(email);
            databaseReference.child("password").setValue(password);
            databaseReference.child("username").setValue(username);
            databaseReference.child("fullname").setValue(fullname);
            databaseReference.child("photo").setValue(photo);

            String type = "";

            if (kode == null) {
                databaseReference.child("acountType").setValue("student");
                databaseReference.child("jenjang").setValue(jenjangX);
                databaseReference.child("kelas").setValue(kelasX);
                databaseReference.child("code").setValue("null");
                type = "student";
            } else {
                databaseReference.child("acountType").setValue("teacher");
                databaseReference.child("code").setValue(kodeX);
                databaseReference.child("jenjang").setValue("null");
                databaseReference.child("kelas").setValue("null");
                type = "teacher";
            }

            assignStatistic(email, username, fullname, type, photo);

            sendWelcomeEmail();

            while(getSupportFragmentManager().popBackStackImmediate()){}

            Intent main = new Intent(LoginActivity.this, MainActivityStudent.class);
            startActivity(main);
            LoginActivity.this.finish();
            progress.dismiss();
        }
    }

    private void sendWelcomeEmail () {
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
            String content = "Your account detail :\nEmail : " + rec + "\n";
            if (!password.equals("null")) {
                content += "Password : ";
                for (int i = 0; i < password.length(); i++) {
                    if (i == 0) content += password.charAt(i);
                    else if (i == password.length()-1) content += password.charAt(password.length()-1);
                    else content += "*";
                }
                content += "\n";
            }
            content += "Username : " + username + "\n";
            content += "Fullname : " + fullname + "\n";
            if (kode == null) {
                content += "Account type : student\nSchool : " + jenjang + "\nGrade : " + kelas + "\n";
            } else {
                content += "Account type : teacher\nSchool code : " + kode + "\n";
            }
            content += "Should you encounter any problems with signing in, please contact one of the following: \n" +
                    "Email : sup.funlearn@gmail.com (with subject \"Question)\"\nPhone : +62 813 2217 2750\n" +
                    "\t\t\tSincerely,\n\n\n\t\tAnchovy Studios\n\n";

            content += "----------------------------------------------------------------------------------------------\n\n";

            content += "Detail akun Anda :\nEmail : " + rec + "\n";
            if (!password.equals("null")) {
                content += "Kata sandi : ";
                for (int i = 0; i < password.length(); i++) {
                    if (i == 0) content += password.charAt(i);
                    else if (i == password.length()-1) content += password.charAt(password.length()-1);
                    else content += "*";
                }
                content += "\n";
            }
            content += "Nama pengguna : " + username + "\n";
            content += "Nama lengkap : " + fullname + "\n";
            if (kode == null) {
                content += "Tipe akun : murid\nJenjang : " + jenjang + "\nKelas : " + kelas + "\n";
            } else {
                content += "Tipe akun : guru\nKode sekolah : " + kode + "\n";
            }
            content += "Jika anda menemukan masalah dalam melakukan sign in, silahkan hubungi salah satu kontak dibawah ini: \n" +
                    "Email : sup.funlearn@gmail.com (dengan judul \"Question)\"\nPhone : +62 813 2217 2750\n" +
                    "\t\t\tDengan hormat,\n\n\n\t\tAnchovy Studios\n\n";

            content += "----------------------------------------------------------------------------------------------\n\n";

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sup.funlearn@gmail.com"));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
                message.setSubject("[NO REPLY] Thank you for signing up with us | Terima kasih sudah melakukan registrasi");
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
//            Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }

    private void assignStatistic (String email, String username, String fullname, String type, String photo) {
        DatabaseReference statistic = FirebaseDatabase.getInstance().getReference().child("Statistic").child(firebaseAuth.getCurrentUser().getUid());
        statistic.child("email").setValue(email);
        statistic.child("username").setValue(username);
        statistic.child("fullname").setValue(fullname);
        statistic.child("type").setValue(type);
        statistic.child("photo").setValue(photo);
        statistic.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        statistic.child("totalGames").setValue(0);
        statistic.child("totalWin").setValue(0);
        statistic.child("totalLose").setValue(0);
        statistic.child("totalExercise").setValue(0);
        statistic.child("pvpBestScore").setValue(0);
        statistic.child("timeTrialBestScore").setValue(0);
        statistic.child("totalLearn").setValue(0);
        statistic.child("pvpTotalGames").setValue(0);
        statistic.child("pvpTotalWin").setValue(0);
        statistic.child("pvpTotalLose").setValue(0);
        statistic.child("pvpAnswer").setValue(0);
        statistic.child("pvpAnswerTrue").setValue(0);
        statistic.child("pvpAnswerFalse").setValue(0);
        statistic.child("timeTrialTotalGames").setValue(0);
        statistic.child("timeTrialTotalWin").setValue(0);
        statistic.child("timeTrialTotalLose").setValue(0);
        statistic.child("timeTrialAnswer").setValue(0);
        statistic.child("timeTrialAnswerTrue").setValue(0);
        statistic.child("timeTrialAnswerFalse").setValue(0);
    }
}
