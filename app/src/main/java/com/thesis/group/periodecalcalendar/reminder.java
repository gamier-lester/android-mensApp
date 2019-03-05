package com.thesis.group.periodecalcalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class reminder extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper db;
    private List<ReminderModel> remos;
    private String sid;
    ImageView _imgLeft, _imgRight, _imgUp;
    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        SessionManager session = new SessionManager(this);
        db = new DatabaseHelper(this);

        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);
        final String suname = user.get(SessionManager.KEY_USERNAME);
        menCategory = db.getMensCat(suname);

        listView = findViewById(R.id.lstvRemindersReminders);
        ImageView imgAdd = findViewById(R.id.imgAddReminders);
        _imgLeft = findViewById(R.id.imgLeftReminder);
        _imgRight = findViewById(R.id.imgRightReminder);
        _imgUp = findViewById(R.id.imgUpReminder);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReminderModel remo = remos.get(position);
                TextView textMemo = view.findViewById(R.id.tvReminderReminders);
                if (remo.isFullDisplayed()){
                    textMemo.setText(remo.getShortText());
                    remo.setFullDisplayed(false);
                } else {
                    textMemo.setText(remo.getText());
                    remo.setFullDisplayed(true);
                }
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(reminder.this, notes.class);
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
                Intent intent;
                if (menCategory == 1)
                {
                    intent = new Intent(reminder.this, editprofile.class);
                }
                else
                {
                    intent = new Intent(reminder.this, editprofilenonreg.class);
                }
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
                    intent = new Intent(reminder.this, welcomeuser.class);
                }
                else
                {
                    intent = new Intent(reminder.this, nonregwelcomeuser.class);
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
    protected void onResume()
    {
        super.onResume();
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);
        this.remos = db.getAllRemos(sid);
        RemoAdapter adapter = new RemoAdapter(this, remos);
        this.listView.setAdapter(adapter);
    }

    public void onAddClicked()
    {
        Intent intent = new Intent(this, addreminders.class);
        startActivity(intent);
    }

    public void onDeleteClicked(final ReminderModel remo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(reminder.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteReminders(remo, sid);

                ArrayAdapter<ReminderModel> adapter = (ArrayAdapter<ReminderModel>) listView.getAdapter();
                adapter.remove(remo);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setMessage("Are you sure you want to delete this reminder?");
        builder.setTitle("Confirm reminder deletion");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void setSound(final ImageView imgView, int sound)
    {
        if (sound == 1)
        {
            imgView.setImageResource(R.drawable.speaker);
        }
        else
        {
            imgView.setImageResource(R.drawable.mute);
        }
    }

    public void soundPressed(final ImageView imgView, final ReminderModel remo, int sound)
    {
        if (sound == 1)
        {
            imgView.setTag(1);
        }
        else
        {
            imgView.setTag(2);
        }

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgView.getTag().equals(1))
                {
                    imgView.setImageResource(R.drawable.mute);
                    imgView.setTag(2);
                    remo.setSound(0);
                }
                else
                {
                    imgView.setImageResource(R.drawable.speaker);
                    imgView.setTag(1);
                    remo.setSound(1);
                }
                db.updateSound(remo, sid);
            }
        });
    }


    private class RemoAdapter extends ArrayAdapter<ReminderModel> {


        RemoAdapter(Context context, List<ReminderModel> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitemreminders, parent, false);
            }

            ImageView imgSound = convertView.findViewById(R.id.imgSoundReminders);
            ImageView imgDelete = convertView.findViewById(R.id.imgDeleteReminders);
            TextView txtDate = convertView.findViewById(R.id.tvDateReminders);
            TextView txtReminder = convertView.findViewById(R.id.tvReminderReminders);

            final ReminderModel remo = remos.get(position);
            remo.setFullDisplayed(false);
            txtDate.setText(remo.getDate());
            txtReminder.setText(remo.getShortText());
            setSound(imgSound, remo.getSound());
            soundPressed(imgSound, remo, remo.getSound());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(remo);
                }
            });
            return convertView;
        }
    }
}
