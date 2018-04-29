package com.philipp.paris.weatherapp.web.weatherunderground.results;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.philipp.paris.weatherapp.domain.ForecastDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastDailyResult {
    public List<ForecastDay> days = new ArrayList<>();

    public static ForecastDailyResult parse(JsonObject json) {
        ForecastDailyResult result = new ForecastDailyResult();
        JsonArray simpleforecast = json.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday");
        for (int i = 0; i < simpleforecast.size(); i ++) {
            result.days.add(parseForecastDay(simpleforecast.get(i).getAsJsonObject()));
        }

        JsonArray txt_forecast = json.getAsJsonObject("forecast").getAsJsonObject("txt_forecast").getAsJsonArray("forecastday");
        for (int i = 0; i < txt_forecast.size(); i += 2) {
            result.days.get(i / 2).setText(txt_forecast.get(i).getAsJsonObject().get("fcttext_metric").getAsString());
        }
        return result;
    }

    private static ForecastDay parseForecastDay(JsonObject json) {
        ForecastDay data = new ForecastDay();
        data.setTime(new Date(json.getAsJsonObject("date").get("epoch").getAsLong() * 1000));
        data.setTemperatureMin(json.getAsJsonObject("low").get("celsius").getAsFloat());
        data.setTemperature(json.getAsJsonObject("high").get("celsius").getAsFloat());
        data.setIconKey(json.get("icon").getAsString());
        data.setCondition(json.get("conditions").getAsString());
        data.setQpfAllDay(json.getAsJsonObject("qpf_allday").get("mm").getAsFloat());
        data.setSnowAllDay(json.getAsJsonObject("snow_allday").get("cm").getAsFloat());
        data.setWindMax(json.getAsJsonObject("maxwind").get("kph").getAsFloat());
        return data;
    }
}
