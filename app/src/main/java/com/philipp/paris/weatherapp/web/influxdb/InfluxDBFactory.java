package com.philipp.paris.weatherapp.web.influxdb;


import java.net.MalformedURLException;
import java.util.HashMap;

public class InfluxDBFactory {
    static public InfluxDB getInstance(String dbName, String url, String user, String password) throws IllegalArgumentException {
        return new InfluxDB(dbName, url, user, password);
    }
}
