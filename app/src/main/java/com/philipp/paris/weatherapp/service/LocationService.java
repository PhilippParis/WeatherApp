package com.philipp.paris.weatherapp.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.philipp.paris.weatherapp.WeatherApp;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class LocationService {
    public static int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private static LocationService instance;

    public interface LocationServiceCallback {
        void locationDisabled();
        void insufficientPermissions();
    }


    public static LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    private LocationService() {

    }

    public void getAddress(final ServiceCallback<Address> callback, final LocationServiceCallback lCallback) {
        final LocationManager lm = (LocationManager) WeatherApp.getAppContext().getSystemService(Context.LOCATION_SERVICE);

        // check if location is enabled
        if (lm == null || !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lCallback.locationDisabled();
            return;
        }

        // check permissions
        if (ActivityCompat.checkSelfPermission(WeatherApp.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            lCallback.insufficientPermissions();
            return;
        }

        // use last known location
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            callback.onSuccess(locationToAddress(location));
            return;
        }

        // request new location
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    callback.onSuccess(locationToAddress(location));
                } else {
                    callback.onError(null);
                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        }, null);
    }

    public void openLocationSettings(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                LocationService.REQUEST_LOCATION_PERMISSION_CODE);
    }

    private Address locationToAddress(Location location) {
        Geocoder geocoder = new Geocoder(WeatherApp.getAppContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0);
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
