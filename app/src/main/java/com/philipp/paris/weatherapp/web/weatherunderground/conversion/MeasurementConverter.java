package com.philipp.paris.weatherapp.web.weatherunderground.conversion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philipp.paris.weatherapp.domain.Measurement;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class MeasurementConverter implements Converter<ResponseBody, Measurement> {
    private static final String RFC822_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
    private static final String JSON_KEY_OBSERVATION = "current_observation";
    private static final String JSON_KEY_TIME = "local_time_rfc822";
    private static final String JSON_KEY_TEMPERATURE = "temp_c";
    private static final String JSON_KEY_HUMIDITY = "relative_humidity";
    private static final String JSON_KEY_WIND = "wind_kph";
    private static final String JSON_KEY_PRESSURE = "pressure_mb";
    private static final String JSON_KEY_PRECIP = "precip_1hr_metric";

    private static final DateFormat formatter = new SimpleDateFormat(RFC822_DATE_FORMAT);

    @Override
    public Measurement convert(ResponseBody value) throws IOException {
        try {
            JsonObject jsonObject = new JsonParser().parse(value.string()).getAsJsonObject();
            jsonObject = jsonObject.getAsJsonObject(JSON_KEY_OBSERVATION);
            Measurement measurement = new Measurement();
            measurement.setTime(formatter.parse(jsonObject.get(JSON_KEY_TIME).getAsString()));
            measurement.setTemperature(jsonObject.get(JSON_KEY_TEMPERATURE).getAsFloat());
            measurement.setHumidity(jsonObject.get(JSON_KEY_HUMIDITY).getAsFloat());
            measurement.setWind(jsonObject.get(JSON_KEY_WIND).getAsFloat());
            measurement.setPressure(jsonObject.get(JSON_KEY_PRESSURE).getAsFloat());
            measurement.setPrecipitation(jsonObject.get(JSON_KEY_PRECIP).getAsFloat());
            return measurement;
        } catch (Exception e) {
            throw new IOException("HTTP Response conversion failed: '" + e.getMessage() + "'", e);
        }
    }
}
