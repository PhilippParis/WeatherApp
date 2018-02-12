package com.philipp.paris.weatherapp.web.weatherunderground;

import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Measurement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface WUService {

    @GET("/api/{api_key}/conditions/q/{location}.json")
    Call<Measurement> conditions(@Path("api_key") String apiKey, @Path("location") String location);

    @GET("/api/{api_key}/hourly/q/{location}.json")
    Call<ForecastHour.ForecastHourList> hourly(@Path("api_key") String apiKey, @Path("location") String location);
}
