package com.philipp.paris.weatherapp.web.influxdb;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InfluxDBService {

    @GET("/query")
    Call<InfluxDBQueryResult> query(@Query("db") String db, @Query("q") String query);
}
