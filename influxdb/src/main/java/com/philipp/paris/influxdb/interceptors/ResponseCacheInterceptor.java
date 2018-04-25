package com.philipp.paris.influxdb.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


public class ResponseCacheInterceptor implements Interceptor {
    private long maxAge;

    public ResponseCacheInterceptor(long maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            return originalResponse;
        }
    }
}
