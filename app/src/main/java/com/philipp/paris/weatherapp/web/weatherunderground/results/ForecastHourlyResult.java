package com.philipp.paris.weatherapp.web.weatherunderground.results;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.philipp.paris.weatherapp.domain.ForecastHour;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastHourlyResult {
    private static final String TAG = "ForecastDailyResult";
    public List<ForecastHour> hours = new ArrayList<>();

    public static ForecastHourlyResult parse(JsonObject json) {
        ForecastHourlyResult result = new ForecastHourlyResult();
        for (JsonElement obj : json.getAsJsonArray("hourly_forecast")) {
            result.hours.add(parseForecastHour(obj.getAsJsonObject()));
        }
        return result;
    }

    private static ForecastHour parseForecastHour(JsonObject json) {
        ForecastHour data = new ForecastHour();
        try {
            data.setTime(new Date(json.getAsJsonObject("FCTTIME").get("epoch").getAsLong() * 1000));
            data.setTemperature(json.getAsJsonObject("temp").get("metric").getAsFloat());
            data.setIconKey(json.get("icon").getAsString());
            data.setQpf(json.getAsJsonObject("qpf").get("metric").getAsFloat());
            data.setSnow(json.getAsJsonObject("snow").get("metric").getAsFloat());
            data.setWspd(json.getAsJsonObject("wspd").get("metric").getAsInt());
            data.setWindDirection(json.getAsJsonObject("wdir").get("degrees").getAsInt());
            data.setPop(json.get("pop").getAsFloat() / 100f);
        } catch (Exception e) {
            Log.e(TAG,"failed to parse forecast hour", e);
        }
        return data;
    }
}
