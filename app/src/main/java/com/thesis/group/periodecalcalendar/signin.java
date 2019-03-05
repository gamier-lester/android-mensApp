package com.thesis.group.periodecalcalendar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class signin extends AppCompatActivity {
    DatabaseHelper db;
    ScrollView _scrlvParent;
    Button _btnSignin, _btnRegister;
    EditText _tfUsername, _tfPassword;
    TextView _tvForgotPassword;
    SessionManager session;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog1, progressDialog2, progressDialog3;
    ServiceMaker sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        boolean checkSession = session.redirectToMain();
        if (checkSession)
        {
            Intent i = new Intent(signin.this, selectmenstruation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHelper(this);
        _scrlvParent = findViewById(R.id.scrlvParentSignIn);
        _tfUsername = findViewById(R.id.tfUserNameSignIn);
        _tfPassword = findViewById(R.id.tfPassWordSignIn);
        _btnSignin = findViewById(R.id.btnLogInSignIn);
        _btnRegister = findViewById(R.id.btnRegisterSignIn);
        _tvForgotPassword = findViewById(R.id.tvForgotPasswordSignIn);
        _scrlvParent.requestFocus();
        _tfUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfUsername.setHint("");
                else
                    _tfUsername.setHint(getString(R.string.tfUsernameSignin));
            }
        });
        _tfPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfPassword.setHint("");
                else
                    _tfPassword.setHint(getString(R.string.tfPasswordSignin));
            }
        });
        _btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = _tfUsername.getText().toString();
                String pass = _tfPassword.getText().toString();
                boolean checkData = checkDataEntered(username, pass);
                if (checkData)
                {
                    boolean verifyLogin = db.verifyLogin(username, pass);
                    if (verifyLogin)
                    {
                        HashMap<String, String> emailAndAccessMap = db.getEmailAndAccess(username);

                        String email = emailAndAccessMap.get(DatabaseHelper.KEY_email);
                        String accessS = emailAndAccessMap.get(DatabaseHelper.KEY_access);
                        assert accessS != null;
                        int access = Integer.parseInt(accessS);

                        if (access == 1)
                        {
                            int id = db.getId(username);
                            session.createLoginSession(id, username);
                            sm = new ServiceMaker(signin.this);
                            sm.createService();
                            Toast.makeText(getApplicationContext(), "Successfully Login!", Toast.LENGTH_LONG).show();
                            Intent loginIntent = new Intent(signin.this, selectmenstruation.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            loginIntent.putExtra("EXIT", true);
                            startActivity(loginIntent);
                            finish();
                        }
                        else
                        {
                            if (isOnline())
                            {
                                progressDialog1 = new ProgressDialog(signin.this);
                                progressDialog1.setTitle("Accessing firebase!");
                                progressDialog1.setMessage("Loading...");
                                progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog1.show();
                                progressDialog1.setCancelable(false);
                                assert email != null;
                                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog1.dismiss();
                                            progressDialog2 = new ProgressDialog(signin.this);
                                            progressDialog2.setTitle("Checking email verification!");
                                            progressDialog2.setMessage("Loading...");
                                            progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressDialog2.show();
                                            progressDialog2.setCancelable(false);
                                            final FirebaseUser user = mAuth.getCurrentUser();
                                            assert user != null;
                                            if (user.isEmailVerified())
                                            {
                                                progressDialog2.dismiss();
                                                Toast.makeText(getApplicationContext(), "Email is already verified!", Toast.LENGTH_LONG).show();
                                                db.updateAccess(username);
                                                FirebaseAuth.getInstance().signOut();
                                                int id = db.getId(username);
                                                session.createLoginSession(id, username);
                                                sm = new ServiceMaker(signin.this);
                                                sm.createService();
                                                Toast.makeText(getApplicationContext(), "Successfully Login!", Toast.LENGTH_LONG).show();
                                                Intent loginIntent = new Intent(signin.this, selectmenstruation.class);
                                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                loginIntent.putExtra("EXIT", true);
                                                startActivity(loginIntent);
                                                finish();
                                            }
                                            else
                                            {
                                                progressDialog2.dismiss();
                                                progressDialog3 = new ProgressDialog(signin.this);
                                                progressDialog3.setTitle("Sending email verification!");
                                                progressDialog3.setMessage("Loading...");
                                                progressDialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                progressDialog3.show();
                                                progressDialog3.setCancelable(false);
                                                user.sendEmailVerification().addOnCompleteListener(signin.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            progressDialog3.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Successfully sent email verification! Please check your email for verification link.", Toast.LENGTH_LONG).show();
                                                            FirebaseAuth.getInstance().signOut();
                                                        }
                                                        else
                                                        {
                                                            progressDialog3.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Failed to send email verification!", Toast.LENGTH_LONG).show();
                                                            FirebaseAuth.getInstance().signOut();
                                                        }
                                                    }
                                                });
                                            }

                                        }
                                        else
                                        {
                                            progressDialog1.dismiss();
                                            Toast.makeText(getApplicationContext(), "Failed to connect to firebase!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Please check internet connection!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong username or password!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to login!", Toast.LENGTH_LONG).show();
                }

            }
        });

        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(signin.this, signup.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.putExtra("EXIT", true);
                startActivity(loginIntent);
                finish();
            }
        });

        _tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }
    private boolean isEmpty(String str)
    {
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered(String username, String password)
    {
        boolean bol = true;

        if (isEmpty(username))
        {
            _tfUsername.setError("Username is required!");
            bol = false;
        }

        if (isEmpty(password))
        {
            _tfPassword.setError("Password is required!");
            bol = false;
        }

        return bol;
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    protected void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(signin.this);
        View promptView = layoutInflater.inflate(R.layout.inputemail, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(signin.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.emailInputDialog);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do here
                        String email1 = editText.getText().toString();
                        HashMap<String, String> emailAndAccessMap2 = db.getEmailAndAccessForgotPassword(email1);
                        String email2 = emailAndAccessMap2.get(DatabaseHelper.KEY_email);
                        String accessS = emailAndAccessMap2.get(DatabaseHelper.KEY_access);

                        assert email2 != null;
                        if (!email2.equals("thisEmailDoesntExist"))
                        {
                            assert accessS != null;
                            if (Integer.valueOf(accessS) == 1)
                            {
                                String password = db.getPassword(email1);
                                if (password.equals("*%&$32435fhfeinvslhklshvje**(*("))
                                {
                                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    sendEmail(email1, password);
                                }

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "This email is not yet verified!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "This email doesnt exist!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void sendEmail(String email, String password) {

        String username = db.getUsername(email);
        String subject = "\"Menstrual Calendar Application Requested Password";
        String body = "Hi "+ username + ",\n" +
                "\n" +
                "You recently requested your password for your menstrual calendar application account.\n" +
                "\n" +
                "\n" +
                password +"\n" +
                "\n" +
                "\n" +
                "Thanks,\n" +
                "- Admin";

        SendMail sm = new SendMail(this, email, subject, body);
        sm.execute();
    }
}
