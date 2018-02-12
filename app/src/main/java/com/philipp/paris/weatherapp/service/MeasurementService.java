package com.philipp.paris.weatherapp.service;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDB;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBFactory;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQuery;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQueryResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeasurementService {
    private static final String TAG = "MeasurementService";
    private static final String DB_NAME = "db";
    private Context context;

    private class InfluxDBCallback implements com.philipp.paris.weatherapp.web.influxdb.InfluxDBCallback {
        private ServiceCallback<List<Measurement>> callback;
        private InfluxDBCallback(ServiceCallback<List<Measurement>> callback) {
            this.callback = callback;
        }
        @Override
        public void onResponse(InfluxDBQueryResult result) {
            try {
                if (result == null || result.seriesCount() == 0) {
                    callback.onSuccess(new ArrayList<Measurement>());
                }
                callback.onSuccess(result.getSeries(0).toObject(Measurement.class));
            } catch (Exception e) {
                callback.onError(e);
            }
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onError(t);
        }
    }

    public MeasurementService(Context context)  {
        this.context = context;
    }

    public void getMeasurementsToday(final ServiceCallback<List<Measurement>> callback) {
        Log.v(TAG, "entering getMeasurementsToday");
        Pair<Date, Date> range = DateUtil.getStartEndOfCurrentDay();
        getMeasurements(range.first, range.second, callback);
    }

    public void getMeasurements(Date from, Date to, final ServiceCallback<List<Measurement>> callback) {
        Log.v(TAG, "entering getMeasurements with params " + from.toString() +", " + to.toString());
        InfluxDB db = getDB(callback);
        if (db != null){
            InfluxDBQuery query = new InfluxDBQuery("SELECT * from weather where time > @from and time < @to")
                    .addParameter("@from", from)
                    .addParameter("@to", to);
            db.query(query, new InfluxDBCallback(callback));
        }
    }

    public void getMeasurements(final ServiceCallback<List<Measurement>> callback) {
        Log.v(TAG, "entering getMeasurements");
        InfluxDB db = getDB(callback);
        if (db != null){
            InfluxDBQuery query = new InfluxDBQuery("SELECT * from weather");
            db.query(query, new InfluxDBCallback(callback));
        }
    }

    private InfluxDB getDB(ServiceCallback<List<Measurement>> callback) {
        Settings settings = new Settings(context);
        try {
            return InfluxDBFactory.getInstance(DB_NAME, settings.getDbUrl(), settings.getDbUsername(), settings.getDbPassword());
        } catch (IllegalArgumentException e) {
            callback.onError(e);
            return null;
        }
    }

}
