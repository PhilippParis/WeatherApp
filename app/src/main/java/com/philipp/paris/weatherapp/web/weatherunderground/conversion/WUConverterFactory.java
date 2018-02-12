package com.philipp.paris.weatherapp.web.weatherunderground.conversion;


import com.google.gson.JsonParser;
import com.philipp.paris.weatherapp.web.weatherunderground.results.CurrentConditionsResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastHourlyResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastDailyResult;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class WUConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == CurrentConditionsResult.class) {
            return currentConditionsResultConverter();
        }
        if (type == ForecastHourlyResult.class) {
            return forecastHourlyResultConverter();
        }
        if (type == ForecastDailyResult.class) {
            return forecastDailyResultConverter();
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        return null;
    }

    private Converter<ResponseBody, CurrentConditionsResult> currentConditionsResultConverter() {
        return new Converter<ResponseBody, CurrentConditionsResult>() {
            @Override
            public CurrentConditionsResult convert(ResponseBody value) throws IOException {
                return CurrentConditionsResult.parse(new JsonParser().parse(value.string()).getAsJsonObject());
            }
        };
    }

    private Converter<ResponseBody, ForecastDailyResult> forecastDailyResultConverter() {
        return new Converter<ResponseBody, ForecastDailyResult>() {
            @Override
            public ForecastDailyResult convert(ResponseBody value) throws IOException {
                return ForecastDailyResult.parse(new JsonParser().parse(value.string()).getAsJsonObject());
            }
        };
    }

    private Converter<ResponseBody, ForecastHourlyResult> forecastHourlyResultConverter() {
        return new Converter<ResponseBody, ForecastHourlyResult>() {
            @Override
            public ForecastHourlyResult convert(ResponseBody value) throws IOException {
                return ForecastHourlyResult.parse(new JsonParser().parse(value.string()).getAsJsonObject());
            }
        };
    }
}
