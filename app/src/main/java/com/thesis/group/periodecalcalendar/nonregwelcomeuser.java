package com.thesis.group.periodecalcalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class nonregwelcomeuser extends AppCompatActivity implements View.OnClickListener {
    SessionManager session;
    DatabaseHelper db;
    TextView _tvMyNotes, _tvMyReminders, _tvMyProfile, _tvMyHistory, _tvLogout;
    ImageView _imgvMyNotes, _imgMyReminders, _imgMyProfile, _imgMyHistory, _imgvLogout, _imgLeft, _imgRight, _imgUp;
    ConstraintLayout _cstLMyNotes, _cstLMyReminders, _cstLMyProfile, _cstLMyHistory, _cstLLogout;
    String sid;
    ServiceMaker sm;
//    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        boolean checkSession = session.checkLogin();
        if (checkSession)
        {
            Intent i = new Intent(nonregwelcomeuser.this, signin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonregwelcomeuser);

        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);

        db = new DatabaseHelper(this);
        boolean checkPic = db.checkPicture(sid);

        if (checkPic)
        {
            CircleImageView imageView = findViewById(R.id.profilePicNonReg);
            String imagePath = db.getPicture(sid);
            displayPic(imagePath, imageView);
        }

        _tvMyNotes = findViewById(R.id.tvMyNotesNonRegWelcomeUser);
        _tvMyReminders = findViewById(R.id.tvMyRemindersNonRegWelcomeUser);
        _tvMyProfile =  findViewById(R.id.tvMyEditProfNonRegWelcomeUser);
        _tvMyHistory = findViewById(R.id.tvMyHistoryNonRegWelcomeUser);
        _tvLogout = findViewById(R.id.tvMyLogoutNonRegWelcomeUser);

        _imgvMyNotes = findViewById(R.id.imgvMyNotesNonRegWelcomeUser);
        _imgMyReminders = findViewById(R.id.imgvRemindersNonRegWelcomeUser);
        _imgMyProfile =  findViewById(R.id.imgvEditProfNonRegWelcomeUser);
        _imgMyHistory = findViewById(R.id.imgvHistoryNonRegWelcomeUser);
        _imgvLogout = findViewById(R.id.imgvLogoutNonRegWelcomeUser);
        _imgLeft = findViewById(R.id.imgLeftNonRegWelcomeUser);
        _imgRight = findViewById(R.id.imgRightNonRegWelcomeUser);
        _imgUp = findViewById(R.id.imgUpNonRegWelcomeUser);

        _cstLMyNotes = findViewById(R.id.cstLMyNotesNonRegWelcomeUser);
        _cstLMyReminders = findViewById(R.id.cstLReminderNonRegWelcomeUser);
        _cstLMyProfile =  findViewById(R.id.cstLEditProfileNonRegWelcomeUser);
        _cstLMyHistory = findViewById(R.id.cstLHistoryNonRegWelcomeUser);
        _cstLLogout = findViewById(R.id.cstLLogoutNonRegWelcomeUser);

        _tvMyNotes.setOnClickListener(this);
        _tvMyReminders.setOnClickListener(this);
        _tvMyProfile.setOnClickListener(this);
        _tvMyHistory.setOnClickListener(this);
        _tvLogout.setOnClickListener(this);

        _imgvMyNotes.setOnClickListener(this);
        _imgMyReminders.setOnClickListener(this);
        _imgMyProfile.setOnClickListener(this);
        _imgMyHistory.setOnClickListener(this);
        _imgvLogout.setOnClickListener(this);
        _imgLeft.setOnClickListener(this);
        _imgRight.setOnClickListener(this);
        _imgUp.setOnClickListener(this);

        _cstLMyNotes.setOnClickListener(this);
        _cstLMyReminders.setOnClickListener(this);
        _cstLMyProfile.setOnClickListener(this);
        _cstLMyHistory.setOnClickListener(this);
        _cstLLogout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        boolean checkPic = db.checkPicture(sid);
        ImageView imageView = findViewById(R.id.profilePicNonReg);

        enableButtons();
        if (checkPic)
        {
            String imagePath = db.getPicture(sid);
            displayPic(imagePath, imageView);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.tvMyNotesNonRegWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyRemindersNonRegWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, reminder.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyEditProfNonRegWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, editprofilenonreg.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.tvMyHistoryNonRegWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent loginIntent = new Intent(nonregwelcomeuser.this, history.class);
            startActivity(loginIntent);
        }
        else if (v.getId()==R.id.tvMyLogoutNonRegWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(nonregwelcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)nonregwelcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONNONREG");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
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
        else if (v.getId()==R.id.imgvMyNotesNonRegWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgvRemindersNonRegWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, reminder.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgvEditProfNonRegWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, editprofilenonreg.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgvHistoryNonRegWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, history.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.imgvLogoutNonRegWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(nonregwelcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)nonregwelcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONNONREG");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
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
        else if (v.getId()==R.id.cstLMyNotesNonRegWelcomeUser)
        {
            _tvMyNotes.setClickable(false);
            _imgvMyNotes.setClickable(false);
            _cstLMyNotes.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLReminderNonRegWelcomeUser)
        {
            _tvMyReminders.setClickable(false);
            _imgMyReminders.setClickable(false);
            _cstLMyReminders.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, reminder.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLEditProfileNonRegWelcomeUser)
        {
            _tvMyProfile.setClickable(false);
            _imgMyProfile.setClickable(false);
            _cstLMyProfile.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, editprofilenonreg.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLHistoryNonRegWelcomeUser)
        {
            _tvMyHistory.setClickable(false);
            _imgMyHistory.setClickable(false);
            _cstLMyHistory.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, history.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.cstLLogoutNonRegWelcomeUser)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(nonregwelcomeuser.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmManager alarmManager = (AlarmManager)nonregwelcomeuser.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONNONREG");
                    Intent intent2 = new Intent("com.thesis.group.periodecalcalendar.DISPLAY_NOTIFICATIONREMINDERS");
                    PendingIntent broadcast1 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(nonregwelcomeuser.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
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
        else  if (v.getId()==R.id.imgLeftNonRegWelcomeUser)
        {
            _imgLeft.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, editprofilenonreg.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgRightNonRegWelcomeUser)
        {
            _imgRight.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, history.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgUpNonRegWelcomeUser)
        {
            _imgUp.setClickable(false);
            Intent intent = new Intent(nonregwelcomeuser.this, nonregwelcomeuser.class);
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

    private void enableButtons()
    {
        _tvMyNotes.setClickable(true);
        _tvMyReminders.setClickable(true);
        _tvMyProfile.setClickable(true);
        _tvMyHistory.setClickable(true);

        _imgvMyNotes.setClickable(true);
        _imgMyReminders.setClickable(true);
        _imgMyProfile.setClickable(true);
        _imgMyHistory.setClickable(true);
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);

        _cstLMyNotes.setClickable(true);
        _cstLMyReminders.setClickable(true);
        _cstLMyProfile.setClickable(true);
        _cstLMyHistory.setClickable(true);
    }
}
