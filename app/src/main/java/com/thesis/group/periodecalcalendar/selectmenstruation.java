package com.thesis.group.periodecalcalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class selectmenstruation extends AppCompatActivity {
    DatabaseHelper db;
    SessionManager session;
    Button _btnReg, _btnNonReg;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHelper(this);
        boolean checkSession = session.checkLogin();
        if (checkSession)
        {
            Intent i = new Intent(selectmenstruation.this, signin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            finish();
        }
        user = session.getUserDetails();
        String forCheckingUname = user.get(SessionManager.KEY_USERNAME);
//        Toast.makeText(getApplicationContext(),forCheckingUname,Toast.LENGTH_LONG).show();
        int mensCat = db.getMensCat(forCheckingUname);
        if (mensCat > 0)
        {
            if (mensCat == 1)
            {
                Intent i = new Intent(selectmenstruation.this, regularmenstruationquestionnaire.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("EXIT", true);
                startActivity(i);
                finish();
            }
            else
            {
                Intent i = new Intent(selectmenstruation.this, nonregularmenstruationquestionnaire.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("EXIT", true);
                startActivity(i);
                finish();
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectmenstruation);
        _btnReg = findViewById(R.id.btnRegSelectMens);
        _btnNonReg = findViewById(R.id.btnNonRegSelectMens);
        _btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(selectmenstruation.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = user.get(SessionManager.KEY_USERNAME);
                        db.registerPersonMensCat(1, username);
                        Intent questionnaireIntent = new Intent(selectmenstruation.this, regularmenstruationquestionnaire.class);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        questionnaireIntent.putExtra("EXIT", true);
                        startActivity(questionnaireIntent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setMessage("Are you sure you want to select regular?");
                builder.setTitle("Confirm selected category");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        _btnNonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(selectmenstruation.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = user.get(SessionManager.KEY_USERNAME);
                        db.registerPersonMensCat(2, username);
                        Intent questionnaireIntent = new Intent(selectmenstruation.this, nonregularmenstruationquestionnaire.class);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        questionnaireIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        questionnaireIntent.putExtra("EXIT", true);
                        startActivity(questionnaireIntent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setMessage("Are you sure you want to select non-regular?");
                builder.setTitle("Confirm selected category");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
