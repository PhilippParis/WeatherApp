package com.philipp.paris.weatherapp.domain;


import java.io.Serializable;
import java.util.Date;

public class ForecastHour implements Serializable {
    private Date time;
    private Float temperature;
    private String iconKey;
    private Float snow;             // quantitative snow forecast
    private Float qpf;              // quantitative precipitation forecast
    private Float pop;              // precipitation probability
    private int wspd;               // wind speed
    private int windDirection;      // wind direction in degree

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

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public Float getQpf() {
        return qpf;
    }

    public void setQpf(Float qpf) {
        this.qpf = qpf;
    }

    public int getWspd() {
        return wspd;
    }

    public void setWspd(int wspd) {
        this.wspd = wspd;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public Float getPop() {
        return pop;
    }

    public void setPop(Float pop) {
        this.pop = pop;
    }

    public Float getSnow() {
        return snow;
    }

    public void setSnow(Float snow) {
        this.snow = snow;
    }
}
