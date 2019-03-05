package com.thesis.group.periodecalcalendar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Memo implements Serializable {
    private Date date;
    private String text;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.getDefault());

    Memo() {
        this.date = new Date();
    }

    Memo(long time, String text) {
        this.date = new Date(time);
        this.text = text;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    String getShortText() {
        String temp = text.replaceAll("\n", " ");
        if (temp.length() > 25) {
            return temp.substring(0, 25) + "...";
        } else {
            return temp;
        }
    }

    void setFullDisplayed(boolean fullDisplayed) {
        this.fullDisplayed = fullDisplayed;
    }

    boolean isFullDisplayed() {
        return this.fullDisplayed;
    }
    @NonNull
    @Override
    public String toString() {
        return this.text;
    }
}