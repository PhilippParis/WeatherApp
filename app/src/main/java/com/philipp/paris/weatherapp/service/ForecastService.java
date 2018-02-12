package com.philipp.paris.weatherapp.service;


import android.location.Address;

import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.Constants;
import com.philipp.paris.weatherapp.web.weatherunderground.WUService;
import com.philipp.paris.weatherapp.web.weatherunderground.conversion.WUConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastService {
    private static final String BASE_URL = "http://api.wunderground.com";
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
        service.conditions(Constants.WU_API_KEY, latitude + "," + longitude).enqueue(new Callback<Measurement>() {
            @Override
            public void onResponse(Call<Measurement> call, Response<Measurement> response) {
                callback.onSuccess(response.body());
            }
            @Override
            public void onFailure(Call<Measurement> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
