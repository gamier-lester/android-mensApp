package com.thesis.group.periodecalcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private static final String PREF_NAME = "periodecalTablePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";

    public SessionManager(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    void createLoginSession(int id, String userName)
    {
        String sid = Integer.toString(id);
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, sid);
        editor.putString(KEY_USERNAME, userName);
        editor.commit();
    }

    public boolean checkLogin()
    {
        boolean check = false;
        if(!this.isLoggedIn()){
            check = true;
        }
        return check;
    }

    boolean redirectToMain()
    {
        boolean check = false;
        if(this.isLoggedIn())
        {
            check = true;
        }
        return check;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }

    void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, signin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("EXIT", true);
        _context.startActivity(i);
    }

    private boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
