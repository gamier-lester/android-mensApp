package com.thesis.group.periodecalcalendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class menstrualcalendar extends AppCompatActivity {
    DatabaseHelper db;
    SessionManager session;
    HashMap<String, String> user;
    TextView _tvPreviousMonth, _tvMonth, _tvUpdate, _tvNextMonth;
    ImageView _imgLeft, _imgRight, _imgUp;
    CompactCalendarView compactCalendar;
    SimpleDateFormat _dateFormatMonth, _dateFormatLastMens;
    String _lastMens, _id;
    Date _currentDate;
    int _counter, _baseGap, _periodLength, _ovulationNexDate, _leftCounter;
    long  _currentTime, _hourInMilliseconds, _estimatedHalfYear, _nextPeriodDayInMilliseconds, _nextPeriodLenghtInMilliseconds, _nextFertileLenghtInMilliseconds, _nextFertileDayInMilliseconds, _nextOvulationLenghtInMilliseconds, _nextOvulationDayInMilliseconds, _redLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menstrualcalendar);
        db = new DatabaseHelper(this);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        user = session.getUserDetails();
        _id = user.get(SessionManager.KEY_ID);

        _dateFormatMonth = new SimpleDateFormat("MMMM", Locale.getDefault());
        _tvPreviousMonth = findViewById(R.id.tvPreviousMenstrualCalendar);
        _tvNextMonth = findViewById(R.id.tvNextMenstrualCalendar);
        _tvUpdate = findViewById(R.id.tvEditMenstrualCalendar);
        _tvMonth = findViewById(R.id.tvMonthMenstrualCalendar);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendar.shouldScrollMonth(false);
        _imgLeft = findViewById(R.id.imgLeftMenstrualCalendar);
        _imgRight = findViewById(R.id.imgRightMenstrualCalendar);
        _imgUp = findViewById(R.id.imgUpMenstrualCalendar);

        _currentDate = new Date();
        _tvMonth.setText(_dateFormatMonth.format(_currentDate));

        _redLength = db.getPeriodLength(_id);
//        _periodLength = db.getPeriodLength(_id); ----- NOTED
//        _baseGap = getBaseGap(_periodLength); ----- NOTED
        _baseGap = getBaseGap(28);
        _hourInMilliseconds = TimeUnit.HOURS.toMillis(24);
        _estimatedHalfYear = TimeUnit.DAYS.toMillis(182);
        _ovulationNexDate = _baseGap + 2;
//        _ovulationNexDate = 28 + 2; ----- NOTED2

//        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _periodLength); ----- NOTED
        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, 28);
        _nextFertileLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _baseGap);
