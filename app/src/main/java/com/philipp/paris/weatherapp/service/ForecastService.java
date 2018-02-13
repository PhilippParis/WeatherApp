package com.philipp.paris.weatherapp.service;


import android.util.Log;

import com.philipp.paris.weatherapp.WeatherApp;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.service.caching.OfflineRequestCacheInterceptor;
import com.philipp.paris.weatherapp.service.caching.ResponseCacheInterceptor;
import com.philipp.paris.weatherapp.util.Constants;
import com.philipp.paris.weatherapp.web.weatherunderground.WULanguageUtil;
import com.philipp.paris.weatherapp.web.weatherunderground.WUService;
import com.philipp.paris.weatherapp.web.weatherunderground.conversion.WUConverterFactory;
import com.philipp.paris.weatherapp.web.weatherunderground.results.CurrentConditionsResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastDailyResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastHourlyResult;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastService {
    private static final String TAG = "ForecastService";
    private static final String BASE_URL = "http://api.wunderground.com";
    private static ForecastService instance;
    private WUService service;
    private Cache cache;
    private double latitude;
    private double longitude;

    public static ForecastService getInstance() {
        if (instance == null) {
            instance = new ForecastService();
        }
        return instance;
    }

    private ForecastService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        cache = new okhttp3.Cache(new File(WeatherApp.getAppContext().getCacheDir(),
                "HTTP"), (long) 5 * 1024 * 1024);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineRequestCacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(new WUConverterFactory())
                .build();

        service = retrofit.create(WUService.class);
    }

    public boolean currentLocationSet() {
        return this.longitude != 0.0 && this.latitude != 0.0;
    }

    public void deleteCurrentLocation() {
        synchronized (this) {
            this.latitude = 0.0;
            this.longitude = 0.0;
        }
    }

    public void setCurrentLocation(double latitude, double longitude) {
        synchronized (this) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public void clearCache() {
        try {
            Iterator<String> it = cache.urls();
            while (it.hasNext()) {
                if (it.next().startsWith(BASE_URL)) {
                    it.remove();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "clearing cache failed");
        }
    }

    public void getCurrentConditions(final ServiceCallback<Measurement> callback) {
        final String location = latitude + "," + longitude;
        service.currentConditions(Constants.WU_API_KEY, location,
                WULanguageUtil.getLanguage(Locale.getDefault())).enqueue(new Callback<CurrentConditionsResult>() {
            @Override
            public void onResponse(Call<CurrentConditionsResult> call, Response<CurrentConditionsResult> response) {
                if (response.body() != null) {
                    callback.onSuccess(response.body().measurement);
                } else {
                    callback.onError(new Exception("no internet connection"));
                }
            }
            @Override
            public void onFailure(Call<CurrentConditionsResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecastDayHourly(final ServiceCallback<List<ForecastHour>> callback) {
        final String location = latitude + "," + longitude;
        service.forecastDayHourly(Constants.WU_API_KEY, location,
                WULanguageUtil.getLanguage(Locale.getDefault())).enqueue(new Callback<ForecastHourlyResult>() {
            @Override
            public void onResponse(Call<ForecastHourlyResult> call, Response<ForecastHourlyResult> response) {
                if (response.body() != null) {
                    callback.onSuccess(response.body().hours);
                } else {
                    callback.onError(new Exception("no internet connection"));
                }
            }

            @Override
            public void onFailure(Call<ForecastHourlyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecast10Day(final ServiceCallback<List<ForecastDay>> callback) {
        final String location = latitude + "," + longitude;
        service.forecast10Day(Constants.WU_API_KEY, location,
                WULanguageUtil.getLanguage(Locale.getDefault())).enqueue(new Callback<ForecastDailyResult>() {
            @Override
            public void onResponse(Call<ForecastDailyResult> call, Response<ForecastDailyResult> response) {
                if (response.body() != null) {
                    callback.onSuccess(response.body().days);
                } else {
                    callback.onError(new Exception("no internet connection"));
                }
            }

            @Override
            public void onFailure(Call<ForecastDailyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecast10DayHourly(final ServiceCallback<List<ForecastHour>> callback) {
        final String location = latitude + "," + longitude;
        service.forecast10DayHourly(Constants.WU_API_KEY, location,
                WULanguageUtil.getLanguage(Locale.getDefault())).enqueue(new Callback<ForecastHourlyResult>() {
            @Override
            public void onResponse(Call<ForecastHourlyResult> call, Response<ForecastHourlyResult> response) {
                if (response.body() != null) {
                    callback.onSuccess(response.body().hours);
                } else {
                    callback.onError(new Exception("no internet connection"));
                }
            }

            @Override
            public void onFailure(Call<ForecastHourlyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}
