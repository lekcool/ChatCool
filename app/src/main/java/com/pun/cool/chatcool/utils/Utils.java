package com.pun.cool.chatcool.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Cool on 16/3/2560.
 */

public class Utils {

    private static Utils ourInstance;

    private static String today;

    public static Utils getInstance() {
        if (ourInstance == null) ourInstance = new Utils();
        return ourInstance;
    }

    private Utils() {
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @SuppressLint("SimpleDateFormat")
    public String getTimeStamp(String dateStr) {
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        if (today != null) {
            today = today.length() < 2 ? "0" + today : today;
        }
        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            timestamp = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeStamp(Long dateLong) {
        SimpleDateFormat format;
        String timestamp = "";
        if (dateLong == null) {
            return timestamp;
        }

        today = today.length() < 2 ? "0" + today : today;

        Date date = new Date(dateLong);
        SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
        String dateToday = todayFormat.format(date);
        format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
        timestamp = format.format(date);

        return timestamp;
    }
}
