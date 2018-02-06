package com.philipp.paris.weatherapp.service.impl;

import android.content.Context;

import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.IMeasurementService;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeasurementService implements IMeasurementService {

    private Context context;
    private InfluxDBResultMapper resultMapper;// = new InfluxDBResultMapper();

    public MeasurementService(Context context)  {
        this.context = context;
    }

    @Override
    public List<Weather> getMeasurementsToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date from = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date to = calendar.getTime();
        return getMeasurements(from, to);
    }

    @Override
    public List<Weather> getMeasurements(Date from, Date to) {
        return query("SELECT * from weather where time >= " + from.toString() + " and time <= " + to.toString());
    }

    @Override
    public List<Weather> getMeasurements() {
        return query("SELECT * from weather");
    }

    private List<Weather> query(String command) {
        Settings settings = new Settings(context);
        InfluxDB db = InfluxDBFactory.connect(settings.getDbUrl(), settings.getDbUsername(), settings.getDbPassword());
        try {
            QueryResult queryResult = db.query(new Query(command, "db"));
            return resultMapper.toPOJO(queryResult, Weather.class);
        } finally {
            db.close();
        }
    }

}
