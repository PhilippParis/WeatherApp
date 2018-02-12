package com.philipp.paris.weatherapp.web.weatherunderground.conversion;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class ForecastHourListConverter implements Converter<ResponseBody, ForecastHour.ForecastHourList> {
    private static final String JSON_KEY_HOURLY_FORECAST = "hourly_forecast";

    @Override
    public ForecastHour.ForecastHourList convert(ResponseBody value) throws IOException {
        JsonObject jsonObject = new JsonParser().parse(value.string()).getAsJsonObject();
        ForecastHour.ForecastHourList data = new ForecastHour.ForecastHourList();
        for (JsonElement obj : jsonObject.getAsJsonArray(JSON_KEY_HOURLY_FORECAST)) {
            data.hours.add(WUJsonParser.parseForecastHour(obj.getAsJsonObject()));
        }
        return data;
    }
}
