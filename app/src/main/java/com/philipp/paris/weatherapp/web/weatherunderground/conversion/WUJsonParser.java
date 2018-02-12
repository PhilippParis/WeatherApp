package com.philipp.paris.weatherapp.web.weatherunderground.conversion;


import com.google.gson.JsonObject;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Measurement;

import org.json.JSONObject;

import java.util.Date;


public class WUJsonParser {

    public static ForecastHour parseForecastHour(JsonObject json) {
        ForecastHour data = new ForecastHour();
        data.setTime(new Date(json.getAsJsonObject("FCTTIME").get("epoch").getAsLong() * 1000));
        data.setTemperature(json.getAsJsonObject("temp").get("metric").getAsFloat());
        data.setIconUrl(json.get("icon_url").getAsString());
        data.setQpf(json.getAsJsonObject("qpf").get("metric").getAsFloat());
        data.setWspd(json.getAsJsonObject("wspd").get("metric").getAsInt());
        return data;
    }

    public static Measurement parseObservation(JsonObject json) {
        Measurement measurement = new Measurement();
        measurement.setTime(new Date(json.get("local_epoch").getAsLong() * 1000));
        measurement.setTemperature(json.get("temp_c").getAsFloat());
        measurement.setHumidity(Float.parseFloat(json.get("relative_humidity").getAsString().replace('%',' ')) / 100f);
        measurement.setWind(json.get("wind_kph").getAsFloat());
        measurement.setPressure(json.get("pressure_mb").getAsFloat());
        measurement.setPrecipitation(json.get("precip_1hr_metric").getAsFloat());
        return measurement;
    }
}
