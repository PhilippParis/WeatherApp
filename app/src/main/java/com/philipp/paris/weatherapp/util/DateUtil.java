package com.philipp.paris.weatherapp.util;


import android.content.Context;
import android.support.v4.util.Pair;

import com.philipp.paris.weatherapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;
    public static final int WEEK = 7 * DAY;

    public static Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public static Pair<Date, Date> getStartEndOfDay(Date day) {
        Calendar from = getCalendarAtStartOfDay(day);
        Calendar to = getCalendarAtStartOfDay(day);
        to.add(Calendar.DAY_OF_YEAR, 1);
        return new Pair<>(from.getTime(), to.getTime());
    }

    public static Pair<Date, Date> getStartEndOfCurrentDay() {
        Calendar from = getCalendarAtStartOfDay(getCurrentTime());
        Calendar to = getCalendarAtStartOfDay(getCurrentTime());
        to.add(Calendar.DAY_OF_YEAR, 1);
        return new Pair<>(from.getTime(), to.getTime());
    }

    public static Pair<Date, Date> getStartEndOfCurrentWeek() {
        Calendar calendar = getCalendarAtStartOfDay(getCurrentTime());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date from = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date to = calendar.getTime();

        return new Pair<>(from, to);
    }

    public static boolean isNight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) >= 21 || calendar.get(Calendar.HOUR_OF_DAY) < 6;
    }

    public static long diff(Date from, Date to, int unit) {
        return (to.getTime() - from.getTime()) / unit;
    }

    public static Pair<Date, Date> before(Pair<Date, Date> range, int millis) {
        long from = range.first.getTime();
        long to = range.second.getTime();
        return new Pair<>(new Date(from - millis), new Date(to - millis));
    }

    public static Pair<Date, Date> after(Pair<Date, Date> range, int millis) {
        long from = range.first.getTime();
        long to = range.second.getTime();
        return new Pair<>(new Date(from + millis), new Date(to + millis));
    }

    public static String format(Context context, Date date, String format) {
        if (diff(getCurrentTime(), date, DAY) == 0) {
            return context.getResources().getString(R.string.today);
        } else if (diff(getCurrentTime(), date, DAY) == 1) {
            return context.getResources().getString(R.string.tomorrow);
        }

        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    private static Calendar getCalendarAtStartOfDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}
