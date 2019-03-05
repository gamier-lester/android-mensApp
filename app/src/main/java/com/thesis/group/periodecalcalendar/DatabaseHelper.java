package com.thesis.group.periodecalcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="register.db";

    private static final String regPerCol2 = "Username";
    private static final String regPerCol3 = "Password";
    private static final String regPerCol4 = "VerifyPassword";
    private static final String regPerCol5 = "Phone";
    private static final String regPerCol6 = "Email";
    private static final String regPerCol7 = "BirthDate";
    private static final String regPerCol8 = "Access";
    private static final String regPerCol9 = "DateRegister";
    private static final String regPerCol10 = "MenstrualCategory";

    private static final String nonRegularMensCol2 = "UserAge";
    private static final String nonRegularMensCol3 = "UserHeight";
    private static final String nonRegularMensCol4 = "UserWeight";
    private static final String nonRegularMensCol5 = "Working";
    private static final String nonRegularMensCol6 = "SportsActive";
    private static final String nonRegularMensCol7 = "TakingPills";
    private static final String nonRegularMensCol8 = "BreastFeeding";
    private static final String nonRegularMensCol9 = "LastMenstruation";
    private static final String nonRegularMensCol10 = "person_id";

    private static final String regularMensCol2 = "UserAge";
    private static final String regularMensCol3 = "UserHeight";
    private static final String regularMensCol4 = "UserWeight";
    private static final String regularMensCol5 = "PeriodLength";
    private static final String regularMensCol6 = "LastPeriod";
    private static final String regularMensCol7 = "MenstrualDelay";
    private static final String regularMensCol8 = "DelayCounts";
    private static final String regularMensCol9 = "Working";
    private static final String regularMensCol10 = "SportsActive";
    private static final String regularMensCol11 = "Status";
    private static final String regularMensCol12 = "TakingPills";
    private static final String regularMensCol13 = "StomachCramp";
    private static final String regularMensCol14 = "person_id";

    private static final String mensDataCol2 = "Counter";
    private static final String mensDataCol3 = "person_id";

    private static final String settingsCol2 = "NotifyMenstruation";
    private static final String settingsCol3 = "NotifyFertile";
    private static final String settingsCol4 = "NotifyNonFertile";
    private static final String settingsCol5 = "TipsAndIntervention";
    private static final String settingsCol6 = "NotifyTextMessage";
    private static final String settingsCol7 = "person_id";

    private static final String notesCol2 = "Date";
    private static final String notesCol3 = "Memo";
    private static final String notesCol4 = "person_id";

    private static final String remindersCol2 = "Date";
    private static final String remindersCol3 = "Repeat";
    private static final String remindersCol4 = "Hours";
    private static final String remindersCol5 = "Minutes";
    private static final String remindersCol6 = "Reminders";
    private static final String remindersCol7 = "Sound";
    private static final String remindersCol8 = "person_id";

    private static final String historyCol2 = "Date";
    private static final String historyCol3 = "History";
    private static final String historyCol4 = "person_id";

    private static final String pictureCol2 = "Image";
    private static final String pictureCol3 = "person_id";

    static final String KEY_email = "email";
    static final String KEY_access = "access";

    static final String KEY_mens = "mensNotif";
    static final String KEY_fertile = "fertileNotif";
    static final String KEY_nonFertile = "nonFertileNotif";
    static final String KEY_tips = "tipsNotif";
    static final String KEY_messages = "textNotif";

    static final String KEY_height = "height";
    static final String KEY_weight = "weight";
    static final String KEY_work = "workRegMens";
    static final String KEY_sports = "sportsRegMens";
    static final String KEY_pills = "pillsRegMens";
    static final String KEY_cramps = "crampsRegMens";
    static final String KEY_breast = "breastFeeding";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    boolean registerPerson(String username, String password, String verifyPassword, String phone, String email, String birthDate, int Access, String dateRegistered, int menstrualCategory)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regPerCol2, username);
        contentValues.put(regPerCol3, password);
        contentValues.put(regPerCol4, verifyPassword);
        contentValues.put(regPerCol5, phone);
        contentValues.put(regPerCol6, email);
        contentValues.put(regPerCol7, birthDate);
        contentValues.put(regPerCol8, Access);
        contentValues.put(regPerCol9, dateRegistered);
        contentValues.put(regPerCol10, menstrualCategory);
        long rowInserted = db.insert("registerInfo", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean registerPersonMensCat(int mensCat, String username)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regPerCol10, mensCat);
        db.update("registerInfo", contentValues,"Username=?",new String[]{username});
        db.close();
        return true;
    }

    boolean updateAccess(String username)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regPerCol8, 1);
        db.update("registerInfo", contentValues,"Username=?",new String[]{username});
        db.close();
        return true;
    }

    boolean setNonRegMensInfo(int age, int height, int weight, int working, int sportsActive, int takingPills, int breastFeeding, String lastMenstruation, int personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(nonRegularMensCol2, age);
        contentValues.put(nonRegularMensCol3, height);
        contentValues.put(nonRegularMensCol4, weight);
        contentValues.put(nonRegularMensCol5, working);
        contentValues.put(nonRegularMensCol6, sportsActive);
        contentValues.put(nonRegularMensCol7, takingPills);
        contentValues.put(nonRegularMensCol8, breastFeeding);
        contentValues.put(nonRegularMensCol9, lastMenstruation);
        contentValues.put(nonRegularMensCol10, personId);
        long rowInserted = db.insert("nonRegMensInfo", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setRegMensInfo(int age, double height, double weight, int periodlength, String lastPeriod, int menstrualDelay, int delayCounts, int working, int sportsActive, String status, int takingPills, int stomachCramp, int personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regularMensCol2, age);
        contentValues.put(regularMensCol3, height);
        contentValues.put(regularMensCol4, weight);
        contentValues.put(regularMensCol5, periodlength);
        contentValues.put(regularMensCol6, lastPeriod);
        contentValues.put(regularMensCol7, menstrualDelay);
        contentValues.put(regularMensCol8, delayCounts);
        contentValues.put(regularMensCol9, working);
        contentValues.put(regularMensCol10, sportsActive);
        contentValues.put(regularMensCol11, status);
        contentValues.put(regularMensCol12, takingPills);
        contentValues.put(regularMensCol13, stomachCramp);
        contentValues.put(regularMensCol14, personId);
        long rowInserted = db.insert("regMensInfo", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setAccountSetting(int notifyMenstruation, int notifyFertile, int notifyNonFertile, int tipsAndIntervention, int notifyTextMessage, int personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(settingsCol2, notifyMenstruation);
        contentValues.put(settingsCol3, notifyFertile);
        contentValues.put(settingsCol4, notifyNonFertile);
        contentValues.put(settingsCol5, tipsAndIntervention);
        contentValues.put(settingsCol6, notifyTextMessage);
        contentValues.put(settingsCol7, personId);
        long rowInserted = db.insert("accountSett", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateAccountSetting(int notifyMenstruation, int notifyFertile, int notifyNonFertile, int tipsAndIntervention, int notifyTextMessage, String personId)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(settingsCol2, notifyMenstruation);
        contentValues.put(settingsCol3, notifyFertile);
        contentValues.put(settingsCol4, notifyNonFertile);
        contentValues.put(settingsCol5, tipsAndIntervention);
        contentValues.put(settingsCol6, notifyTextMessage);
        contentValues.put(settingsCol7, personId);
        try
        {
            db.update("accountSett", contentValues,"person_id=?",new String[]{personId});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setCounter(int counter, int id)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mensDataCol2, counter);
        contentValues.put(mensDataCol3, id);
        long rowInserted = db.insert("mensData", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateCounter(int counter, int id)
    {
        boolean check= true;
        String sid = Integer.toString(id);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mensDataCol2, counter);
        try
        {
            db.update("mensData", contentValues,"person_id=?",new String[]{sid});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setMemo(Memo memo, String personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(notesCol2, memo.getTime());
        contentValues.put(notesCol3, memo.getText());
        contentValues.put(notesCol4, personId);
        long rowInserted = db.insert("notes", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateMemo(Memo memo, String personId)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(notesCol2, new Date().getTime());
        contentValues.put(notesCol3, memo.getText());
        String date = Long.toString(memo.getTime());
        try
        {
            db.update("notes", contentValues,"person_id=? AND Date=?",new String[]{personId,date});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setReminder(ReminderModel remo, String personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(remindersCol2, remo.getTime());
        contentValues.put(remindersCol3, remo.getRepeat());
        contentValues.put(remindersCol4, remo.getHours());
        contentValues.put(remindersCol5, remo.getMins());
        contentValues.put(remindersCol6, remo.getText());
        contentValues.put(remindersCol7, remo.getSound());
        contentValues.put(remindersCol8, personId);
        long rowInserted = db.insert("reminders", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateSound(ReminderModel remo, String personId)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(remindersCol7, remo.getSound());
        String date = Long.toString(remo.getTime());
        try
        {
            db.update("reminders", contentValues,"person_id=? AND Date=?",new String[]{personId,date});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setPicture(String imagePath, String id)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pictureCol2, imagePath);
        contentValues.put(pictureCol3, id);
        long rowInserted = db.insert("picture", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updatePicture(String imagePath, String id)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pictureCol2, imagePath);
        contentValues.put(pictureCol3, id);
        try
        {
            db.update("picture", contentValues,"person_id=?",new String[]{id});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateProfile(double height, double weight, int working, int sports, String id)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regularMensCol3, height);
        contentValues.put(regularMensCol4, weight);
        contentValues.put(regularMensCol9, working);
        contentValues.put(regularMensCol10, sports);
        try
        {
            db.update("regMensInfo", contentValues,"person_id=?",new String[]{id});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateNonRegProfile(double height, double weight, int working, int sports, int breast, String id)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(nonRegularMensCol3, height);
        contentValues.put(nonRegularMensCol4, weight);
        contentValues.put(nonRegularMensCol5, working);
        contentValues.put(nonRegularMensCol6, sports);
        contentValues.put(nonRegularMensCol8, breast);
        try
        {
            db.update("nonRegMensInfo", contentValues,"person_id=?",new String[]{id});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean updateDateCalendar(String date, String id)
    {
        boolean check = true;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(regularMensCol6, date);
        contentValues.put(regularMensCol14, id);
        try
        {
            db.update("regMensInfo", contentValues,"person_id=?",new String[]{id});
        }
        catch (SQLException e)
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean setHistory(HistoryModel hemo, String personId)
    {
        boolean check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(historyCol2, new Date().getTime());
        contentValues.put(historyCol3, hemo.getText());
        contentValues.put(historyCol4, personId);
        long rowInserted = db.insert("history", null, contentValues);
        if (rowInserted != -1)
        {
            check = true;
        }
        else
        {
            check = false;
        }
        db.close();
        return check;
    }

    boolean checkRegMensQuestionnaire(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"person_id"}, "person_id=?", new String[]{id}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        else
        {
            cursor.close();
            db.close();
            return false;
        }
    }

    boolean checkNonRegMensQuestionnaire(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("nonRegMensInfo", new String[]{"person_id"}, "person_id=?", new String[]{id}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        else
        {
            cursor.close();
            db.close();
            return false;
        }
    }

    boolean checkUsername(String username)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Username"}, "Username=?", new String[]{username}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        else
        {
            cursor.close();
            db.close();
            return false;
        }

    }

    HashMap<String, String> getEmailAndAccess(String username)
    {
        HashMap<String, String> emailAndAccessMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Email","Access"}, "Username=?", new String[]{username}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            emailAndAccessMap.put(KEY_email, cursor.getString(cursor.getColumnIndex("Email")));
            emailAndAccessMap.put(KEY_access, String.valueOf(cursor.getInt(cursor.getColumnIndex("Access"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return emailAndAccessMap;
    }

    HashMap<String, String> getEmailAndAccessForgotPassword(String email)
    {
        HashMap<String, String> emailAndAccessMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Email","Access"}, "Email=?", new String[]{email}, null, null, null);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                while(!cursor.isAfterLast())
                {
                    emailAndAccessMap.put(KEY_email, cursor.getString(cursor.getColumnIndex("Email")));
                    emailAndAccessMap.put(KEY_access, String.valueOf(cursor.getInt(cursor.getColumnIndex("Access"))));
                    cursor.moveToNext();
                }
                cursor.close();
                db.close();
            }
            else
            {
                emailAndAccessMap.put(KEY_email, "thisEmailDoesntExist");
                emailAndAccessMap.put(KEY_access, "0");
                cursor.close();
                db.close();
            }

        }
        else
        {
            emailAndAccessMap.put(KEY_email, "thisEmailDoesntExist");
            emailAndAccessMap.put(KEY_access, "0");
            cursor.close();
            db.close();
        }
        return emailAndAccessMap;
    }

    boolean verifyLogin(String username, String password)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Username","Password"}, "Username=? AND Password=?", new String[]{username,password}, null, null, null);
//        if (cursor != null)
//        {
            if (cursor.getCount() > 0)
            {
                cursor.close();
                db.close();
                return true;

            } else
            {
                cursor.close();
                db.close();
                return false;
            }
//        }
//        else
//        {
//            cursor.close();
//            db.close();
//            return false;
//        }
    }

    String getPassword(String email)
    {
        String password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Password"}, "Email=?", new String[]{email}, null, null, null);

        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                password = cursor.getString(cursor.getColumnIndex("Password"));
                cursor.close();
                db.close();
                return password;

            } else
            {
                password = "*%&$32435fhfeinvslhklshvje**(*(";
                cursor.close();
                db.close();
                return password;
            }
        }
        else
        {
            password = "*%&$32435fhfeinvslhklshvje**(*(";
            cursor.close();
            db.close();
            return password;
        }
    }

    int getId(String username)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"ID"}, "Username=?", new String[]{username}, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        db.close();
        return id;
    }

    int getPeriodLength(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"PeriodLength"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        int length = cursor.getInt(cursor.getColumnIndex("PeriodLength"));
        cursor.close();
        db.close();
        return length;
    }

    String getLastMens(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"LastPeriod"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        String lastMens = cursor.getString(cursor.getColumnIndex("LastPeriod"));
        cursor.close();
        db.close();
        return lastMens;
    }

    int getCounter(String id)
    {
        int counter;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("mensData", new String[]{"Counter"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        counter = cursor.getInt(cursor.getColumnIndex("Counter"));
        cursor.close();
        db.close();
        return counter;
    }

    int getMensCat(String username)
    {
        int cat;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"MenstrualCategory"}, "Username=?", new String[]{username}, null, null, null);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                cat = cursor.getInt(cursor.getColumnIndex("MenstrualCategory"));
                cursor.close();
                db.close();
            }
            else
            {
                cat = 0;
                cursor.close();
                db.close();
            }
        }
        else
        {
            cat = 0;
            cursor.close();
            db.close();
        }

        cursor.close();
        db.close();
        return cat;
    }

    String getContact(String username)
    {
        String contact;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Phone"}, "Username=?", new String[]{username}, null, null, null);
        cursor.moveToFirst();
        contact = cursor.getString(cursor.getColumnIndex("Phone"));
        cursor.close();
        db.close();

        return contact;
    }

    String getUsername(String email)
    {
        String username;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("registerInfo", new String[]{"Username"}, "Email=?", new String[]{email}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            username = String.valueOf(cursor.getString(cursor.getColumnIndex("Username")));
            cursor.close();
            db.close();
        }
        else
        {
            username = "Menstrual_Calendar_App";
            cursor.close();
            db.close();
        }
        return username;

    }

    boolean deleteNotes(Memo memo, String personId)
    {
        boolean check = false;

        SQLiteDatabase db = getWritableDatabase();
        String date = Long.toString(memo.getTime());
        db.delete("notes", "Date = ? AND person_id = ?", new String[]{date, personId});
        db.close();

        return check;
    }

    boolean deleteReminders(ReminderModel remo, String personId)
    {
        boolean check = false;

        SQLiteDatabase db = getWritableDatabase();
        String date = Long.toString(remo.getTime());
        db.delete("reminders", "Date = ? AND person_id = ?", new String[]{date, personId});
        db.close();

        return check;
    }

    boolean deleteHistory(HistoryModel hemo, String personId)
    {
        boolean check = false;

        SQLiteDatabase db = getWritableDatabase();
        String date = Long.toString(hemo.getTime());
        db.delete("history", "Date = ? AND person_id = ?", new String[]{date, personId});
        db.close();

        return check;
    }

    boolean deleteAllHistory(String personId)
    {
        boolean check = false;

        SQLiteDatabase db = getWritableDatabase();
        db.delete("history", "person_id = ?", new String[]{personId});
        db.close();

        return check;
    }

    HashMap<String, String> getSettings(String id)
    {
        HashMap<String, String> settingsMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("accountSett", new String[]{"NotifyMenstruation","NotifyFertile","NotifyNonFertile","TipsAndIntervention","NotifyTextMessage"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            settingsMap.put(KEY_mens, String.valueOf(cursor.getInt(cursor.getColumnIndex("NotifyMenstruation"))));
            settingsMap.put(KEY_fertile, String.valueOf(cursor.getInt(cursor.getColumnIndex("NotifyFertile"))));
            settingsMap.put(KEY_nonFertile, String.valueOf(cursor.getInt(cursor.getColumnIndex("NotifyNonFertile"))));
            settingsMap.put(KEY_tips, String.valueOf(cursor.getInt(cursor.getColumnIndex("TipsAndIntervention"))));
            settingsMap.put(KEY_messages, String.valueOf(cursor.getInt(cursor.getColumnIndex("NotifyTextMessage"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return settingsMap;
    }

    HashMap<String, String> getRegMensInfo(String id)
    {
        HashMap<String, String> regMensMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"UserHeight","UserWeight","Working","SportsActive","TakingPills","StomachCramp"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            regMensMap.put(KEY_height, String.valueOf(cursor.getInt(cursor.getColumnIndex("UserHeight"))));
            regMensMap.put(KEY_weight, String.valueOf(cursor.getInt(cursor.getColumnIndex("UserWeight"))));
            regMensMap.put(KEY_work, String.valueOf(cursor.getInt(cursor.getColumnIndex("Working"))));
            regMensMap.put(KEY_sports, String.valueOf(cursor.getInt(cursor.getColumnIndex("SportsActive"))));
            regMensMap.put(KEY_pills, String.valueOf(cursor.getInt(cursor.getColumnIndex("TakingPills"))));
            regMensMap.put(KEY_cramps, String.valueOf(cursor.getInt(cursor.getColumnIndex("StomachCramp"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return regMensMap;
    }

    List getAllMemos(String id) {
        List memos = new ArrayList<>();
        String args[] = {id};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From notes WHERE person_id = ? ORDER BY Date DESC", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(1);
            String text = cursor.getString(2);
            memos.add(new Memo(time, text));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return memos;
    }

    List getAllRemos(String id) {
        List remos = new ArrayList<>();
        String args[] = {id};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From reminders WHERE person_id = ? ORDER BY Date DESC ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(1);
            String repeat = cursor.getString(2);
            String hours = cursor.getString(3);
            String mins = cursor.getString(4);
            String text = cursor.getString(5);
            int sound = cursor.getInt(6);
            remos.add(new ReminderModel(time, repeat, hours, mins,text, sound));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return remos;
    }

    List getReminders(String id, String repeat, String hours, String minutes) {
        List<String> reminders = new ArrayList<>();
        String args[] = {repeat,id,hours,minutes};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From reminders WHERE Repeat = ? AND person_id = ? AND Hours = ? AND Minutes = ? ORDER BY Date DESC ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String text = cursor.getString(5);
            reminders.add(text);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return reminders;
    }

    List getSounds(String id, String repeat, String hours, String minutes) {
        List<String> sounds = new ArrayList<>();
        String args[] = {repeat,id,hours,minutes};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From reminders WHERE Repeat = ? AND person_id = ? AND Hours = ? AND Minutes = ? ORDER BY Date DESC ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String sound = Integer.toString(cursor.getInt(6));
            sounds.add(sound);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sounds;
    }

    List getAllHistory(String id) {
        List hemos = new ArrayList<>();
        String args[] = {id};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From history WHERE person_id = ? ORDER BY Date DESC ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(1);
            String text = cursor.getString(2);
            hemos.add(new HistoryModel(time, text));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return hemos;
    }

    boolean checkPicture(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("picture", new String[]{"Image"}, "person_id=?", new String[]{id}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        else
        {
            cursor.close();
            db.close();
            return false;
        }

    }

    String getPicture(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("picture", new String[]{"Image"}, "person_id=?", new String[]{id}, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex("Image"));
            cursor.close();
            db.close();
            return imagePath;
        }
        else
        {
            cursor.close();
            db.close();
            return "";
        }
    }

    HashMap<String, String> getRegProfile(String id)
    {
        HashMap<String, String> regProfileMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"UserHeight","UserWeight","Working","SportsActive"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            regProfileMap.put(KEY_height, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserHeight"))));
            regProfileMap.put(KEY_weight, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserWeight"))));
            regProfileMap.put(KEY_work, String.valueOf(cursor.getInt(cursor.getColumnIndex("Working"))));
            regProfileMap.put(KEY_sports, String.valueOf(cursor.getInt(cursor.getColumnIndex("SportsActive"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return regProfileMap;
    }

    HashMap<String, String> getNonRegProfile(String id)
    {
        HashMap<String, String> nonRegProfileMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("nonRegMensInfo", new String[]{"UserHeight","UserWeight","Working","SportsActive","BreastFeeding"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            nonRegProfileMap.put(KEY_height, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserHeight"))));
            nonRegProfileMap.put(KEY_weight, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserWeight"))));
            nonRegProfileMap.put(KEY_work, String.valueOf(cursor.getInt(cursor.getColumnIndex("Working"))));
            nonRegProfileMap.put(KEY_sports, String.valueOf(cursor.getInt(cursor.getColumnIndex("SportsActive"))));
            nonRegProfileMap.put(KEY_breast, String.valueOf(cursor.getInt(cursor.getColumnIndex("BreastFeeding"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return nonRegProfileMap;
    }

    HashMap<String, String> getNonRegInfo(String id)
    {
        HashMap<String, String> nonRegInfoMap = new HashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("nonRegMensInfo", new String[]{"UserHeight","UserWeight","Working","SportsActive","TakingPills","BreastFeeding"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            nonRegInfoMap.put(KEY_height, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserHeight"))));
            nonRegInfoMap.put(KEY_weight, String.valueOf(cursor.getDouble(cursor.getColumnIndex("UserWeight"))));
            nonRegInfoMap.put(KEY_work, String.valueOf(cursor.getInt(cursor.getColumnIndex("Working"))));
            nonRegInfoMap.put(KEY_sports, String.valueOf(cursor.getInt(cursor.getColumnIndex("SportsActive"))));
            nonRegInfoMap.put(KEY_pills, String.valueOf(cursor.getInt(cursor.getColumnIndex("TakingPills"))));
            nonRegInfoMap.put(KEY_breast, String.valueOf(cursor.getInt(cursor.getColumnIndex("BreastFeeding"))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return nonRegInfoMap;
    }

    String getDateCalendar(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("regMensInfo", new String[]{"LastPeriod"}, "person_id=?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex("LastPeriod"));
        cursor.close();
        db.close();
        return imagePath;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String sqlRegisterPerson = "CREATE TABLE  registerInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT,Username TEXT NOT NULL UNIQUE,Password TEXT,VerifyPassword TEXT,Phone TEXT,Email TEXT,BirthDate TEXT,Access INTEGER,DateRegister TEXT,MenstrualCategory INTEGER)";
        String sqlNonRegMensInfo = "CREATE TABLE  nonRegMensInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT,UserAge INTEGER,UserHeight INTEGER,UserWeight INTEGER,Working INTEGER,SportsActive INTEGER,TakingPills INTEGER,BreastFeeding INTEGER,LastMenstruation TEXT,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlRegMensInfo = "CREATE TABLE  regMensInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT,UserAge INTEGER,UserHeight INTEGER,UserWeight INTEGER,PeriodLength INTEGER,LastPeriod TEXT,MenstrualDelay INTEGER,DelayCounts INTEGER,Working INTEGER,SportsActive INTEGER,Status TEXT,TakingPills INTEGER,StomachCramp INTEGER,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlAccountSett = "CREATE TABLE  accountSett(ID INTEGER PRIMARY KEY AUTOINCREMENT,NotifyMenstruation INTEGER,NotifyFertile INTEGER,NotifyNonFertile INTEGER,TipsAndIntervention INTEGER,NotifyTextMessage,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlMensData = "CREATE TABLE  mensData(ID INTEGER PRIMARY KEY AUTOINCREMENT,Counter INTEGER,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlNotes = "CREATE TABLE  notes(ID INTEGER PRIMARY KEY AUTOINCREMENT,Date INTEGER, Memo TEXT,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlReminders = "CREATE TABLE  reminders(ID INTEGER PRIMARY KEY AUTOINCREMENT,Date INTEGER, Repeat TEXT,Hours TEXT,Minutes TEXT,Reminders TEXT,Sound INTEGER,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlHistory = "CREATE TABLE  history(ID INTEGER PRIMARY KEY AUTOINCREMENT,Date INTEGER, History TEXT,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";
        String sqlPicture = "CREATE TABLE  picture(ID INTEGER PRIMARY KEY AUTOINCREMENT,Image TEXT,person_id INTEGER,FOREIGN KEY(person_id) REFERENCES registerInfo(ID))";

        db.execSQL(sqlRegisterPerson);
        db.execSQL(sqlNonRegMensInfo);
        db.execSQL(sqlRegMensInfo);
        db.execSQL(sqlAccountSett);
        db.execSQL(sqlMensData);
        db.execSQL(sqlNotes);
        db.execSQL(sqlReminders);
        db.execSQL(sqlHistory);
        db.execSQL(sqlPicture);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        String sqlRegisterPerson = "DROP TABLE IF EXISTS registerInfo";
        String sqlNonRegMensInfo = "DROP TABLE IF EXISTS nonRegisterInfo";
        String sqlRegMensInfo = "DROP TABLE IF EXISTS regMensInfo";
        String sqlAccountSett = "DROP TABLE IF EXISTS accountSett";
        String sqlMensData = "DROP TABLE IF EXISTS mensData";
        String sqlNotes = "DROP TABLE IF EXISTS notes";
        String sqlReminders = "DROP TABLE IF EXISTS reminders";
        String sqlHistory = "DROP TABLE IF EXISTS history";
        String sqlPicture = "DROP TABLE IF EXISTS picture";

        db.execSQL(sqlRegisterPerson);
        db.execSQL(sqlNonRegMensInfo);
        db.execSQL(sqlRegMensInfo);
        db.execSQL(sqlAccountSett);
        db.execSQL(sqlMensData);
        db.execSQL(sqlNotes);
        db.execSQL(sqlReminders);
        db.execSQL(sqlHistory);
        db.execSQL(sqlPicture);
        onCreate(db);
    }
}
