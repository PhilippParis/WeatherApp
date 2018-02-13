package com.philipp.paris.weatherapp.web.weatherunderground;

import com.philipp.paris.weatherapp.web.weatherunderground.results.CurrentConditionsResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastHourlyResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastDailyResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WUService {
    @Headers("Cache-Control: max-age=600")
    @GET("/api/{api_key}/conditions/lang:{language}/q/{location}.json")
    Call<CurrentConditionsResult> currentConditions(@Path("api_key") String apiKey, @Path("location") String location, @Path("language") String language);

    @Headers("Cache-Control: max-age=1800")
    @GET("/api/{api_key}/hourly/lang:{language}/q/{location}.json")
    Call<ForecastHourlyResult> forecastDayHourly(@Path("api_key") String apiKey, @Path("location") String location, @Path("language") String language);

    @Headers("Cache-Control: max-age=3600")
    @GET("/api/{api_key}/forecast10day/lang:{language}/q/{location}.json")
    Call<ForecastDailyResult> forecast10Day(@Path("api_key") String apiKey, @Path("location") String location, @Path("language") String language);

    @Headers("Cache-Control: max-age=3600")
    @GET("/api/{api_key}/hourly10day/lang:{language}/q/{location}.json")
    Call<ForecastHourlyResult> forecast10DayHourly(@Path("api_key") String apiKey, @Path("location") String location, @Path("language") String language);
}
