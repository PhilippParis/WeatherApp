package com.philipp.paris.weatherapp.service;


import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.Constants;
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.web.weatherunderground.WUService;
import com.philipp.paris.weatherapp.web.weatherunderground.conversion.WUConverterFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastService {
    private class CacheEntry<T> {
        private Date time;
        private T data;

        public CacheEntry(Date time, T data) {
            this.time = time;
            this.data = data;
        }
    }

    private static final String BASE_URL = "http://api.wunderground.com";
    private WUService service;

    private static Map<String, CacheEntry<Measurement>> measurementCache = new ConcurrentHashMap<>();
    private static Map<String, CacheEntry<List<ForecastHour>>> hourlyForecastCache = new ConcurrentHashMap<>();

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
        if (loadFromCache(location, measurementCache, callback)) {
            return;
        }

        service.conditions(Constants.WU_API_KEY, location).enqueue(new Callback<Measurement>() {
            @Override
            public void onResponse(Call<Measurement> call, Response<Measurement> response) {
                Measurement m = response.body();
                if (m != null) {
                    writeToCache(location, measurementCache, m);
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

    public void getHourlyForecast(double latitude, double longitude, final ServiceCallback<List<ForecastHour>> callback) {
        final String location = latitude + "," + longitude;
        if (loadFromCache(location, hourlyForecastCache, callback)) {
            return;
        }

        service.hourly(Constants.WU_API_KEY, location).enqueue(new Callback<ForecastHour.ForecastHourList>() {
            @Override
            public void onResponse(Call<ForecastHour.ForecastHourList> call, Response<ForecastHour.ForecastHourList> response) {
                List<ForecastHour> forecastHours = response.body().hours;
                writeToCache(location, hourlyForecastCache, forecastHours);
                callback.onSuccess(forecastHours);
            }

            @Override
            public void onFailure(Call<ForecastHour.ForecastHourList> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    private <T> void writeToCache(String key, Map<String, CacheEntry<T>> cache, T data) {
        cache.put(key, new CacheEntry<>(DateUtil.getCurrentTime(), data));
    }

    private <T> boolean loadFromCache(String key, Map<String, CacheEntry<T>> cache, ServiceCallback<T> callback) {
        if (cache.containsKey(key)) {
            CacheEntry<T> entry = cache.get(key);
            if (DateUtil.diff(entry.time, DateUtil.getCurrentTime(), DateUtil.MINUTE) < 10) {
                callback.onSuccess(entry.data);
                return true;
            } else {
                cache.remove(key);
            }
        }
        return false;
    }
}
