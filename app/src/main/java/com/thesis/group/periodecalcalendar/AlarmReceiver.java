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
import android.telephony.SmsManager;
import android.widget.RemoteViews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "Periodecal Calendar";
    private static final String CHANNEL_DESC = "Periodecal Calendar Notifications";
    DatabaseHelper db;
    String sid, suname;
    boolean checkM;

    @Override
    public void onReceive(final Context context, Intent intent) {

        checkM = Build.VERSION.SDK_INT == Build.VERSION_CODES.M;

        if (checkM)
        {
            SessionManager session = new SessionManager(context);
            boolean checkSession = session.checkLogin();

            if (!checkSession) {
                db = new DatabaseHelper(context);
                CalendarDates cd = new CalendarDates(context);

                HashMap<String, String> user = session.getUserDetails();
                sid = user.get(SessionManager.KEY_ID);
                suname = user.get(SessionManager.KEY_USERNAME);
                HashMap<String, String> settingsMap = db.getSettings(sid);
                HashMap<String, String> regMensMap = db.getRegMensInfo(sid);

                SimpleDateFormat _dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                Date _currentDate = new Date();
                String scurrentDate = _dateFormat.format(_currentDate);

                HashMap<String, String> nextDateMap = cd.getNextDatesMap();
                String nextDatesMapRed = nextDateMap.get(CalendarDates.KEY_nextRedDays);

                assert nextDatesMapRed != null;
                List<String> listRed = getDateList(nextDatesMapRed);

                HashMap<String, String> calendarDateMap = cd.getDatesMap();
                String datesMapRed = calendarDateMap.get(CalendarDates.KEY_redDays);
                String datesMapFertile = calendarDateMap.get(CalendarDates.KEY_fertileDays);
                String datesMapOvulation = calendarDateMap.get(CalendarDates.KEY_ovulationDays);

                assert datesMapRed != null;
                List<String> calendarListRed = getDateList(datesMapRed);
                assert datesMapFertile != null;
                List<String> calendarListFertile = getDateList(datesMapFertile);
                assert datesMapOvulation != null;
                List<String> calendarListOvulation = getDateList(datesMapOvulation);

                int iMens = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_mens));
                int iFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_fertile));
                int iNonFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_nonFertile));
                int iTips = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_tips));
                int iMessages = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_messages));

                double iHeight = Double.parseDouble(regMensMap.get(DatabaseHelper.KEY_height));
                double iWeight = Double.parseDouble(regMensMap.get(DatabaseHelper.KEY_weight));
                int iWork = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_work));
                int iSports = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_sports));
                int iPills = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_pills));
                int iCramps = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_cramps));

                Intent notificationIntent = new Intent(context, history.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(welcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

                if (iMens == 1) {
                    Date date = null;
                    long dateToCompare;
                    long dateInMilliseconds;
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare = dateInMilliseconds - threeDays;
                        String dateString = _dateFormat.format(new Date(dateToCompare));
                        if (scurrentDate.equals(dateString)) {
                            String notif = "Three days to go before your next period!!!";
                            createNotif("Menstrual Calendar", notif, context, pendingIntent);
                            HistoryModel temp = new HistoryModel();
                            temp.setText(notif);
                            db.setHistory(temp, sid);
                        }
                    }
                }

                if (iFertile == 1) {
                    if (calendarListFertile.contains(scurrentDate)) {
                        String notif = "You are fertile today!!!";
                        createNotif("Menstrual Calendar", notif, context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iNonFertile == 1) {
                    if (!calendarListRed.contains(scurrentDate) && !calendarListFertile.contains(scurrentDate) && !calendarListOvulation.contains(scurrentDate)) {
                        String notif = "You are not fertile today!!!";
                        createNotif("Menstrual Calendar", notif, context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iTips == 1) {
                    Calendar rightNow = Calendar.getInstance();
                    Date date = null;
                    long dateToCompare3, dateToCompare5, dateToCompare7, dateToCompare10;
                    long dateInMilliseconds;
                    long sevenDays = TimeUnit.DAYS.toMillis(7);
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    long fiveDays = TimeUnit.DAYS.toMillis(5);
                    long tenDays = TimeUnit.DAYS.toMillis(10);

                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare3 = dateInMilliseconds - threeDays;
                        dateToCompare5 = dateInMilliseconds - fiveDays;
                        dateToCompare7 = dateInMilliseconds - sevenDays;
                        dateToCompare10 = dateInMilliseconds - tenDays;
                        String dateString3 = _dateFormat.format(new Date(dateToCompare3));
                        String dateString5 = _dateFormat.format(new Date(dateToCompare5));
                        String dateString7 = _dateFormat.format(new Date(dateToCompare7));
                        String dateString10 = _dateFormat.format(new Date(dateToCompare10));
                        if (scurrentDate.equals(dateString3)) {
                            if (iWork == 1) {
                                String notif = "Stress and fatigue from work causes delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString5)) {
                            if (iSports == 1) {
                                String notif = "You may experience delay in mens due to sports activeness!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString7)) {
                            boolean check = checkOverWeight(iHeight, iWeight);
                            if (check) {
                                String notif = "Lose weight to avoid delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString10)) {
                            if (iPills == 1) {
                                String notif = "Taking pills causes delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                    }
                    if (iCramps == 1)
                    {
                        setCurrentDayNotif(rightNow, context, pendingIntent);
                    }
                }

                if (iMessages == 1)
                {
                    String phoneNumber = db.getContact(suname);
                    String textMessage = suname + " menstrual period is  about to start in 3 days....";
                    Date date = null;
                    long dateToCompare;
                    long dateInMilliseconds;
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare = dateInMilliseconds - threeDays;
                        String dateString = _dateFormat.format(new Date(dateToCompare));
                        if (scurrentDate.equals(dateString))
                        {
                            sendMessage(context, phoneNumber, textMessage, pendingIntent);
                        }
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

            if (!checkSession) {
                db = new DatabaseHelper(context);
                CalendarDates cd = new CalendarDates(context);

                HashMap<String, String> user = session.getUserDetails();
                sid = user.get(SessionManager.KEY_ID);
                suname = user.get(SessionManager.KEY_USERNAME);
                HashMap<String, String> settingsMap = db.getSettings(sid);
                HashMap<String, String> regMensMap = db.getRegMensInfo(sid);

                SimpleDateFormat _dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                Date _currentDate = new Date();
                String scurrentDate = _dateFormat.format(_currentDate);

                HashMap<String, String> nextDateMap = cd.getNextDatesMap();
                String nextDatesMapRed = nextDateMap.get(CalendarDates.KEY_nextRedDays);

                assert nextDatesMapRed != null;
                List<String> listRed = getDateList(nextDatesMapRed);

                HashMap<String, String> calendarDateMap = cd.getDatesMap();
                String datesMapRed = calendarDateMap.get(CalendarDates.KEY_redDays);
                String datesMapFertile = calendarDateMap.get(CalendarDates.KEY_fertileDays);
                String datesMapOvulation = calendarDateMap.get(CalendarDates.KEY_ovulationDays);

                assert datesMapRed != null;
                List<String> calendarListRed = getDateList(datesMapRed);
                assert datesMapFertile != null;
                List<String> calendarListFertile = getDateList(datesMapFertile);
                assert datesMapOvulation != null;
                List<String> calendarListOvulation = getDateList(datesMapOvulation);

                int iMens = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_mens));
                int iFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_fertile));
                int iNonFertile = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_nonFertile));
                int iTips = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_tips));
                int iMessages = Integer.parseInt(settingsMap.get(DatabaseHelper.KEY_messages));

                double iHeight = Double.parseDouble(regMensMap.get(DatabaseHelper.KEY_height));
                double iWeight = Double.parseDouble(regMensMap.get(DatabaseHelper.KEY_weight));
                int iWork = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_work));
                int iSports = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_sports));
                int iPills = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_pills));
                int iCramps = Integer.parseInt(regMensMap.get(DatabaseHelper.KEY_cramps));

                Intent notificationIntent = new Intent(context, history.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(welcomeuser.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

                if (iMens == 1) {
                    Date date = null;
                    long dateToCompare;
                    long dateInMilliseconds;
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare = dateInMilliseconds - threeDays;
                        String dateString = _dateFormat.format(new Date(dateToCompare));
                        if (scurrentDate.equals(dateString)) {
                            String notif = "Three days to go before your next period!!!";
                            createNotif("Menstrual Calendar", notif, context, pendingIntent);
                            HistoryModel temp = new HistoryModel();
                            temp.setText(notif);
                            db.setHistory(temp, sid);
                        }
                    }
                }

                if (iFertile == 1) {
                    if (calendarListFertile.contains(scurrentDate)) {
                        String notif = "You are fertile today!!!";
                        createNotif("Menstrual Calendar", notif, context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iNonFertile == 1) {
                    if (!calendarListRed.contains(scurrentDate) && !calendarListFertile.contains(scurrentDate) && !calendarListOvulation.contains(scurrentDate)) {
                        String notif = "You are not fertile today!!!";
                        createNotif("Menstrual Calendar", notif, context, pendingIntent);
                        HistoryModel temp = new HistoryModel();
                        temp.setText(notif);
                        db.setHistory(temp, sid);
                    }
                }

                if (iTips == 1) {
                    Calendar rightNow = Calendar.getInstance();
                    Date date = null;
                    long dateToCompare3, dateToCompare5, dateToCompare7, dateToCompare10;
                    long dateInMilliseconds;
                    long sevenDays = TimeUnit.DAYS.toMillis(7);
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    long fiveDays = TimeUnit.DAYS.toMillis(5);
                    long tenDays = TimeUnit.DAYS.toMillis(10);

                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare3 = dateInMilliseconds - threeDays;
                        dateToCompare5 = dateInMilliseconds - fiveDays;
                        dateToCompare7 = dateInMilliseconds - sevenDays;
                        dateToCompare10 = dateInMilliseconds - tenDays;
                        String dateString3 = _dateFormat.format(new Date(dateToCompare3));
                        String dateString5 = _dateFormat.format(new Date(dateToCompare5));
                        String dateString7 = _dateFormat.format(new Date(dateToCompare7));
                        String dateString10 = _dateFormat.format(new Date(dateToCompare10));
                        if (scurrentDate.equals(dateString3)) {
                            if (iWork == 1) {
                                String notif = "Stress and fatigue from work causes delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString5)) {
                            if (iSports == 1) {
                                String notif = "You may experience delay in mens due to sports activeness!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString7)) {
                            boolean check = checkOverWeight(iHeight, iWeight);
                            if (check) {
                                String notif = "Lose weight to avoid delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                        if (scurrentDate.equals(dateString10)) {
                            if (iPills == 1) {
                                String notif = "Taking pills causes delayed menstruation!!!";
                                createNotif("Menstrual Calendar", notif, context, pendingIntent);
                                HistoryModel temp = new HistoryModel();
                                temp.setText(notif);
                                db.setHistory(temp, sid);
                            }
                        }
                    }
                    if (iCramps == 1)
                    {
                        setCurrentDayNotif(rightNow, context, pendingIntent);
                    }
                }

                if (iMessages == 1)
                {
                    String phoneNumber = db.getContact(suname);
                    String textMessage = suname + " menstrual period is  about to start in 3 days....";
                    Date date = null;
                    long dateToCompare;
                    long dateInMilliseconds;
                    long threeDays = TimeUnit.DAYS.toMillis(3);
                    for (int i = 0; i < listRed.size(); i++) {
                        String nextDate = listRed.get(i);
                        try {
                            date = _dateFormat.parse(nextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        dateInMilliseconds = date.getTime();
                        dateToCompare = dateInMilliseconds - threeDays;
                        String dateString = _dateFormat.format(new Date(dateToCompare));
                        if (scurrentDate.equals(dateString))
                        {
                            sendMessage(context, phoneNumber, textMessage, pendingIntent);
                        }
                    }
                }
            }
        }


    }

    public void createNotif(String title, String content, Context context, PendingIntent pendingIntent) {
        Random rand = new Random();
        int m = rand.nextInt(999999999) + 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContent(contentView)
                .setAutoCancel(true);
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(m, mBuilder.build());
    }

    private List<String> getDateList(String datesMap) {
        String[] dates = datesMap.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, dates);
        return list;
    }

    private boolean checkOverWeight(double weight, double height) {
        boolean check;

        double bmi = (weight * 703) / (height * height);
        check = bmi >= 25;

        return check;
    }

    public void setCurrentDayNotif(Calendar cal, Context context, PendingIntent pendingIntent) {
        String[] notifOfTheDay = {"Put a bolster or pillow under the torso to make a cozier space more conducive to holding the pose longer", "Grasp opposite elbows in the pose and release the neck muscles to further unwind", "When in the pose, rock from side to side and front to back to give yourself a back massage", "Relax the legs, shoulders, and hips, letting gravity take over the twisting process. Prop the knees on top of a bolster or pillow if you have trouble relaxing in the twist", "Move from one pose to the other as slowly as needed, lingering where desired. Remember to inhale to rise with Cow pose, and exhale to coil inwards with Cat pose", "The key in Savasana is to soften the body and mind. Feel the support of the ground beneath you, and bring attention to the breath. By controlling the breath with deep and meaningful inhalation and exhalation, the mind has space to quietly focus and redirect attention away from pain"};
        Random r = new Random();
        int randomNumber = r.nextInt(notifOfTheDay.length);
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        if (currentDay == Calendar.MONDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else if (currentDay == Calendar.TUESDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else if (currentDay == Calendar.WEDNESDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else if (currentDay == Calendar.THURSDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else if (currentDay == Calendar.FRIDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else if (currentDay == Calendar.SATURDAY) {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        } else {
            createNotif("Menstrual Calendar", notifOfTheDay[randomNumber], context, pendingIntent);
            HistoryModel temp = new HistoryModel();
            temp.setText(notifOfTheDay[randomNumber]);
            db.setHistory(temp, sid);
        }
    }

    private void sendMessage(Context context, String phoneNumber, String message, PendingIntent pendingIntent) {
        if (phoneNumber == null || phoneNumber.length() == 0)
        {
            createNotif("Periodical App", "Unable to send text message. Invalid phone number", context, pendingIntent);
        }
        else
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
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

        Intent intent1 = new Intent(context, AlarmReceiver.class);

        PendingIntent broadcast1 = PendingIntent.getBroadcast(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(broadcast1);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar2.getTimeInMillis(), broadcast1), broadcast1);
        }
    }

}
