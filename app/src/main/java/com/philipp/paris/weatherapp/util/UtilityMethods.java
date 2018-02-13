package com.philipp.paris.weatherapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philipp.paris.weatherapp.WeatherApp;

public class UtilityMethods {
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) WeatherApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
