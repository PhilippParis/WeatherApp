package com.philipp.paris.weatherapp.domain;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.philipp.paris.weatherapp.WeatherApp;

public class Settings {
    private final String KEY_PREF_DB_URL = "pref_key_db_url";
    private final String KEY_PREF_DB_USER = "pref_key_db_user";
    private final String KEY_PREF_DB_PW = "pref_key_db_password";
    private final String KEY_PREF_HOME_LATITUDE = "pref_key_home_latitude";
    private final String KEY_PREF_HOME_LONGITUDE = "pref_key_home_longitude";
    private final String KEY_PREF_SHOW_HOME = "pref_key_show_home_data";

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private Float homeLocationLatitude;
    private Float homeLocationLongitude;
    private boolean showHomeLocationData;
    private SharedPreferences pref;

    public Settings() {
        pref = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getAppContext());
        this.dbUrl = pref.getString(KEY_PREF_DB_URL, "");
        this.dbUsername = pref.getString(KEY_PREF_DB_USER, "");
        this.dbPassword = pref.getString(KEY_PREF_DB_PW, "");
        this.homeLocationLatitude = pref.getFloat(KEY_PREF_HOME_LATITUDE, 0f);
        this.homeLocationLongitude = pref.getFloat(KEY_PREF_HOME_LONGITUDE, 0f);
        this.showHomeLocationData = pref.getBoolean(KEY_PREF_SHOW_HOME, false);
    }

    public void persist() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PREF_DB_URL, dbUrl);
        editor.putString(KEY_PREF_DB_USER, dbUsername);
        editor.putString(KEY_PREF_DB_PW, dbPassword);
        editor.putFloat(KEY_PREF_HOME_LATITUDE, homeLocationLatitude);
        editor.putFloat(KEY_PREF_HOME_LONGITUDE, homeLocationLongitude);
        editor.putBoolean(KEY_PREF_SHOW_HOME, showHomeLocationData);
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

    public float getHomeLocationLatitude() {
        return homeLocationLatitude;
    }

    public float getHomeLocationLongitude() {
        return homeLocationLongitude;
    }

    public void setHomeLocation(double latitude, double longitude) {
        this.homeLocationLatitude = (float) latitude;
        this.homeLocationLongitude = (float) longitude;
    }

    public boolean showHomeLocationData() {
        return showHomeLocationData;
    }

    public void setShowHomeLocationData(boolean showHomeLocationData) {
        this.showHomeLocationData = showHomeLocationData;
    }
}
