package com.philipp.paris.influxdb;

import android.util.Log;

import com.philipp.paris.influxdb.query.InfluxDBQuery;
import com.philipp.paris.influxdb.query.InfluxDBQueryResult;
import com.philipp.paris.influxdb.conversion.InfluxDBConverterFactory;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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

    /**
     * @return return influxdb builder instance
     */
    public static InfluxDBBuilder Builder() {
        return new InfluxDBBuilder();
    }

    /**
     * @return return influxdb builder instance with custom httpClient
     */
    public static InfluxDBBuilder Builder(OkHttpClient.Builder httpClient) {
        return new InfluxDBBuilder(httpClient);
    }

    InfluxDB(String dbName, String url, OkHttpClient httpClient) {
        this.dbName = dbName;
        this.baseUrl = url;
        this.cache = httpClient.cache();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(new InfluxDBConverterFactory())
                .build();

        service = retrofit.create(InfluxDBService.class);
    }

    /**
     * query the influxdb instance asynchronously
     * @param query query
     * @param callback result callback
     */
    public void query(InfluxDBQuery query, final InfluxDBCallback callback) {
        String createdQuery = query.create();
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

    /**
     * clears the http cache if a cache is used
     */
    public void clearCache() {
        if (cache != null) {
            return;
        }

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
