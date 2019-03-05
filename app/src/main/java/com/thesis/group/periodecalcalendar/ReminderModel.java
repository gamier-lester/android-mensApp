package com.thesis.group.periodecalcalendar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderModel implements Serializable {

    private Date date;
    private String text, repeat, hours, minutes;
    private int sound;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.getDefault());

    ReminderModel() {
        this.date = new Date();
    }

    ReminderModel(long time, String repeat, String hours, String minutes, String text, int sound) {
        this.date = new Date(time);
        this.repeat = repeat;
        this.hours = hours;
        this.minutes = minutes;
        this.text = text;
        this.sound = sound;
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

    public void setRepeat(String repeat) { this.repeat = repeat; }

    public String getRepeat() { return  this.repeat; }

    void setHours(String hours) { this.hours = hours; }

    String getHours() { return  this.hours; }

    void setMins(String minutes) { this.minutes = minutes; }

    String getMins() { return  this.minutes; }

    void setSound(int sound) { this.sound = sound; }

    int getSound() { return  this.sound; }

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
