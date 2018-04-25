package com.philipp.paris.influxdb.query;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.philipp.paris.influxdb.InfluxDB;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfluxDBQuery {
    private String query;
    private Map<String, String> parameters = new HashMap<>();

    public InfluxDBQuery(String query) {
        this.query = query;
    }

    /**
     * adds a date parameter
     * @param key parameter key in query string
     * @param date date parameter
     * @return query instance
     */
    public InfluxDBQuery addParameter(String key, Date date) {
        parameters.put(key, "'" + ISO8601Utils.format(date) + "'");
        return this;
    }

    /**
     * adds a parameter
     * @param key parameter key in query string
     * @param param parameter
     * @return query instance
     */
    public InfluxDBQuery addParameter(String key, String param) {
        parameters.put(key, param);
        return this;
    }

    /**
     * @return returns the query as string with parameters set
     */
    public String create() {
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            query = query.replace(e.getKey(), e.getValue());
        }
        return query;
    }

}
