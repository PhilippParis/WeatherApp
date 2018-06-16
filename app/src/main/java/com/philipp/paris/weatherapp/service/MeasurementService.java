package com.philipp.paris.weatherapp.service;

import android.support.v4.util.Pair;
import android.util.Log;

import com.philipp.paris.influxdb.InfluxDB;
import com.philipp.paris.influxdb.InfluxDBCallback;
import com.philipp.paris.influxdb.query.InfluxDBQuery;
import com.philipp.paris.influxdb.query.InfluxDBQueryResult;

import com.philipp.paris.weatherapp.WeatherApp;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeasurementService {
    private static final String TAG = "MeasurementService";
    private static final String DB_NAME = "db";
    private static MeasurementService instance;

    private InfluxDB db;

    private class Callback implements InfluxDBCallback {
        private ServiceCallback<List<Measurement>> callback;
        private Callback(ServiceCallback<List<Measurement>> callback) {
            this.callback = callback;
        }
        @Override
        public void onResponse(InfluxDBQueryResult result) {
            try {
                if (result == null || result.seriesCount() == 0) {
                    callback.onSuccess(new ArrayList<Measurement>());
                } else {
                    callback.onSuccess(result.getSeries(0).toObject(Measurement.class));
                }
            } catch (Exception e) {
                callback.onError(e);
            }
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onError(t);
        }
    }

    public static MeasurementService getInstance() {
        if (instance == null) {
            instance = new MeasurementService();
        }
        return instance;
    }

    public MeasurementService() {
        Settings settings = new Settings();
        if (!settings.getDbUrl().isEmpty()) {
            db = InfluxDB.Builder()
                    .enableAuthentication(settings.getDbUsername(), settings.getDbPassword())
                    .enableHTTPCache(WeatherApp.getAppContext(), (long) 5 * 1024 * 1024, 600)
                    .connect(DB_NAME, settings.getDbUrl());
        }
    }

    public void clearCache() {
        if (db != null) {
            db.clearCache();
        }
    }

    public void getMeasurementsToday(final ServiceCallback<List<Measurement>> callback) {
        Log.v(TAG, "getMeasurementsToday");
        Pair<Date, Date> range = DateUtil.getStartEndOfCurrentDay();
        getMeasurements(range.first, range.second, callback);
    }

    public void getMeasurements(Date from, Date to, final ServiceCallback<List<Measurement>> callback) {
        Log.v(TAG, "getMeasurements with params " + from.toString() +", " + to.toString());
        if (db != null){
            InfluxDBQuery query = new InfluxDBQuery("SELECT * from weather where time > @from and time < @to")
                    .addParameter("@from", from)
                    .addParameter("@to", to);
            db.query(query, new Callback(callback));
        } else {
            callback.onError(new Exception("database connection not initialized"));
        }
    }

}
