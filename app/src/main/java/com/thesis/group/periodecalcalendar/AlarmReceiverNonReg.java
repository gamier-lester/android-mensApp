package com.thesis.group.periodecalcalendar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class AlarmReceiverNonReg extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel3";
    private static final String CHANNEL_NAME = "Periodecal Calendar Non Reg";
    private static final String CHANNEL_DESC = "Periodecal Calendar Non RegNotifications";
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
                HashMap<String, String> nonRegMensMap = db.getNonRegInfo(sid);

                SimpleDateFormat _dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

                Date _currentDate = new Date();
                String scurrentDay = _dateFormat.format(_currentDate);

                double iHeight = Double.parseDouble(nonRegMensMap.get(DatabaseHelper.KEY_height));
                double iWeight = Double.parseDouble(nonRegMensMap.get(DatabaseHelper.KEY_weight));
                int iWork = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_work));
                int iSports = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_sports));
                int iPills = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_pills));
                int iBreast = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_breast));

                Intent notificationIntent = new Intent(context, history.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(nonregwelcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

                if (checkOverWeight(iHeight, iWeight))
                {
                    if (scurrentDay.equals("Tuesday") || scurrentDay.equals("Friday"))
                    {
                        String notif = "Being overweight causes delayed menstruation!!!";
                        createNotif("Menstrual Calendar", "Being overweight causes delayed menstruation!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iWork == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Wednesday") || scurrentDay.equals("Saturday"))
                    {
                        String notif = "Stress and fatigue from work causes delayed mens!!!";
                        createNotif("Menstrual Calendar", "Stress and fatigue from work causes delayed mens!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iSports == 1)
                {
                    if (scurrentDay.equals("Sunday"))
                    {
                        String notif = "May experience delay in mens due to sports activeness!!!";
                        createNotif("Menstrual Calendar", "May experience delay in mens due to sports activeness!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iPills == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Tuesday") || scurrentDay.equals("Wednesday") || scurrentDay.equals("Thursday") || scurrentDay.equals("Friday") || scurrentDay.equals("Saturday") || scurrentDay.equals("Sunday"))
                    {
                        String notif = "You need to consult to a doctor!!!";
                        createNotif("Menstrual Calendar", "You need to consult to a doctor!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iBreast == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Friday"))
                    {
                        String notif = "Delayed mens is normal when breastfeeding!!!";
                        createNotif("Menstrual Calendar", "Delayed mens is normal when breastfeeding!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
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
                HashMap<String, String> nonRegMensMap = db.getNonRegInfo(sid);

                SimpleDateFormat _dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

                Date _currentDate = new Date();
                String scurrentDay = _dateFormat.format(_currentDate);

                double iHeight = Double.parseDouble(nonRegMensMap.get(DatabaseHelper.KEY_height));
                double iWeight = Double.parseDouble(nonRegMensMap.get(DatabaseHelper.KEY_weight));
                int iWork = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_work));
                int iSports = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_sports));
                int iPills = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_pills));
                int iBreast = Integer.parseInt(nonRegMensMap.get(DatabaseHelper.KEY_breast));

                Intent notificationIntent = new Intent(context, history.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(nonregwelcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

                if (checkOverWeight(iHeight, iWeight))
                {
                    if (scurrentDay.equals("Tuesday") || scurrentDay.equals("Friday"))
                    {
                        String notif = "Being overweight causes delayed menstruation!!!";
                        createNotif("Menstrual Calendar", "Being overweight causes delayed menstruation!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iWork == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Wednesday") || scurrentDay.equals("Saturday"))
                    {
                        String notif = "Stress and fatigue from work causes delayed mens!!!";
                        createNotif("Menstrual Calendar", "Stress and fatigue from work causes delayed mens!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iSports == 1)
                {
                    if (scurrentDay.equals("Sunday"))
                    {
                        String notif = "May experience delay in mens due to sports activeness!!!";
                        createNotif("Menstrual Calendar", "May experience delay in mens due to sports activeness!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iPills == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Tuesday") || scurrentDay.equals("Wednesday") || scurrentDay.equals("Thursday") || scurrentDay.equals("Friday") || scurrentDay.equals("Saturday") || scurrentDay.equals("Sunday"))
                    {
                        String notif = "You need to consult to a doctor!!!";
                        createNotif("Menstrual Calendar", "You need to consult to a doctor!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iBreast == 1)
                {
                    if (scurrentDay.equals("Monday") || scurrentDay.equals("Friday"))
                    {
                        String notif = "Delayed mens is normal when breastfeeding!!!";
                        createNotif("Menstrual Calendar", "Delayed mens is normal when breastfeeding!!!", context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }
            }
        }



    }

    public void createNotif(String title, String content, Context context, PendingIntent pendingIntent)
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
//                .setContentTitle(title)
//                .setContentText(content)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContent(contentView)
                .setAutoCancel(true);
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(m, mBuilder.build());
    }

    private boolean checkOverWeight(double weight, double height)
    {
        boolean check;

        double bmi = (weight*703)/(height*height);
        check = bmi >= 25;

        return check;
    }

    private void reAlarm(Context context)
    {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.HOUR, 12);
        calendar2.set(Calendar.AM_PM,Calendar.AM);

        Intent intent3 = new Intent(context, AlarmReceiverNonReg.class);

        PendingIntent broadcast3 = PendingIntent.getBroadcast(context, 300, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(broadcast3);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar2.getTimeInMillis(), broadcast3), broadcast3);
        }
    }

}
