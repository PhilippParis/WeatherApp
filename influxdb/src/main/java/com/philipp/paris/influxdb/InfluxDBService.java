package com.philipp.paris.influxdb;


import com.philipp.paris.influxdb.query.InfluxDBQueryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface InfluxDBService {

    @Headers("Cache-Control: max-age=600")
    @GET("/query")
    Call<InfluxDBQueryResult> query(@Query("db") String db, @Query("q") String query);
}
