package com.philipp.paris.weatherapp.components;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.service.MeasurementService;

import java.util.List;

public class DashBoardFragment extends Fragment implements ServiceCallback<List<Weather>> {
    private static final String TAG = "DashBoardFragment";

    private MeasurementService measurementService;
    private MeasurementView measurementView;

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
        measurementService = new MeasurementService(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        Settings settings = new Settings(getContext());

        if (settings.showHomeLocationData()) {
            measurementService.getMeasurementsToday(this);
        } else {
            // TODO WeatherProviderService.getMeasurementsToday
        }
    }

    @Override
    public void onSuccess(List<Weather> data) {
        measurementView.showData(data);
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, t.getMessage());
        measurementView.showError();
    }
}
