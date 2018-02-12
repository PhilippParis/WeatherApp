package com.philipp.paris.weatherapp.service;


import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.Constants;
import com.philipp.paris.weatherapp.web.weatherunderground.WUService;
import com.philipp.paris.weatherapp.web.weatherunderground.conversion.WUConverterFactory;
import com.philipp.paris.weatherapp.web.weatherunderground.results.CurrentConditionsResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastDailyResult;
import com.philipp.paris.weatherapp.web.weatherunderground.results.ForecastHourlyResult;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastService {
    private static final String BASE_URL = "http://api.wunderground.com";

    private static Cache<Measurement> currentConditionsRequestCache = new Cache<>();
    private static Cache<List<ForecastHour>> forecastDayHourlyRequestCache = new Cache<>();
    private static Cache<List<ForecastDay>> forecast10DayRequestCache = new Cache<>();
    private static Cache<List<ForecastHour>> forecast10DayHourlyRequestCache = new Cache<>();

    private WUService service;

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
        if (currentConditionsRequestCache.read(location, callback)) {
            return;
        }

        service.currentConditions(Constants.WU_API_KEY, location).enqueue(new Callback<CurrentConditionsResult>() {
            @Override
            public void onResponse(Call<CurrentConditionsResult> call, Response<CurrentConditionsResult> response) {
                Measurement m = response.body().measurement;
                if (m != null) {
                    currentConditionsRequestCache.write(location, m);
                    callback.onSuccess(m);
                } else {
                    callback.onError(null);
                }
            }
            @Override
            public void onFailure(Call<CurrentConditionsResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecastDayHourly(double latitude, double longitude, final ServiceCallback<List<ForecastHour>> callback) {
        final String location = latitude + "," + longitude;
        if (forecastDayHourlyRequestCache.read(location, callback)) {
            return;
        }

        service.forecastDayHourly(Constants.WU_API_KEY, location).enqueue(new Callback<ForecastHourlyResult>() {
            @Override
            public void onResponse(Call<ForecastHourlyResult> call, Response<ForecastHourlyResult> response) {
                List<ForecastHour> forecastHours = response.body().hours;
                forecastDayHourlyRequestCache.write(location, forecastHours);
                callback.onSuccess(forecastHours);
            }

            @Override
            public void onFailure(Call<ForecastHourlyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecast10Day(double latitude, double longitude, final ServiceCallback<List<ForecastDay>> callback) {
        final String location = latitude + "," + longitude;
        if (forecast10DayRequestCache.read(location, callback)) {
            return;
        }

        service.forecast10Day(Constants.WU_API_KEY, location).enqueue(new Callback<ForecastDailyResult>() {
            @Override
            public void onResponse(Call<ForecastDailyResult> call, Response<ForecastDailyResult> response) {
                List<ForecastDay> forecastDays = response.body().days;
                forecast10DayRequestCache.write(location, forecastDays);
                callback.onSuccess(forecastDays);
            }

            @Override
            public void onFailure(Call<ForecastDailyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getForecast10DayHourly(double latitude, double longitude, final ServiceCallback<List<ForecastHour>> callback) {
        final String location = latitude + "," + longitude;
        if (forecast10DayHourlyRequestCache.read(location, callback)) {
            return;
        }

        service.forecast10DayHourly(Constants.WU_API_KEY, location).enqueue(new Callback<ForecastHourlyResult>() {
            @Override
            public void onResponse(Call<ForecastHourlyResult> call, Response<ForecastHourlyResult> response) {
                List<ForecastHour> forecastHours = response.body().hours;
                forecastDayHourlyRequestCache.write(location, forecastHours);
                callback.onSuccess(forecastHours);
            }

            @Override
            public void onFailure(Call<ForecastHourlyResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}