//        _nextFertileLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, 28); ----- NOTED2
        _nextOvulationLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _ovulationNexDate);

        _lastMens = db.getLastMens(_id);
        _dateFormatLastMens = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        _nextPeriodDayInMilliseconds = getNextEventInMillis(_nextPeriodLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);
        _nextFertileDayInMilliseconds = getNextEventInMillis(_nextFertileLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);
        _nextOvulationDayInMilliseconds = getNextEventInMillis(_nextOvulationLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);

        long forRedDays = convertDateToLong(_dateFormatLastMens, _lastMens);
        long nextFertileDayInMillisecondsGreen = _nextFertileDayInMilliseconds;
        long nextPeriodDayInMillisecondsGreen = _nextPeriodDayInMilliseconds;
        _counter = db.getCounter(_id);
        for (int i = 0; i < _counter; i++)
        {
            long lastDayF = setFertileDays(5, _nextFertileDayInMilliseconds);
            _nextFertileDayInMilliseconds = _nextFertileLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
            setOvulationDay(_nextOvulationDayInMilliseconds);
            _nextOvulationDayInMilliseconds = _nextOvulationLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
//            long lastDayR = setRedDays(5, forRedDays); ----- NOTED
            long lastDayR = setRedDays((int) (long) _redLength, forRedDays);
            _nextPeriodDayInMilliseconds = _nextPeriodLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
            forRedDays = _nextPeriodLenghtInMilliseconds + forRedDays;
            try {
                setGreenDays(lastDayF, nextPeriodDayInMillisecondsGreen, lastDayR, nextFertileDayInMillisecondsGreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nextFertileDayInMillisecondsGreen = _nextFertileLenghtInMilliseconds + nextPeriodDayInMillisecondsGreen;
            nextPeriodDayInMillisecondsGreen = _nextPeriodLenghtInMilliseconds + nextPeriodDayInMillisecondsGreen;

        }

        _currentTime = System.currentTimeMillis();
        if(_currentTime > _nextPeriodDayInMilliseconds - _estimatedHalfYear)
        {
            _counter = _counter + 6;
            int id = Integer.parseInt(_id);
            boolean checkCounter = db.updateCounter(_counter, id);
        }

        _tvPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.scrollLeft();
                _leftCounter = _leftCounter + 1;
                _tvNextMonth.setVisibility(View.VISIBLE);
            }
        });

        _tvNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.scrollRight();
                _leftCounter = _leftCounter - 1;
                if (_leftCounter == 0)
                {
                    _tvNextMonth.setVisibility(View.INVISIBLE);
                }
            }
        });

        _tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(menstrualcalendar.this, updatecalendar.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }
        });

        _imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _imgLeft.setClickable(false);
                Intent intent = new Intent(menstrualcalendar.this, mycurrentcycle.class);
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
                Intent intent = new Intent(menstrualcalendar.this, notes.class);
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
                Intent intent = new Intent(menstrualcalendar.this, welcomeuser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        compactCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
//                Context context = getApplicationContext();
//                List<Event> events = compactCalendar.getEvents(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                _tvMonth.setText(_dateFormatMonth.format(firstDayOfNewMonth));
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
        compactCalendar.removeAllEvents();
        _currentDate = new Date();
        _tvMonth.setText(_dateFormatMonth.format(_currentDate));

        _redLength = db.getPeriodLength(_id);
//        _periodLength = db.getPeriodLength(_id); ----- NOTED
//        _baseGap = getBaseGap(_periodLength); ----- NOTED
        _baseGap = getBaseGap(28);
        _hourInMilliseconds = TimeUnit.HOURS.toMillis(24);
        _estimatedHalfYear = TimeUnit.DAYS.toMillis(182);
        _ovulationNexDate = _baseGap + 2;
//        _ovulationNexDate = 28 + 2; ----- NOTED2

//        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _periodLength); ----- NOTED
        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, 28);
        _nextFertileLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _baseGap);
