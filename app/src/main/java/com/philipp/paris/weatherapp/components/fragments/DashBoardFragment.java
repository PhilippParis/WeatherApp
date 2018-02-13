package com.philipp.paris.weatherapp.components.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.activities.DayDetailActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashBoardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = "DashBoardFragment";

    private MeasurementService measurementService = MeasurementService.getInstance();
    private ForecastService forecastService = ForecastService.getInstance();

    private MeasurementView measurementView;
    private HourlyForecastView hourlyForecastView;
    private ListView lvForecast;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        lvForecast.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Settings settings = new Settings();

        if (settings.showHomeLocationData()) {
            getMeasurementFromDatabase();
        } else if (forecastService.currentLocationSet()) {
            getMeasurementFromForecastService();
        } else {
            measurementView.showProgressBar();
        }

        getForecastHourly();
        getForecast();
    }

    private void updateDayForecastView(List<ForecastDay> data) {
        lvForecast.setAdapter(new ForecastDayAdapter(getContext(),
                Arrays.asList(data.get(0), data.get(1), data.get(2), data.get(3))));
    }

    private void updateHourForecastView(List<ForecastHour> data) {
        Pair<Date, Date> range = DateUtil.after(DateUtil.getStartEndOfCurrentDay(), 6 * DateUtil.HOUR);
        Date current = DateUtil.getCurrentTime();
        hourlyForecastView.setData(data, current.after(range.first) ? current : range.first, range.second);
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
        forecastService.getCurrentConditions(
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
        forecastService.getForecastDayHourly(
                new ServiceCallback<List<ForecastHour>>() {
                    @Override
                    public void onSuccess(List<ForecastHour> data) {
                        updateHourForecastView(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    private void getForecast() {
        forecastService.getForecast10Day(
                new ServiceCallback<List<ForecastDay>>() {
                    @Override
                    public void onSuccess(List<ForecastDay> data) {
                        updateDayForecastView(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    @Override
    public void onRefresh() {
        measurementService.clearCache();
        forecastService.clearCache();
        swipeRefreshLayout.setRefreshing(false);
        onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DayDetailActivity.class);
        intent.putExtra(DayDetailActivity.PAGER_POSITION, i);
        startActivity(intent);
    }
}
