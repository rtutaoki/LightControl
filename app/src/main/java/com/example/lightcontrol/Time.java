package com.example.lightcontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class Time {
    public static String getNowDateTimeFormat() {
        String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        Date d_date = new Date();
        String s_date = "";
        s_date = s_format.format(d_date);
        return s_date;
    }

    public static int getNowDateTime() {
        Date d_date = new Date();
        String timestamp = String.valueOf(d_date.getTime()/1000);
        int returntime = Integer.valueOf(timestamp);
        Log.i("time",String.valueOf(returntime));
        return returntime;
    }
}