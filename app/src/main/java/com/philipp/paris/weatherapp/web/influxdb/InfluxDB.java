package com.philipp.paris.weatherapp.web.influxdb;

import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBConverterFactory;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InfluxDB {
    private String dbName;
    private InfluxDBService service;

    InfluxDB(String dbName, String url, String user, String password) {
        this.dbName = dbName;

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(Credentials.basic(user, password)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addConverterFactory(new InfluxDBConverterFactory())
                .build();

        service = retrofit.create(InfluxDBService.class);
    }

    public void query(InfluxDBQuery query, Callback<InfluxDBQueryResult> callback) {
        service.query(dbName, query.create(this)).enqueue(callback);
    }
}
