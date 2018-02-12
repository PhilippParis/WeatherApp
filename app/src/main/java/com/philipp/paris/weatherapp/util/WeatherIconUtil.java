package com.philipp.paris.weatherapp.util;


import android.content.Context;
import java.util.Date;

public class WeatherIconUtil {

    public static int getDrawableID(Context context, Date time, String key) {
        if (DateUtil.isNight(time)) {
            key = "nt_" + key;
        }
        return context.getResources().getIdentifier(key, "drawable", context.getPackageName());
    }
}
