package com.philipp.paris.weatherapp.web.influxdb;

import android.util.Log;

import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBConverterFactory;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InfluxDB {
    private static final String TAG = "InfluxDB";
    private String dbName;
    private InfluxDBService service;

    InfluxDB(String dbName, String url, String user, String password) {
        this.dbName = dbName;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(Credentials.basic(user, password)))
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addConverterFactory(new InfluxDBConverterFactory())
                .build();

        service = retrofit.create(InfluxDBService.class);
    }

    public void query(InfluxDBQuery query, Callback<InfluxDBQueryResult> callback) {
        String createdQuery = query.create(this);
        Log.v(TAG, "execute query: '" + createdQuery+ "'");
        service.query(dbName, createdQuery).enqueue(callback);
    }
}
