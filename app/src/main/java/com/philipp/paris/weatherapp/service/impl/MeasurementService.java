package com.philipp.paris.weatherapp.service.impl;

import android.content.Context;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.IMeasurementService;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDB;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBFactory;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQuery;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQueryResult;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementService implements IMeasurementService {
    private static final String DB_NAME = "db";
    private Context context;

    private class InfluxDBCallback implements Callback<InfluxDBQueryResult> {
        private ServiceCallback<Weather> callback;
        private InfluxDBCallback(ServiceCallback<Weather> callback) {
            this.callback = callback;
        }
        @Override
        public void onResponse(Call<InfluxDBQueryResult> call, Response<InfluxDBQueryResult> response) {
            try {
                callback.onResponse(response.body().getSeries(0).toObject(Weather.class));
            } catch (Exception e) {
                callback.onError();
            }
        }
        @Override
        public void onFailure(Call<InfluxDBQueryResult> call, Throwable t) {
            callback.onError();
        }
    }

    public MeasurementService(Context context)  {
        this.context = context;
    }

    @Override
    public void getMeasurementsToday(final ServiceCallback<Weather> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date from = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date to = calendar.getTime();
        getMeasurements(from, to, callback);
    }

    @Override
    public void getMeasurements(Date from, Date to, final ServiceCallback<Weather> callback) {
        Settings settings = new Settings(context);
        InfluxDB db = InfluxDBFactory.getInstance(DB_NAME, settings.getDbUrl(), settings.getDbUsername(), settings.getDbPassword());
        InfluxDBQuery query = new InfluxDBQuery("SELECT * from weather where time >= @from and time <= @to")
                .addParameter("@from", ISO8601Utils.format(from))
                .addParameter("@to", ISO8601Utils.format(to));
        db.query(query, new InfluxDBCallback(callback));
    }

    @Override
    public void getMeasurements(final ServiceCallback<Weather> callback) {
        Settings settings = new Settings(context);
        InfluxDB db = InfluxDBFactory.getInstance(DB_NAME, settings.getDbUrl(), settings.getDbUsername(), settings.getDbPassword());
        InfluxDBQuery query = new InfluxDBQuery("SELECT * from weather");
        db.query(query, new InfluxDBCallback(callback));
    }

}
