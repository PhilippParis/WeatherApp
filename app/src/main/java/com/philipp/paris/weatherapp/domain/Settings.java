package com.philipp.paris.weatherapp.domain;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.URL;

public class Settings {
    private final String KEY_PREF_DB_URL = "pref_key_db_url";
    private final String KEY_PREF_DB_USER = "pref_key_db_user";
    private final String KEY_PREF_DB_PW = "pref_key_db_password";
    private final String KEY_PREF_HOME = "pref_key_home";
    private final String KEY_PREF_SHOW_HOME = "pref_key_show_home_data";

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String homeLocation;
    private boolean showHomeLocationData;
    private SharedPreferences pref;

    public Settings(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.dbUrl = pref.getString(KEY_PREF_DB_URL, "");
        this.dbUsername = pref.getString(KEY_PREF_DB_USER, "");
        this.dbPassword = pref.getString(KEY_PREF_DB_PW, "");
        this.homeLocation = pref.getString(KEY_PREF_HOME, "");
        this.showHomeLocationData = pref.getBoolean(KEY_PREF_SHOW_HOME, false);
    }

    public void persist() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PREF_DB_URL, dbUrl);
        editor.putString(KEY_PREF_DB_USER, dbUsername);
        editor.putString(KEY_PREF_DB_PW, dbPassword);
        editor.putString(KEY_PREF_HOME, homeLocation);
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

    public String getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = homeLocation;
    }

    public boolean showHomeLocationData() {
        return showHomeLocationData;
    }

    public void setShowHomeLocationData(boolean showHomeLocationData) {
        this.showHomeLocationData = showHomeLocationData;
    }
}
