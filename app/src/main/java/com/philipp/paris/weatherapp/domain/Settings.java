package com.philipp.paris.weatherapp.domain;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.philipp.paris.weatherapp.WeatherApp;

public class Settings {
    private final String KEY_PREF_DB_URL = "pref_key_db_url";
    private final String KEY_PREF_DB_USER = "pref_key_db_user";
    private final String KEY_PREF_DB_PW = "pref_key_db_password";
    private final String KEY_PREF_HOME = "pref_key_home";
    private final String KEY_PREF_SHOW_HOME = "pref_key_show_home_data";
    private final String KEY_CURRENT_LOCATION = "pref_key_location";

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String homeLocation;
    private String currentLocation;
    private boolean showHomeLocationData;
    private SharedPreferences pref;

    public Settings() {
        pref = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getAppContext());
        this.dbUrl = pref.getString(KEY_PREF_DB_URL, "");
        this.dbUsername = pref.getString(KEY_PREF_DB_USER, "");
        this.dbPassword = pref.getString(KEY_PREF_DB_PW, "");
        this.homeLocation = pref.getString(KEY_PREF_HOME, "");
        this.showHomeLocationData = pref.getBoolean(KEY_PREF_SHOW_HOME, false);
        this.currentLocation = pref.getString(KEY_CURRENT_LOCATION, "");
    }

    public void persist() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PREF_DB_URL, dbUrl);
        editor.putString(KEY_PREF_DB_USER, dbUsername);
        editor.putString(KEY_PREF_DB_PW, dbPassword);
        editor.putString(KEY_PREF_HOME, homeLocation);
        editor.putBoolean(KEY_PREF_SHOW_HOME, showHomeLocationData);
        editor.putString(KEY_CURRENT_LOCATION, currentLocation);
        editor.apply();
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public double getHomeLocationLatitude() {
        if (!homeLocationSet()) {
            return 0.0;
        }
        return Double.valueOf(homeLocation.split(";")[0]);
    }

    public double getHomeLocationLongitude() {
        if (!homeLocationSet()) {
            return 0.0;
        }
        return Double.valueOf(homeLocation.split(";")[1]);
    }

    public boolean homeLocationSet() {
        return !homeLocation.isEmpty();
    }

    public void setHomeLocation(double latitude, double longitude) {
        this.homeLocation = latitude + "," + longitude;
    }

    public boolean showHomeLocationData() {
        return showHomeLocationData;
    }

    public void setShowHomeLocationData(boolean showHomeLocationData) {
        this.showHomeLocationData = showHomeLocationData;
    }

    public boolean currentLocationSet() {
        return !currentLocation.isEmpty();
    }

    public void deleteCurrentLocation() {
        currentLocation = "";
    }

    public void setCurrentLocation(double latitude, double longitude) {
        this.currentLocation = latitude + "," + longitude;
    }

    public double getCurrentLocationLatitude() {
        if (!currentLocationSet()) {
            return 0.0;
        }
        return Double.valueOf(currentLocation.split(",")[0]);
    }

    public double getCurrentLocationLongitude() {
        if (!currentLocationSet()) {
            return 0.0;
        }
        return Double.valueOf(currentLocation.split(",")[1]);
    }
}
