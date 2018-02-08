package com.philipp.paris.weatherapp.domain;


import java.util.Date;

public class Weather {
    private Date time;
    private Float temperature;
    private Float humidity;
    private Float pressure;

    public Date getTime() {
        return time;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public Float getPressure() {
        return pressure;
    }
}
