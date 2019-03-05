package com.thesis.group.periodecalcalendar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AlarmReceiverReminders extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel2";
    private static final String CHANNEL_NAME = "Periodecal Calendar Reminders";
    private static final String CHANNEL_DESC = "Periodecal Calendar Reminders Notifications";
    boolean checkM;

    @Override
    public void onReceive(final Context context, Intent intent) {

        checkM = Build.VERSION.SDK_INT == Build.VERSION_CODES.M;

        if (checkM)
        {
            SessionManager session = new SessionManager(context);
            boolean checkSession = session.checkLogin();
            if (!checkSession)
            {
                DatabaseHelper db = new DatabaseHelper(context);

                HashMap<String, String> user = session.getUserDetails();
                final String sid = user.get(SessionManager.KEY_ID);

                Intent notificationIntent = new Intent(context, reminder.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(welcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(200, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar rightNow = Calendar.getInstance();
                String currentDay = getCurrentDay(rightNow);
                String currentHour = String.format("%02d", rightNow.get(Calendar.HOUR_OF_DAY));
                String currentMinute = String.format("%02d", rightNow.get(Calendar.MINUTE));
                List<String> reminders, sounds;
                reminders = db.getReminders(sid, currentDay, currentHour, currentMinute);
                sounds = db.getSounds(sid, currentDay, currentHour, currentMinute);

                for (int i = 0; i < reminders.size(); i++)
                {
                    HistoryModel temp = new HistoryModel();
                    temp.setText(reminders.get(i));
                    db.setHistory(temp, sid);
                    createNotif("Menstrual Calendar", reminders.get(i), context, pendingIntent, sounds.get(i));
                }
            }

            Handler handler=new Handler();
            Runnable r=new Runnable()
            {
                public void run()
                {
                    reAlarm(context);
                }
            };
            handler.postDelayed(r, 60000);
        }
        else
        {
            SessionManager session = new SessionManager(context);
            boolean checkSession = session.checkLogin();
            if (!checkSession)
            {
                DatabaseHelper db = new DatabaseHelper(context);

                HashMap<String, String> user = session.getUserDetails();
                final String sid = user.get(SessionManager.KEY_ID);

                Intent notificationIntent = new Intent(context, reminder.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(welcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(200, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar rightNow = Calendar.getInstance();
                String currentDay = getCurrentDay(rightNow);
                String currentHour = String.format("%02d", rightNow.get(Calendar.HOUR_OF_DAY));
                String currentMinute = String.format("%02d", rightNow.get(Calendar.MINUTE));
                List<String> reminders, sounds;
                reminders = db.getReminders(sid, currentDay, currentHour, currentMinute);
                sounds = db.getSounds(sid, currentDay, currentHour, currentMinute);

                for (int i = 0; i < reminders.size(); i++)
                {
                    HistoryModel temp = new HistoryModel();
                    temp.setText(reminders.get(i));
                    db.setHistory(temp, sid);
                    createNotif("Menstrual Calendar", reminders.get(i), context, pendingIntent, sounds.get(i));
                }
            }
        }
    }

    public void createNotif(String title, String content, Context context, PendingIntent pendingIntent, String sound)
    {
        Random rand = new Random();
        int m = rand.nextInt(999999999) + 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.imgPushNotif, R.mipmap.appicon);
        contentView.setTextViewText(R.id.tvHeaderPushNotif, title);
        contentView.setTextViewText(R.id.tvBodyPushNotif, content);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setChannelId(CHANNEL_ID)
//                .setContentTitle(title)
//                .setContentText(content)
//                .setColorized(true)
//                .setColor(Color.argb(255, 255, 0, 153))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContent(contentView)
                .setAutoCancel(true);
        if (sound.equals("1"))
        {
            mBuilder.setSound(alarmSound);
        }
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(m, mBuilder.build());
    }

    public String getCurrentDay (Calendar cal)
    {
        String day;
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        if (currentDay == Calendar.MONDAY)
        {
            day = "Monday";
        }
        else if (currentDay == Calendar.TUESDAY)
        {
            day = "Tuesday";
        }
        else if (currentDay == Calendar.WEDNESDAY)
        {
            day = "Wednesday";
        }
        else if (currentDay == Calendar.THURSDAY)
        {
            day = "Thursday";
        }
        else if (currentDay == Calendar.FRIDAY)
        {
            day = "Friday";
        }
        else if (currentDay == Calendar.SATURDAY)
        {
            day = "Saturday";
        }
        else
        {
            day = "Sunday";
        }
        return day;
    }

    private void reAlarm(Context context)
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.SECOND, 5);

        Intent intent2 = new Intent(context, AlarmReceiverReminders.class);

        PendingIntent broadcast2 = PendingIntent.getBroadcast(context, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(broadcast2);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar1.getTimeInMillis(), broadcast2), broadcast2);
        }
    }

}
