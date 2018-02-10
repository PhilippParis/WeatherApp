package com.philipp.paris.weatherapp.web.influxdb;

public interface InfluxDBCallback {
    void onResponse(InfluxDBQueryResult result);
    void onFailure(Throwable t);
}