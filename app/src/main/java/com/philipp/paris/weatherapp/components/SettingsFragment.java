package com.philipp.paris.weatherapp.components;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.philipp.paris.weatherapp.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
