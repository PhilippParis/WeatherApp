package com.philipp.paris.influxdb;

import com.philipp.paris.influxdb.query.InfluxDBQueryResult;

public interface InfluxDBCallback {
    void onResponse(InfluxDBQueryResult result);
    void onFailure(Throwable t);
}