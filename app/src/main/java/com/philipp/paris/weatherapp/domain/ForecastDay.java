package com.philipp.paris.weatherapp.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastDay implements Serializable {
    private Date time;
    private Float temperature;
    private Float temperatureMin;
    private String condition;
    private String iconKey;
    private String text;
    private Float qpfAllDay;
    private Float snowAllDay;
    private Float windMax;

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

    public Float getQpfAllDay() {
        return qpfAllDay;
    }

    public void setQpfAllDay(Float qpfAllDay) {
        this.qpfAllDay = qpfAllDay;
    }

    public Float getSnowAllDay() {
        return snowAllDay;
    }

    public void setSnowAllDay(Float snowAllDay) {
        this.snowAllDay = snowAllDay;
    }

    public Float getWindMax() {
        return windMax;
    }

    public void setWindMax(Float windMax) {
        this.windMax = windMax;
    }
}
