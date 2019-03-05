package com.thesis.group.periodecalcalendar;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

    public class welcomeuser extends AppCompatActivity implements View.OnClickListener {
    CalendarDates cd;
    SessionManager session;
    DatabaseHelper db;
    TextView _tvMyDay, _tvMyCalendar, _tvMyNotes, _tvMyReminders, _tvMyProfile, _tvMyHistory, _tvSettings, _tvLogout;
    ImageView _imgvMyCalendar, _imgvMyNotes, _imgMyReminders, _imgMyProfile, _imgMyHistory,  _imgvSettings, _imgvLogout, _imgLeft, _imgRight, _imgUp;
    ConstraintLayout _cstLMyDay, _cstLMyCalendar, _cstLMyNotes, _cstLMyReminders, _cstLMyProfile, _cstLMyHistory, _cstLSettings, _cstLLogout;
    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        boolean checkSession = session.checkLogin();
        if (checkSession)
        {
            Intent i = new Intent(welcomeuser.this, signin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        cd = new CalendarDates(this);
        cd.test();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeuser);

        if (!checkPermission())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        }

//        sm = new ServiceMaker(this);
//        sm.createService();

        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);

        db = new DatabaseHelper(this);
        boolean checkPic = db.checkPicture(sid);

        if (checkPic)
        {
            CircleImageView imageView = findViewById(R.id.imgProfilePic1);
            String imagePath = db.getPicture(sid);
            displayPic(imagePath, imageView);
        }

        _tvMyDay = findViewById(R.id.tvMyDayWelcomeUser);
        _tvMyCalendar = findViewById(R.id.tvMyCalendarWelcomeUser);
        _tvMyNotes = findViewById(R.id.tvMyNotesWelcomeUser);
        _tvMyReminders = findViewById(R.id.tvMyRemindersWelcomeUser);
        _tvMyProfile =  findViewById(R.id.tvMyEditProfWelcomeUser);
        _tvMyHistory = findViewById(R.id.tvMyHistoryWelcomeUser);
        _tvSettings = findViewById(R.id.tvMySettingsWelcomeUser);
        _tvLogout = findViewById(R.id.tvMyLogoutWelcomeUser);

        _imgvMyCalendar = findViewById(R.id.imgMyCalendarWelcomeUser);
        _imgvMyNotes = findViewById(R.id.imgMyNotesWelcomeUser);
        _imgMyReminders = findViewById(R.id.imgMyRemindersWelcomeUser);
        _imgMyProfile =  findViewById(R.id.imgMyEditProfWelcomeUser);
        _imgMyHistory = findViewById(R.id.imgMyHistoryWelcomeUser);
        _imgvSettings = findViewById(R.id.imgMySettingsWelcomeUser);
        _imgvLogout = findViewById(R.id.imgMyLogoutWelcomeUser);
        _imgLeft = findViewById(R.id.imgLeftRegWelcomeUser);
        _imgRight = findViewById(R.id.imgRightRegWelcomeUser);
        _imgUp = findViewById(R.id.imgUpRegWelcomeUser);

        _cstLMyDay = findViewById(R.id.cstLMyDayWelcomeUser);
        _cstLMyCalendar = findViewById(R.id.cstLMyCalendarWelcomeUser);
        _cstLMyNotes = findViewById(R.id.cstLMyNotesWelcomeUser);
        _cstLMyReminders = findViewById(R.id.cstLMyRemindersWelcomeUser);
        _cstLMyProfile =  findViewById(R.id.cstLMyEditProfWelcomeUser);
        _cstLMyHistory = findViewById(R.id.cstLMyHistoryWelcomeUser);
        _cstLSettings = findViewById(R.id.cstLMySettingWelcomeUser);
        _cstLLogout = findViewById(R.id.cstLMyLogoutWelcomeUser);

        _tvMyDay.setOnClickListener(this);
        _tvMyCalendar.setOnClickListener(this);
        _tvMyNotes.setOnClickListener(this);
        _tvMyReminders.setOnClickListener(this);
        _tvMyProfile.setOnClickListener(this);
        _tvMyHistory.setOnClickListener(this);
        _tvSettings.setOnClickListener(this);
        _tvLogout.setOnClickListener(this);

        _imgvMyCalendar.setOnClickListener(this);
        _imgvMyNotes.setOnClickListener(this);
        _imgMyReminders.setOnClickListener(this);
        _imgMyProfile.setOnClickListener(this);
        _imgMyHistory.setOnClickListener(this);
        _imgvSettings.setOnClickListener(this);
        _imgvLogout.setOnClickListener(this);
        _imgLeft.setOnClickListener(this);
        _imgRight.setOnClickListener(this);
        _imgUp.setOnClickListener(this);

        _cstLMyDay.setOnClickListener(this);
        _cstLMyCalendar.setOnClickListener(this);
        _cstLMyNotes.setOnClickListener(this);
        _cstLMyReminders.setOnClickListener(this);
        _cstLMyProfile.setOnClickListener(this);
        _cstLMyHistory.setOnClickListener(this);
        _cstLSettings.setOnClickListener(this);
        _cstLLogout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        cd = new CalendarDates(this);
        cd.test();
        boolean checkPic = db.checkPicture(sid);
        ImageView imageView = findViewById(R.id.imgProfilePic1);

        enableButtons();

        if (checkPic)
        {
            String imagePath = db.getPicture(sid);
            displayPic(imagePath, imageView);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.tvMyDayWelcomeUser)
        {
            _tvMyDay.setClickable(false);
            _cstLMyDay.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, mycurrentcycle.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyCalendarWelcomeUser)
        {
            _tvMyCalendar.setClickable(false);
            _imgvMyCalendar.setClickable(false);
            _cstLMyCalendar.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, menstrualcalendar.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyNotesWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyRemindersWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, reminder.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyEditProfWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, editprofile.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyHistoryWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, history.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMySettingsWelcomeUser)
        {
            _tvSettings.setClickable(false);
            _imgvSettings.setClickable(false);
            _cstLSettings.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, settings.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyLogoutWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(welcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)welcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONSETTINGS");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(welcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(welcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(broadcast1);
                    alarmManager.cancel(broadcast2);
                    session.logoutUser();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setMessage("Are you sure you want to logout?");
            builder.setTitle("Confirm logout");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (v.getId()==R.id.imgMyCalendarWelcomeUser)
        {
            _tvMyCalendar.setClickable(false);
            _imgvMyCalendar.setClickable(false);
            _cstLMyCalendar.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, menstrualcalendar.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMyNotesWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMyRemindersWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, reminder.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMyEditProfWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, editprofile.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMyHistoryWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, history.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMySettingsWelcomeUser)
        {
            _tvSettings.setClickable(false);
            _imgvSettings.setClickable(false);
            _cstLSettings.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, settings.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgMyLogoutWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(welcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)welcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONSETTINGS");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(welcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(welcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(broadcast1);
                    alarmManager.cancel(broadcast2);
                    session.logoutUser();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setMessage("Are you sure you want to logout?");
            builder.setTitle("Confirm logout");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(v.getId()==R.id.cstLMyDayWelcomeUser)
        {
            _tvMyDay.setClickable(false);
            _cstLMyDay.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, mycurrentcycle.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMyCalendarWelcomeUser)
        {
            _tvMyCalendar.setClickable(false);
            _imgvMyCalendar.setClickable(false);
            _cstLMyCalendar.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, menstrualcalendar.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMyNotesWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMyRemindersWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent loginIntent = new Intent(welcomeuser.this, reminder.class);
            startActivity(loginIntent);
        }
        else if (v.getId()==R.id.cstLMyEditProfWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, editprofile.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMyHistoryWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, history.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMySettingWelcomeUser)
        {
            _tvSettings.setClickable(false);
            _imgvSettings.setClickable(false);
            _cstLSettings.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, settings.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLMyLogoutWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(welcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)welcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONSETTINGS");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(welcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(welcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(broadcast1);
                    alarmManager.cancel(broadcast2);
                    session.logoutUser();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setMessage("Are you sure you want to logout?");
            builder.setTitle("Confirm logout");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else  if (v.getId()==R.id.imgLeftRegWelcomeUser)
        {
            _imgLeft.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, history.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgRightRegWelcomeUser)
        {
            _imgRight.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, mycurrentcycle.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgUpRegWelcomeUser)
        {
            _imgUp.setClickable(false);
            Intent intent = new Intent(welcomeuser.this, welcomeuser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
    }

    private void displayPic(String imagePath, ImageView imgV)
    {
        if (imagePath != null)
        {
            Uri uri;
            uri = Uri.parse(imagePath);
            imgV.setImageURI(uri);
        }
    }

    private boolean checkPermission()
    {
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    private void enableButtons()
    {
        _tvMyDay.setClickable(true);
        _tvMyCalendar.setClickable(true);
        _tvMyNotes.setClickable(true);
        _tvMyReminders.setClickable(true);
        _tvMyProfile.setClickable(true);
        _tvMyHistory.setClickable(true);
        _tvSettings.setClickable(true);

        _imgvMyCalendar.setClickable(true);
        _imgvMyNotes.setClickable(true);
        _imgMyReminders.setClickable(true);
        _imgMyProfile.setClickable(true);
        _imgMyHistory.setClickable(true);
        _imgvSettings.setClickable(true);
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);

        _cstLMyDay.setClickable(true);
        _cstLMyCalendar.setClickable(true);
        _cstLMyNotes.setClickable(true);
        _cstLMyReminders.setClickable(true);
        _cstLMyProfile.setClickable(true);
        _cstLMyHistory.setClickable(true);
        _cstLSettings.setClickable(true);
    }
}
