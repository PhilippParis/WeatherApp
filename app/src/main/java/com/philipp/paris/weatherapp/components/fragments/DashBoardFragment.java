package com.philipp.paris.weatherapp.components.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.views.HourlyForecastView;
import com.philipp.paris.weatherapp.components.views.MeasurementView;
import com.philipp.paris.weatherapp.components.views.listadapter.ForecastDayAdapter;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.service.ForecastService;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.service.MeasurementService;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.util.Arrays;
import java.util.List;

public class DashBoardFragment extends Fragment {
    private static final String TAG = "DashBoardFragment";

    private MeasurementService measurementService;
    private ForecastService forecastService;

    private MeasurementView measurementView;
    private HourlyForecastView hourlyForecastView;
    private ListView lvForecast;

    public DashBoardFragment() {
    }

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        measurementView = view.findViewById(R.id.measurementView);
        hourlyForecastView = view.findViewById(R.id.hourlyForecastView);
        lvForecast = view.findViewById(R.id.lvForecast);

        measurementService = new MeasurementService(getContext());
        forecastService = new ForecastService();
    }

    @Override
    public void onStart() {
        super.onStart();
        Settings settings = new Settings(getContext());

        if (settings.showHomeLocationData()) {
            getMeasurementFromDatabase();
        } else if (settings.currentLocationSet()) {
            getMeasurementFromForecastService();
        } else {
            measurementView.showProgressBar();
        }

        getForecastHourly();
        getForecast();
    }

    private void getMeasurementFromDatabase() {
        measurementService.getMeasurementsToday(new ServiceCallback<List<Measurement>>() {
            @Override
            public void onSuccess(List<Measurement> data) {
                measurementView.showData(data);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.getMessage());
                measurementView.showError();
            }
        });
    }

    private void getMeasurementFromForecastService() {
        Settings settings = new Settings(getContext());
        forecastService.getCurrentConditions(settings.getCurrentLocationLatitude(),
                settings.getCurrentLocationLongitude(),
                new ServiceCallback<Measurement>() {
                    @Override
                    public void onSuccess(Measurement data) {
                        measurementView.showData(Arrays.asList(data));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        measurementView.showError();
                    }
                });
    }

    private void getForecastHourly() {
        Settings settings = new Settings(getContext());
        forecastService.getForecastDayHourly(settings.getCurrentLocationLatitude(),
                settings.getCurrentLocationLongitude(),
                new ServiceCallback<List<ForecastHour>>() {
                    @Override
                    public void onSuccess(List<ForecastHour> data) {
                        hourlyForecastView.setData(data, DateUtil.getCurrentTime(), DateUtil.getStartEndOfCurrentDay().second);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    private void getForecast() {
        Settings settings = new Settings(getContext());
        forecastService.getForecast10Day(settings.getCurrentLocationLatitude(),
                settings.getCurrentLocationLongitude(),
                new ServiceCallback<List<ForecastDay>>() {
                    @Override
                    public void onSuccess(List<ForecastDay> data) {
                        lvForecast.setAdapter(new ForecastDayAdapter(getContext(), data));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }
}
