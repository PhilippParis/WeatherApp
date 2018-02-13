package com.philipp.paris.weatherapp.service.caching;


import com.philipp.paris.weatherapp.util.UtilityMethods;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

public class OfflineRequestCacheInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!UtilityMethods.isNetworkAvailable()) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached")
                    .build();
        }
        return chain.proceed(request);
    }
}
