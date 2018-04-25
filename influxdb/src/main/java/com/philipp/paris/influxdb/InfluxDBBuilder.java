package com.philipp.paris.influxdb;


import android.content.Context;

import com.philipp.paris.influxdb.interceptors.AuthenticationInterceptor;
import com.philipp.paris.influxdb.interceptors.OfflineRequestCacheInterceptor;
import com.philipp.paris.influxdb.interceptors.ResponseCacheInterceptor;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

public class InfluxDBBuilder {
    private static final String CACHE_NAME = "INFLUXDB_HTTP";
    private OkHttpClient.Builder httpBuilder;

    InfluxDBBuilder() {
        this.httpBuilder = new OkHttpClient.Builder();
    }

    InfluxDBBuilder(OkHttpClient.Builder httpBuilder) {
        this.httpBuilder = httpBuilder;
    }

    /**
     * enables authentication
     * @param username influxdb username
     * @param password influxdb password
     * @return builder
     */
    public InfluxDBBuilder enableAuthentication(String username, String password) {
        httpBuilder.addInterceptor(new AuthenticationInterceptor(Credentials.basic(username, password)));
        return this;
    }

    /**
     * enable HTTP Cache for caching responses
     * @param context application context
     * @param maxSize maximum cache size in bytes
     * @param maxAge maximum age of cached requests
     * @return builder
     */
    public InfluxDBBuilder enableHTTPCache(Context context, long maxSize, long maxAge) {
        Cache cache = new okhttp3.Cache(new File(context.getCacheDir(), CACHE_NAME), maxSize);
        httpBuilder
                .cache(cache)
                .addNetworkInterceptor(new ResponseCacheInterceptor(maxAge))
                .addInterceptor(new OfflineRequestCacheInterceptor(context));
        return this;
    }

    /**
     * connect to a influxdb database
     * @param dbName database name
     * @param url database url
     * @return builder instance
     */
    public InfluxDB connect(String dbName, String url) {
        return new InfluxDB(dbName, url, httpBuilder.build());
    }
}
