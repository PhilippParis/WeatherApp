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
    private String iconUrl;
}
