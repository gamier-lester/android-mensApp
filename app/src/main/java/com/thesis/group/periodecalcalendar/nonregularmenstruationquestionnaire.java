package com.thesis.group.periodecalcalendar;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class nonregularmenstruationquestionnaire extends AppCompatActivity {
    DatabaseHelper db;
    ScrollView _scrlvParent;
    Button _btnSubmit;
    EditText _tfAge, _tfHeight, _tfWeight, _tfLastMensDate;
    Spinner _spnrWorking, _spnrSports, _spnrPills, _spnrBreastFeed;
    SessionManager session;
    DatePickerDialog.OnDateSetListener _mDateSetListener;
    HashMap<String, String> user;
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3, adapter4;
    ServiceMaker sm;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHelper(this);
        boolean checkSession = session.checkLogin();
        if (checkSession)
        {
            Intent i = new Intent(nonregularmenstruationquestionnaire.this, signin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        user = session.getUserDetails();
        String forCheckingId = user.get(SessionManager.KEY_ID);
        boolean checkMens = db.checkNonRegMensQuestionnaire(forCheckingId);
        if (checkMens)
        {
            Intent i = new Intent(nonregularmenstruationquestionnaire.this, nonregwelcomeuser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonregularmenstruationquestionnaire);
        user = session.getUserDetails();
        _scrlvParent = findViewById(R.id.scrlvParentNonRegMens);
        _tfAge = findViewById(R.id.tfAgeNonRegMens);
        _tfHeight = findViewById(R.id.tfHeightNonRegMens);
        _tfWeight = findViewById(R.id.tfWeightNonRegMens);
        _tfLastMensDate = findViewById(R.id.tfMensDateNonRegMens);
        _spnrWorking = findViewById(R.id.spnrWorkingNonRegMens);
        _spnrSports = findViewById(R.id.spnrSportsActiveNonRegMens);
        _spnrPills = findViewById(R.id.spnrTakingPillsNonRegMens);
        _spnrBreastFeed = findViewById(R.id.spnrBreastFeedingNonRegMens);
        _btnSubmit = findViewById(R.id.btnSubmitNonRegMens);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.working_non_reg, R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrWorking.setAdapter(adapter1);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.sports_active_non_reg, R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrSports.setAdapter(adapter2);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.pills_non_reg, R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrPills.setAdapter(adapter3);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.breast_feeding_non_reg, R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrBreastFeed.setAdapter(adapter4);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();
        _tfLastMensDate.setShowSoftInputOnFocus(false);
        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sage = _tfAge.getText().toString();
                int age;
                try
                {
                    age = Integer.parseInt(sage);
                }
                catch (NumberFormatException e)
                {
                    age = 0;
                }

                String sheight = _tfHeight.getText().toString();
                int height;
                try
                {
                    height = Integer.parseInt(sheight);
                }
                catch (NumberFormatException e)
                {
                    height = 0;
                }

                String sweight = _tfWeight.getText().toString();
                int weight;
                try
                {
                    weight = Integer.parseInt(sweight);
                }
                catch (NumberFormatException e)
                {
                    weight = 0;
                }

                String lastMensDate = _tfLastMensDate.getText().toString();
                String spnrWorking = _spnrWorking.getSelectedItem().toString();
                String spnrSports = _spnrSports.getSelectedItem().toString();
                String spnrPills = _spnrPills.getSelectedItem().toString();
                String spnrBreastFeed = _spnrBreastFeed.getSelectedItem().toString();
                String sid = user.get(SessionManager.KEY_ID);
                assert sid != null;
                int id = Integer.parseInt(sid);
                int iSpnrWorking = convertSpnrValue(spnrWorking);
                int iSpnrSports = convertSpnrValue(spnrSports);
                int iSpnrPills = convertSpnrValue(spnrPills);
                int iSpnrBreastFeed = convertSpnrValue(spnrBreastFeed);

                if(checkDataEntered(sage, sheight, sweight, lastMensDate))
                {
                    boolean check = db.setNonRegMensInfo(age, height, weight, iSpnrWorking, iSpnrSports, iSpnrPills, iSpnrBreastFeed, lastMensDate, id);
                    if(check)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(nonregularmenstruationquestionnaire.this);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sm = new ServiceMaker(nonregularmenstruationquestionnaire.this);
                                sm.createService();
                                Toast.makeText(getApplicationContext(), "Successfully Inserted Data!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(nonregularmenstruationquestionnaire.this, nonregwelcomeuser.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("EXIT", true);
                                startActivity(i);
                                finish();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setMessage("Are you sure you want to submit given information?");
                        builder.setTitle("Confirm information");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Unable to insert Data!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill up all the fields!", Toast.LENGTH_LONG).show();
                }
            }
        });

        _tfLastMensDate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(nonregularmenstruationquestionnaire.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, _mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        _mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                _tfLastMensDate.setText(date);
            }
        };
    }

    private int convertSpnrValue(String spnrValue)
    {
        if (spnrValue.equals("yes"))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    private boolean isEmpty(String str)
    {
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered(String age, String height, String weight, String date)
    {
        boolean bol = true;

        if (isEmpty(age))
        {
//            _tfAge.setError("Age is required!");
            bol = false;
        }

        if (isEmpty(height))
        {
//            _tfHeight.setError("Height is required!");
            bol = false;
        }

        if (isEmpty(weight))
        {
//            _tfWeight.setError("Weight is required!");
            bol = false;
        }

        if (isEmpty(date))
        {
//            _tfLastMensDate.setError("Date is required!");
            bol = false;
        }

        return bol;
    }
}
