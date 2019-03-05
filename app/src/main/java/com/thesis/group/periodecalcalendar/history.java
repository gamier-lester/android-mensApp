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
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class history extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper db;
    private List<HistoryModel> hemos;
    private String sid;
    ImageView _imgLeft, _imgRight, _imgUp;
    int menCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SessionManager session = new SessionManager(this);
        db = new DatabaseHelper(this);

        HashMap<String, String> user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);
        final String suname = user.get(SessionManager.KEY_USERNAME);
        menCategory = db.getMensCat(suname);

        listView = findViewById(R.id.lstvHistoryHistory);
        TextView btnDeleteAll = findViewById(R.id.tvDeleteAllHistory);
        _imgLeft = findViewById(R.id.btnLeftHistory);
        _imgRight = findViewById(R.id.btnRightHistory);
        _imgUp = findViewById(R.id.btnUpHistory);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryModel hemo = hemos.get(position);
                TextView textHistory = view.findViewById(R.id.tvHistoryHistory);
                if (hemo.isFullDisplayed()){
                    textHistory.setText(hemo.getShortText());
                    hemo.setFullDisplayed(false);
                } else {
                    textHistory.setText(hemo.getText());
                    hemo.setFullDisplayed(true);
                }
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllHistory();
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent;
                if (menCategory == 1)
                {
                    intent = new Intent(history.this, settings.class);
                }
                else
                {
                    intent = new Intent(history.this, editprofilenonreg.class);
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
                Intent intent;
                if (menCategory == 1)
                {
                    intent = new Intent(history.this, mycurrentcycle.class);
                }
                else
                {
                    intent = new Intent(history.this, notes.class);
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
                    intent = new Intent(history.this, welcomeuser.class);
                }
                else
                {
                    intent = new Intent(history.this, nonregwelcomeuser.class);
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
        this.hemos = db.getAllHistory(sid);
        history.HistoAdapter adapter = new history.HistoAdapter(this, hemos);
        this.listView.setAdapter(adapter);
    }

    public void onDeleteClicked(final HistoryModel hemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(history.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteHistory(hemo, sid);
                ArrayAdapter<HistoryModel> adapter = (ArrayAdapter<HistoryModel>) listView.getAdapter();
                adapter.remove(hemo);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setMessage("Are you sure you want to delete this notification log from you history?");
        builder.setTitle("Confirm history deletion");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void deleteAllHistory()
    {
        final ArrayAdapter<HistoryModel> adapter = (ArrayAdapter<HistoryModel>) listView.getAdapter();

        if (adapter.getCount() == 0 )
        {
            Toast.makeText(getApplicationContext(), "History list is empty!", Toast.LENGTH_LONG).show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(history.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteAllHistory(sid);
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setMessage("Are you sure you want to delete all notification logs from your history?");
            builder.setTitle("Confirm all history deletion");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private class HistoAdapter extends ArrayAdapter<HistoryModel>
    {

        HistoAdapter(Context context, List<HistoryModel> objects)
        {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.listitemhistory, parent, false);
            }
            ImageView btnDelete = convertView.findViewById(R.id.imgDeleteHistory);
            TextView txtHemo = convertView.findViewById(R.id.tvHistoryHistory);
            TextView txtDate = convertView.findViewById(R.id.tvDateHistory);

            final HistoryModel hemo = hemos.get(position);
            hemo.setFullDisplayed(false);
            txtHemo.setText(hemo.getShortText());
            txtDate.setText(hemo.getDate());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(hemo);
                }
            });

            return convertView;
        }
    }

}
