package com.thesis.group.periodecalcalendar;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class CalendarDates {
    private Context mcontext ;
    SessionManager session;
    private SharedPreferences.Editor editor;
    HashMap<String, String> user;
    private SimpleDateFormat _dateFormat;
    private SharedPreferences sharedpreferences;
    private StringBuilder csvRedList = new StringBuilder();
    private StringBuilder csvFertileList = new StringBuilder();
    private StringBuilder csvOvulationList = new StringBuilder();
    private StringBuilder nextRedSb = new StringBuilder();
    private StringBuilder nextFertileSb = new StringBuilder();
    private StringBuilder nextOvulationSb = new StringBuilder();
    static final String KEY_redDays = "redDaysList";
    static final String KEY_fertileDays = "fertileDaysList";
    static final String KEY_ovulationDays = "ovulationDaysList";
    static final String KEY_nextRedDays = "nextRedDaysList";
    static final String KEY_nextFertileDays = "nextFertileDaysList";
    static final String KEY_nextOvulationDays = "nextOvulationDaysList";
    private List<String> nextRedDateList = new ArrayList<>();
    private List<String> nextFertileDateList = new ArrayList<>();
    private List<String> nextOvulationDateList = new ArrayList<>();


    CalendarDates(Context context) {
        this.mcontext = context;
        String myPREFERENCES = "CalendarDates";
        sharedpreferences = mcontext.getSharedPreferences(myPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.apply();
    }

    void test()
    {
        SimpleDateFormat _dateFormatLastMens;
        DatabaseHelper db;
        String _id, _lastMens;
        int _periodLength, _baseGap, _ovulationNexDate, _counter, _redLength;
        long _currentTime, _hourInMilliseconds, _estimatedHalfYear, _nextPeriodLenghtInMilliseconds, _nextFertileLenghtInMilliseconds, _nextOvulationLenghtInMilliseconds, _nextPeriodDayInMilliseconds, _nextFertileDayInMilliseconds, _nextOvulationDayInMilliseconds;

        db = new DatabaseHelper(mcontext);
        session = new SessionManager(mcontext);
        user = session.getUserDetails();
        _id = user.get(SessionManager.KEY_ID);
        _dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        _redLength = db.getPeriodLength(_id);
//        _periodLength = db.getPeriodLength(_id); ----- NOTED
//        _baseGap = getBaseGap(_periodLength); ----- NOTED
        _baseGap = getBaseGap(28);
        _hourInMilliseconds = TimeUnit.HOURS.toMillis(24);
        _estimatedHalfYear = TimeUnit.DAYS.toMillis(182);
        _ovulationNexDate = _baseGap + 2;

//        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _periodLength); ----- NOTED
        _nextPeriodLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, 28);
        _nextFertileLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _baseGap);
        _nextOvulationLenghtInMilliseconds = getEventLenghtInMilliseconds(_hourInMilliseconds, _ovulationNexDate);

        _lastMens = db.getLastMens(_id);
        _dateFormatLastMens = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        _nextPeriodDayInMilliseconds = getNextEventInMillis(_nextPeriodLenghtInMilliseconds, _dateFormatLastMens, _lastMens);
        _nextFertileDayInMilliseconds = getNextEventInMillis(_nextFertileLenghtInMilliseconds, _dateFormatLastMens, _lastMens);
        _nextOvulationDayInMilliseconds = getNextEventInMillis(_nextOvulationLenghtInMilliseconds, _dateFormatLastMens, _lastMens);

        long forRedDays = convertDateToLong(_dateFormatLastMens, _lastMens);
//        setRedDays(5,  forRedDays); ----- NOTED
        setRedDays((int) (long) _redLength,  forRedDays);
        _counter = db.getCounter(_id);
        for (int i = 0; i < _counter; i++)
        {
            String nextDateFertile = _dateFormat.format(_nextFertileDayInMilliseconds);
            String nextDateOvulation = _dateFormat.format(_nextOvulationDayInMilliseconds);
            String nextDateRed = _dateFormat.format(_nextPeriodDayInMilliseconds);
            nextFertileDateList.add(nextDateFertile);
            nextOvulationDateList.add(nextDateOvulation);
            nextRedDateList.add(nextDateRed);
            setFertileDays(5, _nextFertileDayInMilliseconds);
            _nextFertileDayInMilliseconds = _nextFertileLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
            setOvulationDay(_nextOvulationDayInMilliseconds);
            _nextOvulationDayInMilliseconds = _nextOvulationLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
//            setRedDays(5,  _nextPeriodDayInMilliseconds); ----- NOTED
            setRedDays((int) (long) _redLength,  _nextPeriodDayInMilliseconds);
            _nextPeriodDayInMilliseconds = _nextPeriodLenghtInMilliseconds + _nextPeriodDayInMilliseconds;
        }

        for(String s : nextRedDateList){
            nextRedSb.append(s);
            nextRedSb.append(",");
        }
        for(String s : nextFertileDateList){
            nextFertileSb.append(s);
            nextFertileSb.append(",");
        }
        for(String s : nextOvulationDateList){
            nextOvulationSb.append(s);
            nextOvulationSb.append(",");
        }

        editor.putString(KEY_nextRedDays, nextRedSb.toString());
        editor.putString(KEY_nextFertileDays, nextFertileSb.toString());
        editor.putString(KEY_nextOvulationDays, nextOvulationSb.toString());
        editor.putString(KEY_redDays, csvRedList.toString());
        editor.putString(KEY_fertileDays, csvFertileList.toString());
        editor.putString(KEY_ovulationDays, csvOvulationList.toString());
        editor.commit();

        _currentTime = System.currentTimeMillis();
        if(_currentTime > _nextPeriodDayInMilliseconds - _estimatedHalfYear)
        {
            _counter = _counter + 6;
            assert _id != null;
            int id = Integer.parseInt(_id);
            boolean checkCounter = db.setCounter(_counter, id);
        }
    }

    private int getBaseGap(int periodLength)
    {
        int diff = periodLength - 22;
//        int baseGap = 5; ----- NOTED
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

    private void setRedDays(int dayCount, long nextPeriodMillis)
    {
        String date;
        long oneDayMillis;
        List<String> redDateList = new ArrayList<>();
        oneDayMillis = TimeUnit.HOURS.toMillis(24);

        for (int i = 0; i < dayCount; i++) {
            date = _dateFormat.format(nextPeriodMillis);
            redDateList.add(date);
            nextPeriodMillis = nextPeriodMillis + oneDayMillis;
        }
        for(String s : redDateList){
            csvRedList.append(s);
            csvRedList.append(",");
        }
    }

    private void setFertileDays(int dayCount, long nextFertileMillis)
    {
        String date;
        long oneDayMillis;
        List<String> fertileDateList = new ArrayList<>();
        oneDayMillis = TimeUnit.HOURS.toMillis(24);

        for (int i = 0; i < dayCount; i++) {
            date = _dateFormat.format(nextFertileMillis);
            fertileDateList.add(date);
            nextFertileMillis = nextFertileMillis + oneDayMillis;
        }
        for(String s : fertileDateList){
            csvFertileList.append(s);
            csvFertileList.append(",");
        }
    }

    private void setOvulationDay(long nextOvulationMillis)
    {
        String dateL;
        dateL = _dateFormat.format(nextOvulationMillis);
        csvOvulationList.append(dateL);
        csvOvulationList.append(",");
//        editor.putString(KEY_ovulationDays, dateL);
//        editor.commit();
    }

    HashMap<String, String> getDatesMap()
    {
        HashMap<String, String> calendarDatesMap = new HashMap<>();
        calendarDatesMap.put(KEY_redDays, sharedpreferences.getString(KEY_redDays, null));
        calendarDatesMap.put(KEY_fertileDays, sharedpreferences.getString(KEY_fertileDays, null));
        calendarDatesMap.put(KEY_ovulationDays, sharedpreferences.getString(KEY_ovulationDays, null));
        return calendarDatesMap;
    }

    HashMap<String, String> getNextDatesMap()
    {
        HashMap<String, String> calendarDatesMap = new HashMap<>();
        calendarDatesMap.put(KEY_nextRedDays, sharedpreferences.getString(KEY_nextRedDays, null));
        calendarDatesMap.put(KEY_nextFertileDays, sharedpreferences.getString(KEY_nextFertileDays, null));
        calendarDatesMap.put(KEY_nextOvulationDays, sharedpreferences.getString(KEY_nextOvulationDays, null));
        return calendarDatesMap;
    }

}
