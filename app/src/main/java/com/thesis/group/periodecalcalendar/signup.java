package com.thesis.group.periodecalcalendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class signup extends AppCompatActivity {
    SessionManager session;
    DatabaseHelper db;
    Button _btnSubmit;
    CheckBox _chbTerms;
    EditText  _tfRUsername, _tfRPassword, _tfVerifyPassword, _tfPhone, _tfEmail, _tfBirthDate;
    ScrollView _scrlvParent;
    TextView _chbLink, _tvLoginLink;
    DatePickerDialog.OnDateSetListener _mDateSetListener;
    DatabaseReference databaseUsers;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        boolean checkSession = session.redirectToMain();
        if (checkSession)
        {
            Intent i = new Intent(signup.this, welcomeuser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        db = new DatabaseHelper(this);
        _scrlvParent = findViewById(R.id.scrlvParentRegistration);
        _btnSubmit = findViewById(R.id.btnSubmitRegistration);
        _tfRUsername = findViewById(R.id.tfUsernameRegistration);
        _tfRPassword = findViewById(R.id.tfPasswordRegistration);
        _tfVerifyPassword = findViewById(R.id.tfVerifyPasswordRegistration);
        _tfPhone = findViewById(R.id.tfPhoneRegistration);
        _tfEmail = findViewById(R.id.tfEmailRegistration);
        _tfBirthDate = findViewById(R.id.tfParentEmailRegistration);
        _chbTerms = findViewById( R.id.chbTermsRegistration);
        _chbLink = findViewById(R.id.chbLinkRegistration);
        _tvLoginLink = findViewById(R.id.tvLoginLinkRegistration);
        _btnSubmit.setEnabled(false);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();
        _tfBirthDate.setShowSoftInputOnFocus(false);


        _tfRUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfRUsername.setHint("");
                else
                    _tfRUsername.setHint(getString(R.string.tfUsernameSignup));
            }
        });
        _tfRPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfRPassword.setHint("");
                else
                    _tfRPassword.setHint(getString(R.string.tfPasswordSignup));
            }
        });
        _tfVerifyPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _tfVerifyPassword.setHint("");
                }
                else {
                    _tfVerifyPassword.setHint(getString(R.string.tfVerifyPasswordSignup));
                }
            }
        });
        _tfPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfPhone.setHint("");
                else
                    _tfPhone.setHint(getString(R.string.tfPhoneSignup));
            }
        });
        _tfEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfEmail.setHint("");
                else
                    _tfEmail.setHint(getString(R.string.tfEmailSignup));
            }
        });
        _tfBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    _tfBirthDate.setHint("");
                else
                    _tfBirthDate.setHint(getString(R.string.tfParentEmailSignup));
            }
        });
        _chbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    _btnSubmit.setBackground(getResources().getDrawable(R.drawable.registerbutton));
                    _btnSubmit.setEnabled(true);
                }
                else
                {
                    _btnSubmit.setBackground(getResources().getDrawable(R.drawable.registerbuttondisable));
                    _btnSubmit.setEnabled(false);
                }

            }
        });
        _chbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = _tfRUsername.getText().toString();
                String password = _tfRPassword.getText().toString();
                String verifypassword = _tfVerifyPassword.getText().toString();
                String phone = _tfPhone.getText().toString();
                String email = _tfEmail.getText().toString();
                int access = 0;
                String birthdate = _tfBirthDate.getText().toString();
                Date c = Calendar.getInstance().getTime();
                String dateregistered = DateFormat.getDateInstance().format(c);
