package com.thesis.group.periodecalcalendar;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class updatecalendar extends AppCompatActivity {
    EditText _tfDate;
    Button _btnEdit;
    SessionManager session;
    DatabaseHelper db;
    String sid;
    HashMap<String, String> user;
    DatePickerDialog.OnDateSetListener _mDateSetListener;
    ImageView _imgLeft, _imgRight, _imgUp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecalendar);

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);

        db = new DatabaseHelper(this);

        ScrollView _scrlvParent = findViewById(R.id.scrlvParentUpdateCalendar);
        _tfDate = findViewById(R.id.tfDayUpdateCalendar);
        _tfDate.setShowSoftInputOnFocus(false);
        _btnEdit = findViewById(R.id.btnEditUpdateCalendar);
        _imgLeft = findViewById(R.id.imgLeftUpdateCalendar);
        _imgRight = findViewById(R.id.imgRightUpdateCalendar);
        _imgUp = findViewById(R.id.imgUpUpdateCalendar);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();

        setInputs();

        _btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDateCalendar();
            }
        });

        _tfDate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(updatecalendar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, _mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        _mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                _tfDate.setError(null);
                _tfDate.setText(date);

            }
        };

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(updatecalendar.this, menstrualcalendar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        _imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgRight.setClickable(false);
                Intent intent = new Intent(updatecalendar.this, menstrualcalendar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        _imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgUp.setClickable(false);
                Intent intent = new Intent(updatecalendar.this, welcomeuser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);
    }

    private void setInputs()
    {
        String date = db.getDateCalendar(sid);
        _tfDate.setText(date);
    }

    private void updateDateCalendar()
    {
        String date = _tfDate.getText().toString();
        boolean check = db.updateDateCalendar(date, sid);
        if (check)
        {
            Toast.makeText(getApplicationContext(), "Successfully updated date!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Failed to update date!", Toast.LENGTH_LONG).show();
        }

    }

}
