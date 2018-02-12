package com.philipp.paris.weatherapp.web.weatherunderground;

import com.philipp.paris.weatherapp.domain.Measurement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface WUService {

    @GET("/api/{api_key}/conditions/q/{location}")
    Call<Measurement> conditions(@Path("api_key") String apiKey, @Path("location") String location);
}