//                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//                String dateregistered = df.format(c);
                int menstrualcat = 0;

                if(checkDataEntered(username, password, verifypassword, phone, email, birthdate))
                {
                    if (isOnline())
                    {
                        createAcc(username, password, verifypassword, phone, email, birthdate, access, dateregistered, menstrualcat);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Unable to register please check internet connectivity!", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to register!", Toast.LENGTH_LONG).show();
                }
            }
        });
        _tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(signup.this, signin.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.putExtra("EXIT", true);
                startActivity(loginIntent);
                finish();
            }
        });
        _tfBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(signup.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, _mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        _mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                _tfBirthDate.setError(null);
                _tfBirthDate.setText(date);

            }
        };
    }

    private void openDialog()
    {
        TermsDialog termsDialog = new TermsDialog();
        termsDialog.show(getSupportFragmentManager(), "Terms and Condition dialog");
    }

    private boolean isEmail(String email)
    {
        return (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isEmpty(String str)
    {
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered(String username, String password, String verifyPassword, String phone, String email, String birthDate)
    {
        boolean bol = true;

        if (isEmpty(username))
        {
            _tfRUsername.setError("Username is required!");
            bol = false;
        }
        else
        {
            if (username.length() <6 )
            {
                _tfRUsername.setError("Username must be at least six characters in length!");
                bol = false;
            }
            else
            {
                boolean cursor = db.checkUsername(username);
                if (cursor)
                {
                    _tfRUsername.setError("Username already exist!");
                    bol = false;
                }
            }
        }

        if (isEmpty(password))
        {
            _tfRPassword.setError("Password is required!");
            bol = false;
        }
        else
        {
            if (password.length() <6 )
            {
                _tfRPassword.setError("Password must be at least six characters in length!");
                bol = false;
            }

            if (password.length() >8 )
            {
                _tfRPassword.setError("Password must not be greater than eight characters in length!");
                bol = false;
            }
        }

        if (isEmpty(verifyPassword))
        {
            _tfVerifyPassword.setError("Verify Password is required!");
            bol = false;
        }
        else
        {
            if (!password.equals(verifyPassword))
            {
                _tfVerifyPassword.setError("Verify Password doesnt match password!");
                bol = false;
            }
        }

        if (isEmpty(phone))
        {
            _tfPhone.setError("Contact number is required!");
            bol = false;
        }

        if (isEmpty(email))
        {
            _tfEmail.setError("Email is required!");
            bol = false;
        }
        else
        {
            if (isEmail(email))
            {
                _tfEmail.setError("Enter valid email!");
                bol = false;
            }
        }

        if (isEmpty(birthDate))
        {
            _tfBirthDate.setError("Birthdate is required!");
            bol = false;
        }

        return bol;
    }

    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i)
        {
            View view = group.getChildAt(i);
            if (view instanceof EditText)
            {
                ((EditText) view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

//    private void addUserFireBase(final String username, final String password, final String verifypassword, final String contact, final String email, final String birthdate, final int access, final String dateregistered, final int menstrualcat)
//    {
//        String id = databaseUsers.push().getKey();
//        User user = new User(username, password, contact, birthdate, email);
//        if (id != null) {
//            databaseUsers.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    boolean check = db.registerPerson(username, password, verifypassword, contact, email, birthdate, access, dateregistered, menstrualcat);
//                    if(check)
//                    {
//                        clearForm((ViewGroup) findViewById(R.id.scrlvParentRegistration));
//                        Toast.makeText(getApplicationContext(), "Registered successfully!", Toast.LENGTH_LONG).show();
//                        _scrlvParent.requestFocus();
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(), "Error inserting data!", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "Error connecting to firebase!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(), "Error cant get unique id!", Toast.LENGTH_LONG).show();
//        }
//    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

//    private void checkUserNameFB(final String username, final String password, final String verifypassword, final String contact, final String email, final String birthdate, final int access, final String dateregistered, final int menstrualcat)
//    {
//        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("userUserName").equalTo(username);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getChildrenCount() > 0)
//                {
//                    Toast.makeText(getApplicationContext(), "Username already exist!", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    addUserFireBase(username, password, verifypassword, contact, email, birthdate, access, dateregistered, menstrualcat);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void createAcc(final String username, final String password, final String verifypassword, final String contact, final String email, final String birthdate, final int access, final String dateregistered, final int menstrualcat)
    {
        progressDialog = new ProgressDialog(signup.this);
        progressDialog.setTitle("Registering email to firebase");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    boolean check = db.registerPerson(username, password, verifypassword, contact, email, birthdate, access, dateregistered, menstrualcat);
                    if(check)
                    {
                        clearForm((ViewGroup) findViewById(R.id.scrlvParentRegistration));
                        Toast.makeText(getApplicationContext(), "Registered successfully!", Toast.LENGTH_LONG).show();
                        _scrlvParent.requestFocus();
                        Intent loginIntent = new Intent(signup.this, signin.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        loginIntent.putExtra("EXIT", true);
                        startActivity(loginIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error inserting data!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        Toast.makeText(getApplicationContext(), "Email already exist!", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to register email to firebase!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
