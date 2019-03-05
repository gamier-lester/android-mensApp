package com.thesis.group.periodecalcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

public class addnotes extends AppCompatActivity {
    private TextView _tvMemo;
    private DatabaseHelper db;
    private Memo memo;
    private String sid;
    ImageView _imgLeft, _imgRight, _imgUp;
    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);

        db = new DatabaseHelper(this);
        SessionManager session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);
        final String suname = user.get(SessionManager.KEY_USERNAME);
        menCategory = db.getMensCat(suname);

        ScrollView _scrlvParent = findViewById(R.id.scrlvParentAddNotes);
        _tvMemo = findViewById(R.id.etNotesAddNotes);
        Button _btnSubmit = findViewById(R.id.btnSubmitAddNotes);
        _imgLeft = findViewById(R.id.btnLeftAddNotes);
        _imgRight = findViewById(R.id.btnRightAddNotes);
        _imgUp = findViewById(R.id.btnLeftUpAddNotes);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("MEMO");
            if(memo != null) {
                this._tvMemo.setText(memo.getText());
            }
        }

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
                Intent intent = new Intent(addnotes.this, notes.class);
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
                Intent intent = new Intent(addnotes.this, notes.class);
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
                    intent = new Intent(addnotes.this, welcomeuser.class);
                }
                else
                {
                    intent = new Intent(addnotes.this, nonregwelcomeuser.class);
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

    public void onSaveClicked() {
        if(memo == null) {
            // Add new memo
            Memo temp = new Memo();
            temp.setText(_tvMemo.getText().toString());
            db.setMemo(temp, sid);
        } else {
            // Update the memo
            memo.setText(_tvMemo.getText().toString());
            db.updateMemo(memo, sid);
        }
        this.finish();
    }

}
