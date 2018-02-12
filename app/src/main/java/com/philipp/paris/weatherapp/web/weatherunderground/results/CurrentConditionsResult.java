package com.philipp.paris.weatherapp.web.weatherunderground.results;


import com.google.gson.JsonObject;
import com.philipp.paris.weatherapp.domain.Measurement;

import java.util.Date;

public class CurrentConditionsResult {
    public Measurement measurement;

    public static CurrentConditionsResult parse(JsonObject json) {
        Measurement measurement = new Measurement();
        json = json.getAsJsonObject("current_observation");
        measurement.setTime(new Date(json.get("local_epoch").getAsLong() * 1000));
        measurement.setTemperature(json.get("temp_c").getAsFloat());
        measurement.setHumidity(Float.parseFloat(json.get("relative_humidity").getAsString().replace('%',' ')) / 100f);
        measurement.setWind(json.get("wind_kph").getAsFloat());
        measurement.setPressure(json.get("pressure_mb").getAsFloat());
        measurement.setPrecipitation(json.get("precip_1hr_metric").getAsFloat());
        return new CurrentConditionsResult(measurement);
    }

    public CurrentConditionsResult(Measurement measurement) {
        this.measurement = measurement;
    }
}
