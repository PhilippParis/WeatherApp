package com.philipp.paris.weatherapp.domain;

import java.util.Date;

public class Measurement {
    private Date time;
    private Float temperature;  // temperature in °C
    private Float humidity;     // relative humidity in %
    private Float pressure;     // air pressure in mba
    private Float wind;         // wind in kph
    private Float precipitation;// precipitation over the last hour in mm

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getWind() {
        return wind;
    }

    public void setWind(Float wind) {
        this.wind = wind;
    }

    public Float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Float precipitation) {
        this.precipitation = precipitation;
    }
}
