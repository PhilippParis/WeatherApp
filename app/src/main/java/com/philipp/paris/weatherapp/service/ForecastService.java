package com.philipp.paris.weatherapp.service;


import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.Constants;
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.web.weatherunderground.WUService;
import com.philipp.paris.weatherapp.web.weatherunderground.conversion.WUConverterFactory;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastService {
    private static final String BASE_URL = "http://api.wunderground.com";
    private WUService service;

    private static Map<String, Measurement> measurementCache = new ConcurrentHashMap<>();

    public ForecastService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(new WUConverterFactory())
                .build();

        service = retrofit.create(WUService.class);
    }

    public void getCurrentConditions(double latitude, double longitude, final ServiceCallback<Measurement> callback) {
        final String location = latitude + "," + longitude;
        if (loadFromMeasurementCache(location, callback)) {
            return;
        }

        service.conditions(Constants.WU_API_KEY, location).enqueue(new Callback<Measurement>() {
            @Override
            public void onResponse(Call<Measurement> call, Response<Measurement> response) {
                Measurement m = response.body();
                if (m != null) {
                    measurementCache.put(location, m);
                    callback.onSuccess(m);
                } else {
                    callback.onError(null);
                }
            }
            @Override
            public void onFailure(Call<Measurement> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    private boolean loadFromMeasurementCache(String key, ServiceCallback<Measurement> callback) {
        if (measurementCache.containsKey(key)) {
            Measurement m = measurementCache.get(key);
            if (DateUtil.diff(m.getTime(), Calendar.getInstance().getTime(), DateUtil.MINUTE) < 10) {
                callback.onSuccess(m);
                return true;
            } else {
                measurementCache.remove(key);
            }
        }
        return false;
    }
}
