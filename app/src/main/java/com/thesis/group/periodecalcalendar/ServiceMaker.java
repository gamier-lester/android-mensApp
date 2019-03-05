package com.thesis.group.periodecalcalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.HashMap;

class ServiceMaker {
    private Context mContext;
    DatabaseHelper db;
    SessionManager session;
    ServiceMaker(Context context) {
        this.mContext = context;
    }

    void createService()
    {
        session = new SessionManager(mContext);
        db = new DatabaseHelper(mContext);

        HashMap<String, String> user = session.getUserDetails();
        final String suname = user.get(SessionManager.KEY_USERNAME);
        int cat = db.getMensCat(suname);
        if (cat > 0)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.SECOND, 5);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(System.currentTimeMillis());
            calendar2.set(Calendar.MILLISECOND, 0);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.HOUR, 0);
            calendar2.set(Calendar.AM_PM,Calendar.AM);

            Intent intent1 = new Intent(mContext, AlarmReceiver.class);
            Intent intent2 = new Intent(mContext, AlarmReceiverReminders.class);
            Intent intent3 = new Intent(mContext, AlarmReceiverNonReg.class);

            PendingIntent broadcast1 = PendingIntent.getBroadcast(mContext, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent broadcast2 = PendingIntent.getBroadcast(mContext, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent broadcast3 = PendingIntent.getBroadcast(mContext, 300, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(broadcast1);
            alarmManager.cancel(broadcast2);
            alarmManager.cancel(broadcast3);

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
            {
                if (cat == 1)
                {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar2.getTimeInMillis(), broadcast1), broadcast1);
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar1.getTimeInMillis(), broadcast2), broadcast2);
                }
                else
                {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar1.getTimeInMillis(), broadcast2), broadcast2);
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar2.getTimeInMillis(), broadcast3), broadcast3);
                }
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (cat == 1)
                {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000*60*60*24, broadcast1);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), 1000*60, broadcast2);
                }
                else
                {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), 1000*60, broadcast2);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000*60*60*24, broadcast3);
                }
            }
            else
            {
                if (cat == 1)
                {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000*60*60*24, broadcast1);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), 1000*60, broadcast2);
                }
                else
                {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), 1000*60, broadcast2);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000*60*60*24, broadcast3);
                }
            }
        }
    }
}
