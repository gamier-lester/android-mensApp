package com.thesis.group.periodecalcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

public class settings extends AppCompatActivity {
    Switch _swBeforeMens, _swFertile, _swNotFertile, _swTips, _swMessage;
    Button _btnSubmit;
    SessionManager session;
    DatabaseHelper db;
    HashMap<String, String> user, settingsMap;
    ImageView _imgLeft, _imgRight, _imgUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHelper(this);

        _swBeforeMens = findViewById(R.id.swBeforeMensSettings);
        _swFertile = findViewById(R.id.swWhenFertileSettings);
        _swNotFertile = findViewById(R.id.swNotFertileSettings);
        _swTips = findViewById(R.id.swTipsSettings);
        _swMessage = findViewById(R.id.swTextMessageSettings);
        _btnSubmit = findViewById(R.id.btnSubmitSettings);
        _imgLeft = findViewById(R.id.imgLeftSettings);
        _imgRight = findViewById(R.id.imgRightSettings);
        _imgUp = findViewById(R.id.imgUpSettings);

        user = session.getUserDetails();
        final String sid = user.get(SessionManager.KEY_ID);
        settingsMap = db.getSettings(sid);
        int iMens = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_mens));
        int iFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_fertile));
        int iNonFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_nonFertile));
        int iTips = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_tips));
        int iMessages = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_messages));

        setSwitch(iMens, _swBeforeMens);
        setSwitch(iFertile, _swFertile);
        setSwitch(iNonFertile, _swNotFertile);
        setSwitch(iTips, _swTips);
        setSwitch(iMessages, _swMessage);

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int switch1;
                int switch2;
                int switch3;
                int switch4;
                int switch5;

//                sm.createService();

                if (_swBeforeMens.isChecked())
                {
                    switch1 = 1;
                }
                else
                {
                    switch1 = 0;
                }
                if (_swFertile.isChecked())
                {
                    switch2 = 1;
                }
                else
                {
                    switch2 = 0;
                }
                if (_swNotFertile.isChecked())
                {
                    switch3 = 1;
                }
                else
                {
                    switch3 = 0;
                }
                if (_swTips.isChecked())
                {
                    switch4 = 1;
                }
                else
                {
                    switch4 = 0;
                }
                if (_swMessage.isChecked())
                {
                    switch5 = 1;
                }
                else
                {
                    switch5 = 0;
                }
                boolean settings = db.updateAccountSetting(switch1, switch2, switch3, switch4, switch5, sid);
                if (settings)
                {
                    Toast.makeText(getApplicationContext(), "Successfully saved settings!", Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(settings.this, welcomeuser.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.putExtra("EXIT", true);
                    startActivity(loginIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to save settings!", Toast.LENGTH_LONG).show();
                }
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(settings.this, editprofile.class);
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
                Intent intent = new Intent(settings.this, history.class);
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
                Intent intent = new Intent(settings.this, welcomeuser.class);
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

    private void setSwitch(int sw, Switch _switch)
    {
        if (sw == 1)
        {
            _switch.setChecked(true);
        }
        else
        {
            _switch.setChecked(false);
        }
    }
}
