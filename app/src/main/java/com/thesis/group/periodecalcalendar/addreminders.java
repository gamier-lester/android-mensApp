package com.thesis.group.periodecalcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class addreminders extends AppCompatActivity {
    private TextView _tvReminders;
    private Spinner _spnrRepeater, _spnrHours, _spnrMinutes;
    private DatabaseHelper db;
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3;
    private String sid;
    ImageView _imgLeft, _imgRight, _imgUp;
    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreminders);

        db = new DatabaseHelper(this);
        SessionManager session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);
        final String suname = user.get(SessionManager.KEY_USERNAME);
        menCategory = db.getMensCat(suname);

        ScrollView _scrlvParent = findViewById(R.id.scrlvParentAddReminders);
        _tvReminders = findViewById(R.id.etRemindersAddReminders);
        Button _btnSubmit = findViewById(R.id.btnSubmitAddReminders);
        _spnrRepeater = findViewById(R.id.spnrRepeatAddReminders);
        _spnrHours = findViewById(R.id.spnrHoursAddReminders);
        _spnrMinutes = findViewById(R.id.spnrMinutesAddReminders);
        _imgLeft = findViewById(R.id.btnLeftAddReminders);
        _imgRight = findViewById(R.id.btnRightAddReminders);
        _imgUp = findViewById(R.id.btnLeftUpAddReminders);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();

        adapter1 = ArrayAdapter.createFromResource(this, R.array.repeat_day, R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrRepeater.setAdapter(adapter1);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.military_hours, R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrHours.setAdapter(adapter2);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.military_minutes, R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrMinutes.setAdapter(adapter3);

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(addreminders.this, reminder.class);
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
                Intent intent = new Intent(addreminders.this, reminder.class);
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
                Intent intent;
                if (menCategory == 1)
                {
                    intent = new Intent(addreminders.this, welcomeuser.class);
                }
                else
                {
                    intent = new Intent(addreminders.this, nonregwelcomeuser.class);
                }
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
    protected void onResume() {

        super.onResume();
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);
    }

    private void onSaveClicked() {
        ReminderModel temp = new ReminderModel();
        temp.setText(_tvReminders.getText().toString());
        temp.setRepeat(convertSpnrValue(_spnrRepeater.getSelectedItem().toString()));
        temp.setHours(_spnrHours.getSelectedItem().toString());
        temp.setMins(_spnrMinutes.getSelectedItem().toString());
        temp.setSound(0);
        db.setReminder(temp, sid);
        this.finish();
    }

    private String convertSpnrValue(String spnrValue) {

        switch (spnrValue) {
            case "Every Monday":
                return "Monday";
            case "Every Tuesday":
                return "Tuesday";
            case "Every Wednesday":
                return "Wednesday";
            case "Every Thursday":
                return "Thursday";
            case "Every Friday":
                return "Friday";
            case "Every Saturday":
                return "Saturday";
            case "Every Sunday":
                return "Sunday";
            default:
                return "Never";
        }
    }
}
