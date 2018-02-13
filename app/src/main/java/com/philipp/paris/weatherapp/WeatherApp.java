package com.philipp.paris.weatherapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class WeatherApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
