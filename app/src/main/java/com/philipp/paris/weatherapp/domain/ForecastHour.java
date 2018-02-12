package com.philipp.paris.weatherapp.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastHour {
    public static class ForecastHourList {
        public List<ForecastHour> hours = new ArrayList<>();
    }

    private Date time;
    private Float temperature;
    private String iconUrl;
    private Float qpf;            // quantitative precipitation forecast
    private int wspd;           // wind speed

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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
}
