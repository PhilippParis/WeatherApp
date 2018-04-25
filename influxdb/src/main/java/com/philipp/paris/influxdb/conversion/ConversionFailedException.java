package com.philipp.paris.influxdb.conversion;


public class ConversionFailedException extends Exception {
    public ConversionFailedException(Throwable cause) {
        super(cause);
    }

    public ConversionFailedException(String message) {
        super(message);
    }
}
