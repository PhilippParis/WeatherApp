package com.philipp.paris.influxdb.util;


import com.philipp.paris.influxdb.InfluxDBCallback;
import com.philipp.paris.influxdb.query.InfluxDBQueryResult;

public class TestCallback implements InfluxDBCallback {
    private InfluxDBQueryResult result = null;
    private Throwable t = null;
    private boolean requestFailed = false;

    @Override
    public synchronized void onResponse(InfluxDBQueryResult result) {
        this.requestFailed = false;
        this.result = result;
        notifyAll();
    }

    @Override
    public synchronized void onFailure(Throwable t) {
        this.requestFailed = true;
        this.t = t;
        notifyAll();
    }

    public InfluxDBQueryResult getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return t;
    }

    public boolean requestFailed() {
        return requestFailed;
    }
}
