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
    private static final String JSON_KEY_OBSERVATION = "current_observation";

    @Override
    public Measurement convert(ResponseBody value) throws IOException {
        try {
            JsonObject jsonObject = new JsonParser().parse(value.string()).getAsJsonObject();
            return WUJsonParser.parseObservation(jsonObject.getAsJsonObject(JSON_KEY_OBSERVATION));
        } catch (Exception e) {
            throw new IOException("HTTP Response conversion failed: '" + e.getMessage() + "'", e);
        }
    }
}
