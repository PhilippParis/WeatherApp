package com.philipp.paris.weatherapp.web.influxdb;

import java.util.HashMap;
import java.util.Map;

public class InfluxDBQuery {
    private String query;
    private Map<String, String> parameters = new HashMap<>();

    public InfluxDBQuery(String query) {
        this.query = query;
    }

    public void addParameter(String key, String param) {
        parameters.put(key, param);
    }

    public String create(InfluxDB db) {
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            query = query.replace(e.getKey(), e.getValue());
        }
        return query;
    }

}
