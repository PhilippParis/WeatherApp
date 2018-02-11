package com.philipp.paris.weatherapp.domain;


import java.util.Date;

public class Weather {
    private Date time;
    private Float temperature;
    private Float humidity;
    private Float pressure;

    public void setTime(Date time) {
        this.time = time;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

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
