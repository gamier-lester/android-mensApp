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
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class regularmenstruationquestionnaire extends AppCompatActivity {
    DatabaseHelper db;
    ScrollView _scrlvParent;
    ServiceMaker sm;
    Button _btnSubmit;
    EditText _tfAge, _tfHeight, _tfWeight, _tfLastMensDate, _tfDelayCounts;
    Spinner _spnrPeriodLength, _spnrChanceDelay,  _spnrWorking, _spnrSports, _spnrStatus, _spnrPills, _spnrCramps;
    SessionManager session;
    DatePickerDialog.OnDateSetListener _mDateSetListener;
    HashMap<String, String> user;
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3, adapter4, adapter5, adapter6, adapter7;
//    RelativeLayout _rltvLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHelper(this);
        boolean checkSession = session.checkLogin();
        if (checkSession)
        {
            Intent i = new Intent(regularmenstruationquestionnaire.this, signin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        user = session.getUserDetails();
        String forCheckingId = user.get(SessionManager.KEY_ID);
        boolean checkMens = db.checkRegMensQuestionnaire(forCheckingId);
        if (checkMens)
        {
            Intent i = new Intent(regularmenstruationquestionnaire.this, welcomeuser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regularmenstruationquestionnaire);
        _scrlvParent = findViewById(R.id.scrlvParentRegMens);
        _tfAge = findViewById(R.id.tfAgeRegMens);
        _tfHeight = findViewById(R.id.tfHeightRegMens);
        _tfWeight = findViewById(R.id.tfWeightRegMens);
        _tfLastMensDate = findViewById(R.id.tfLastMensRegMens);
        _tfDelayCounts = findViewById(R.id.tfDelayCountsRegMens);
        _spnrPeriodLength = findViewById(R.id.spnrPeriodLengthRegMens);
        _spnrChanceDelay = findViewById(R.id.spnrMensDelayRegMens);
        _spnrWorking = findViewById(R.id.spnrWorkingRegMens);
        _spnrSports = findViewById(R.id.spnrSportsActiveRegMens);
        _spnrStatus = findViewById(R.id.spnrStatusRegMens);
        _spnrPills = findViewById(R.id.spnrTakingPillsRegMens);
        _spnrCramps = findViewById(R.id.spnrStomachCrampsRegMens);
        _btnSubmit = findViewById(R.id.btnSubmitRegMens);
//        _rltvLayout = findViewById(R.id.cnstLayoutRegularQuestionnaire);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.menstrual_period_length_reg, R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrPeriodLength.setAdapter(adapter1);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.chance_of_menstrual_delay_reg, R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrChanceDelay.setAdapter(adapter2);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.working_reg, R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrWorking.setAdapter(adapter3);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.sports_active_reg, R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrSports.setAdapter(adapter4);
        adapter5 = ArrayAdapter.createFromResource(this, R.array.status_reg, R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrStatus.setAdapter(adapter5);
        adapter6 = ArrayAdapter.createFromResource(this, R.array.taking_pills_reg, R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrPills.setAdapter(adapter6);
        adapter7 = ArrayAdapter.createFromResource(this, R.array.stomach_cramps_reg, R.layout.simple_spinner_item);
        adapter7.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrCramps.setAdapter(adapter7);
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
                double height;
                try
                {
                    height = Double.parseDouble(sheight);
                }
                catch (NumberFormatException e)
                {
                    height = 0;
                }

                String sweight = _tfWeight.getText().toString();
                double weight;
                try
                {
                    weight = Double.parseDouble(sweight);
                }
                catch (NumberFormatException e)
                {
                    weight = 0;
                }

                String sdelayCounts = _tfDelayCounts.getText().toString();
                int delayCounts;
                try
                {
                    delayCounts = Integer.parseInt(sdelayCounts);
                }
                catch (NumberFormatException e)
                {
                    delayCounts = 0;
                }

                String lastMensDate = _tfLastMensDate.getText().toString();
                int spnrPeriodLength =  Integer.parseInt(_spnrPeriodLength.getSelectedItem().toString());
                String spnrChanceDelay = _spnrChanceDelay.getSelectedItem().toString();
                String spnrWorking = _spnrWorking.getSelectedItem().toString();
                String spnrSports = _spnrSports.getSelectedItem().toString();
                String spnrStatus = _spnrStatus.getSelectedItem().toString();
                String spnrPills = _spnrPills.getSelectedItem().toString();
                String spnrCramps = _spnrCramps.getSelectedItem().toString();
                String sid = user.get(SessionManager.KEY_ID);
                assert sid != null;
                int id = Integer.parseInt(sid);
                int iSpnrChanceDelay = convertSpnrValue(spnrChanceDelay);
                int iSpnrWorking = convertSpnrValue(spnrWorking);
                int iSpnrSports = convertSpnrValue(spnrSports);
                int iSpnrPills = convertSpnrValue(spnrPills);
                int iSpnrCramps = convertSpnrValue(spnrCramps);

                if(checkDataEntered(sage, sheight, sweight, lastMensDate, iSpnrChanceDelay, sdelayCounts))
                {
                    boolean check = db.setRegMensInfo(age, height, weight, spnrPeriodLength, lastMensDate, iSpnrChanceDelay, delayCounts, iSpnrWorking, iSpnrSports, spnrStatus, iSpnrPills, iSpnrCramps, id);
                    boolean settings = db.setAccountSetting(0, 0,0,0, 0, id);
                    boolean counter = db.setCounter(12, id);
                    if(check && counter && settings)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(regularmenstruationquestionnaire.this);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sm = new ServiceMaker(regularmenstruationquestionnaire.this);
                                sm.createService();
                                Toast.makeText(getApplicationContext(), "Successfully Inserted Data!", Toast.LENGTH_LONG).show();
                                Intent welcomeIntent = new Intent(regularmenstruationquestionnaire.this, welcomeuser.class);
                                welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                welcomeIntent.putExtra("EXIT", true);
                                startActivity(welcomeIntent);
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

                DatePickerDialog dialog = new DatePickerDialog(regularmenstruationquestionnaire.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, _mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        _mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                _tfLastMensDate.setError(null);
                _tfLastMensDate.setText(date);

            }
        };

        _spnrChanceDelay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = _spnrChanceDelay.getSelectedItem().toString();
                if (selectedItem.equals("yes"))
                {
                    _tfDelayCounts.setInputType(InputType.TYPE_CLASS_NUMBER);
                    _tfDelayCounts.setVisibility(View.VISIBLE);
                }
                else
                {
                    _tfDelayCounts.getText().clear();
                    _tfDelayCounts.setInputType(InputType.TYPE_NULL);
                    _tfDelayCounts.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private boolean checkDataEntered(String age, String height, String weight, String date, int delay,  String delayCount)
    {
        boolean bol = true;

        if (isEmpty(age))
        {
            bol = false;
        }

        if (isEmpty(height))
        {
            bol = false;
        }

        if (isEmpty(weight))
        {
            bol = false;
        }

        if (isEmpty(date))
        {
            bol = false;
        }

        if (delay == 1)
        {
            if (isEmpty(delayCount))
            {
                bol = false;
            }
        }

        return bol;
    }
}
