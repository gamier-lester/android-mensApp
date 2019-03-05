package com.thesis.group.periodecalcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class mycurrentcycle extends AppCompatActivity {
    CalendarDates cd;
    Date _currentDate;
    SimpleDateFormat _dateFormat1, _dateFormat2;
    HashMap<String, String> _calendarDates;
    TextView _tvDay, _tvStatus, _tvPregChance1, _tvPregChance2;
    ImageView _imgLeft, _imgRight, _imgUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycurrentcycle);
        cd = new CalendarDates(this);
        _calendarDates = cd.getDatesMap();
        String datesMapRed = _calendarDates.get(CalendarDates.KEY_redDays);
        String datesMapFertile = _calendarDates.get(CalendarDates.KEY_fertileDays);
        String datesMapOvulation = _calendarDates.get(CalendarDates.KEY_ovulationDays);

        assert datesMapRed != null;
        List<String> listRed = getDateList(datesMapRed);
        assert datesMapFertile != null;
        List<String> listFertile = getDateList(datesMapFertile);
        assert datesMapOvulation != null;
        List<String> listOvulation = getDateList(datesMapOvulation);

        _tvDay = findViewById(R.id.tvDayMyCurrentCycle);
        _tvStatus = findViewById(R.id.tvStatusMyCurrentCycle);
        _tvPregChance1 = findViewById(R.id.tvChancePregMyCurrentCylce1);
        _tvPregChance2 = findViewById(R.id.tvChancePregMyCurrentCylce2);
        _imgLeft = findViewById(R.id.imgLeftMyCurrentCycle);
        _imgRight = findViewById(R.id.imgRightCurrentCycle);
        _imgUp = findViewById(R.id.imgUpCurrentCycle);

        _currentDate = new Date();
        _dateFormat1 = new SimpleDateFormat("EEEE", Locale.getDefault());
        _dateFormat2 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        _tvDay.setText(_dateFormat1.format(_currentDate).toUpperCase());
        String currentDate = _dateFormat2.format(_currentDate);
        setTv(currentDate, listRed, listFertile, listOvulation, _tvStatus, _tvPregChance1, _tvPregChance2);

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(mycurrentcycle.this, history.class);
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
                Intent intent = new Intent(mycurrentcycle.this, menstrualcalendar.class);
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
                Intent intent = new Intent(mycurrentcycle.this, welcomeuser.class);
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

    private List<String> getDateList(String datesMap)
    {
        String[] dates = datesMap.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, dates);
        return list;
    }

    private void setTv(String date, List<String> list1, List<String> list2, List<String> list3, TextView tvStatus, TextView tvPregChance1, TextView tvPregChance2)
    {
        if (list1.contains(date))
        {
            tvStatus.setText("PERIOD");
            tvPregChance1.setText("NO CHANCE OF");
            tvPregChance2.setText("PREGNANCY");
        }
        else if (list2.contains(date))
        {
            if (list3.contains(date))
            {
                tvStatus.setText("OVULATION");
                tvPregChance1.setText("HIGH CHANCE OF");
                tvPregChance2.setText("PREGNANCY");
            }
            else
            {
                tvStatus.setText("FERTILE");
                tvPregChance1.setText("HAS CHANCE OF");
                tvPregChance2.setText("PREGNANCY");
            }
        }
        else
        {
            tvStatus.setText("NORMAL");
            tvPregChance1.setText("NO CHANCE OF");
            tvPregChance2.setText("PREGNANCY");
        }
    }


}
