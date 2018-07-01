package com.philipp.paris.weatherapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.philipp.paris.weatherapp.WeatherApp;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class LocationService {
    private static final String TAG = "LocationService";
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
        // check permissions
        if (ActivityCompat.checkSelfPermission(WeatherApp.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            lCallback.insufficientPermissions();
            return;
        }

        // create location request
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // create location settings request
        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        // check location settings
        SettingsClient client = LocationServices.getSettingsClient(WeatherApp.getAppContext());
        client.checkLocationSettings(settingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        getLocation(callback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lCallback.locationDisabled();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void getLocation(final ServiceCallback<Address> callback) {
        // get location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(WeatherApp.getAppContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.v(TAG, "location retrieved " + location);
                    callback.onSuccess(locationToAddress(location));
                } else {
                    Log.e(TAG, "failed to retrieve location");
                    callback.onError(new Exception("failed to retrieve location"));
                }
            }
        });
    }

    public void openLocationSettings(Activity activity) {
        Log.v(TAG, "openLocationSettings");
        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void requestPermissions(Activity activity) {
        Log.v(TAG, "requestPermissions");
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                LocationService.REQUEST_LOCATION_PERMISSION_CODE);
    }

    private Address locationToAddress(Location location) {
        Log.v(TAG, "locationToAddress: " + location);
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
