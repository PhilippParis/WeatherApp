package com.philipp.paris.weatherapp.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastDay {
    public class ForecastDayList {
        public List<ForecastDay> days = new ArrayList<>();
    }

    private Date time;
    private Float temperature;
    private Float temperatureMin;
    private String condition;
    private String iconKey;
    private String text;

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

    public Float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
