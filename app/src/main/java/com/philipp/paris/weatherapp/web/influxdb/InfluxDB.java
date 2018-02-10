package com.philipp.paris.weatherapp.web.influxdb;

import android.util.Log;
import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBConverterFactory;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public void query(InfluxDBQuery query, final InfluxDBCallback callback) {
        String createdQuery = query.create(this);
        Log.v(TAG, "execute query: '" + createdQuery+ "'");
        service.query(dbName, createdQuery).enqueue(new Callback<InfluxDBQueryResult>() {
            @Override
            public void onResponse(Call<InfluxDBQueryResult> call, Response<InfluxDBQueryResult> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<InfluxDBQueryResult> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

}
