package com.philipp.paris.weatherapp.web.influxdb;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfluxDBQuery {
    private String query;
    private Map<String, String> parameters = new HashMap<>();

    public InfluxDBQuery(String query) {
        this.query = query;
    }

    public InfluxDBQuery addParameter(String key, Date date) {
        parameters.put(key, "'" + ISO8601Utils.format(date) + "'");
        return this;
    }

    public InfluxDBQuery addParameter(String key, String param) {
        parameters.put(key, param);
        return this;
    }

    public String create(InfluxDB db) {
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            query = query.replace(e.getKey(), e.getValue());
        }
        return query;
    }

}