//        _nextFertileLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, 28); ----- NOTED2
        _nextOvulationLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _ovulationNexDate);

        _lastMens = db.getLastMens(_id);
        _dateFormatLastMens = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        _nextPeriodDayInMilliseconds = getNextEventInMillis(_nextPeriodLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);
        _nextFertileDayInMilliseconds = getNextEventInMillis(_nextFertileLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);
        _nextOvulationDayInMilliseconds = getNextEventInMillis(_nextOvulationLenghtInMilliseconds,  _dateFormatLastMens, _lastMens);

        long forRedDays = convertDateToLong(_dateFormatLastMens, _lastMens);
        long nextFertileDayInMillisecondsGreen = _nextFertileDayInMilliseconds;
        long nextPeriodDayInMillisecondsGreen = _nextPeriodDayInMilliseconds;
        _counter = db.getCounter(_id);
        for (int i = 0; i < _counter; i++)
        {
            long lastDayF = setFertileDays(5, _nextFertileDayInMilliseconds);
            _nextFertileDayInMilliseconds = _nextFertileLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
            setOvulationDay(_nextOvulationDayInMilliseconds);
            _nextOvulationDayInMilliseconds = _nextOvulationLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
//            long lastDayR = setRedDays(5, forRedDays); ----- NOTED
            long lastDayR = setRedDays((int) (long) _redLength, forRedDays);
            _nextPeriodDayInMilliseconds = _nextPeriodLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
            forRedDays = _nextPeriodLenghtInMilliseconds + forRedDays;
            try {
                setGreenDays(lastDayF, nextPeriodDayInMillisecondsGreen, lastDayR, nextFertileDayInMillisecondsGreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nextFertileDayInMillisecondsGreen = _nextFertileLenghtInMilliseconds + nextPeriodDayInMillisecondsGreen;
            nextPeriodDayInMillisecondsGreen = _nextPeriodLenghtInMilliseconds + nextPeriodDayInMillisecondsGreen;

        }

    }

    private int getBaseGap(int periodLength)
    {
        int diff = periodLength - 22;
//        int baseGap = 5; ---- NOTED
        int baseGap = 6;
        for (int i = 0; i < diff; i++)
        {
            baseGap++;
        }
        return baseGap;
    }

    private long getEventLenghtInMilliseconds(long hourInMilliseconds, long eventMultiplier)
    {

        return hourInMilliseconds * eventMultiplier;
    }

    private long getNextEventInMillis(long nextEventLenghtInMilliseconds, SimpleDateFormat dateFormatLastMens, String lastMens)
    {
        Date dateLastMens = null;
        long dateInMilliseconds;
        try {
            dateLastMens = dateFormatLastMens.parse(lastMens);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateLastMens != null;
        dateInMilliseconds = dateLastMens.getTime();
        return dateInMilliseconds + nextEventLenghtInMilliseconds;
    }

    private long convertDateToLong(SimpleDateFormat dateFormatLastMens, String lastMens)
    {
        Date dateLastMens = null;
        long dateInMilliseconds;
        try {
            dateLastMens = dateFormatLastMens.parse(lastMens);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateLastMens != null;
        dateInMilliseconds = dateLastMens.getTime();

        return dateInMilliseconds;
    }

    private long setRedDays(int dayCount, long nextPeriodMillis)
    {
        long oneDayMillis = TimeUnit.HOURS.toMillis(24);
        int i;

        for ( i = 0; i < dayCount; i++) {
            Event redEv = new Event(Color.argb(255, 213, 0, 0), nextPeriodMillis, "RED DAY");
            compactCalendar.addEvent(redEv);
            nextPeriodMillis = nextPeriodMillis + oneDayMillis;
        }
        return nextPeriodMillis;
    }

    private long setFertileDays(int dayCount, long nextFertileMillis)
    {
        long oneDayMillis = TimeUnit.HOURS.toMillis(24);

        for (int i = 0; i < dayCount; i++) {
            Event ferEv = new Event(Color.argb(255, 128, 216, 255), nextFertileMillis, "FERTILE DAY");
            compactCalendar.addEvent(ferEv);
            nextFertileMillis = nextFertileMillis + oneDayMillis;
        }
        return nextFertileMillis;
    }

    private void setOvulationDay(long nextOvulationMillis)
    {
        Event ovuEv = new Event(Color.argb(255, 106, 27, 154), nextOvulationMillis, "HIGH-CHANCE OF PREGNANCY");
            compactCalendar.addEvent(ovuEv);
    }

    private void setGreenDays(long lastDayFertile, long firstDayRed, long lastDayRed, long firstDayFertile) throws ParseException {
        String dateLastDayFertile = _dateFormatLastMens.format(lastDayFertile);
        String dateFirstDayRed = _dateFormatLastMens.format(firstDayRed);
        String dateLastDayRed = _dateFormatLastMens.format(lastDayRed);
        String dateFirstDayFertile = _dateFormatLastMens.format(firstDayFertile);

        long oneDayMillis = TimeUnit.HOURS.toMillis(24);

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        Calendar cal3 = new GregorianCalendar();
        Calendar cal4 = new GregorianCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Date date1 = sdf.parse(dateLastDayFertile);
        Date date2 = sdf.parse(dateFirstDayRed);
        Date date3 = sdf.parse(dateLastDayRed);
        Date date4 = sdf.parse(dateFirstDayFertile);

        cal1.setTime(date1);
        cal2.setTime(date2);
        cal3.setTime(date3);
        cal4.setTime(date4);

        int diff1 = daysBetween(cal1.getTime(), cal2.getTime());
        int diff2 = daysBetween(cal3.getTime(), cal4.getTime());

        for (int i = 0; i < diff1; i++)
        {
            Event ev3 = new Event(Color.argb(255, 0 ,200 ,83), lastDayFertile, "NON-FERTILE DAY");
            compactCalendar.addEvent(ev3);
            lastDayFertile = lastDayFertile + oneDayMillis;
        }

        for (int i = 0; i < diff2; i++)
        {
            Event ev7 = new Event(Color.argb(255, 0 ,200 ,83), lastDayRed, "NON-FERTILE DAY");
            compactCalendar.addEvent(ev7);
            lastDayRed = lastDayRed + oneDayMillis;
        }
    }

    private int daysBetween(Date d1, Date d2)
    {
        return (int)(d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
    }


}
