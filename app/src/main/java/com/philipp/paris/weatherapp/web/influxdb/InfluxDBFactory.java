package com.philipp.paris.weatherapp.web.influxdb;


import java.util.HashMap;
import java.util.Map;

public class InfluxDBFactory {
    private static Map<String, InfluxDB> instances = new HashMap<>();

    public static InfluxDB get(String dbName, String url, String user, String password) {
        String key = dbName + "@" + url + ";" + user + ":" + password;
        if (instances.containsKey(key)) {
            return instances.get(key);
        }

        InfluxDB instance = new InfluxDB(dbName, url, user, password);
        instances.put(key, instance);
        return instance;
    }
}
