package com.philipp.paris.weatherapp.web.influxdb;

import android.util.Log;

import com.philipp.paris.weatherapp.WeatherApp;
import com.philipp.paris.weatherapp.service.caching.OfflineRequestCacheInterceptor;
import com.philipp.paris.weatherapp.service.caching.ResponseCacheInterceptor;
import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import okhttp3.Cache;
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
    private Cache cache;
    private String baseUrl;

    InfluxDB(String dbName, String url, String user, String password) {
        this.dbName = dbName;
        this.baseUrl = url;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        cache = new okhttp3.Cache(new File(WeatherApp.getAppContext().getCacheDir(),
                "HTTP"), (long) 5 * 1024 * 1024);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineRequestCacheInterceptor())
                .addInterceptor(new AuthenticationInterceptor(Credentials.basic(user, password)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
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

    public void clearCache() {
        try {
            Iterator<String> it = cache.urls();
            while (it.hasNext()) {
                if (it.next().startsWith(baseUrl)) {
                    it.remove();
                }
            }
        } catch (IOException e) {
            Log.e("InfluxDB", "clearing cache failed");
        }
    }

}
