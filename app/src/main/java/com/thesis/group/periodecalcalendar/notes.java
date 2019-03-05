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

public class notes extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper db;
    private List<Memo> memos;
    private String sid;
    ImageView _imgLeft, _imgRight, _imgUp;
    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        SessionManager session = new SessionManager(this);
        db = new DatabaseHelper(this);

        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);
        final String suname = user.get(SessionManager.KEY_USERNAME);
        menCategory = db.getMensCat(suname);

        listView = findViewById(R.id.lstvNotesNotes);
        ImageView imgAdd = findViewById(R.id.imgAddNotes);
        _imgLeft = findViewById(R.id.imgLeftNotes);
        _imgRight = findViewById(R.id.imgRightNotes);
        _imgUp = findViewById(R.id.imgUpNotes);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                TextView textMemo = view.findViewById(R.id.tvNotesNotes);
                if (memo.isFullDisplayed()){
                    textMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    textMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent;
                if (menCategory == 1)
                {
                    intent = new Intent(notes.this, menstrualcalendar.class);
                }
                else
                {
                    intent = new Intent(notes.this, history.class);
                }
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
                Intent intent = new Intent(notes.this, reminder.class);
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
                    intent = new Intent(notes.this, welcomeuser.class);
                }
                else
                {
                    intent = new Intent(notes.this, nonregwelcomeuser.class);
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
        this.memos = db.getAllMemos(sid);
        MemoAdapter adapter = new MemoAdapter(this, memos);
        this.listView.setAdapter(adapter);
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, addnotes.class);
        startActivity(intent);
    }

    public void onDeleteClicked(final Memo memo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(notes.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteNotes(memo, sid);

                ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) listView.getAdapter();
                adapter.remove(memo);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Confirm note deletion");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, addnotes.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    private class MemoAdapter extends ArrayAdapter<Memo> {

        MemoAdapter(Context context, List<Memo> objects)
        {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitemnotes, parent, false);
            }

            ImageView btnEdit = convertView.findViewById(R.id.imgEditNotes);
            ImageView btnDelete = convertView.findViewById(R.id.imgDeleteNotes99);
            TextView txtDate = convertView.findViewById(R.id.tvDateNotes);
            TextView txtMemo = convertView.findViewById(R.id.tvNotesNotes);

            final Memo memo = memos.get(position);
            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });
            return convertView;
        }
    }
}
