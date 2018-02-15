package com.philipp.paris.weatherapp.components.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.views.MeasurementChart;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.service.MeasurementService;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeasurementsFragment extends Fragment implements View.OnClickListener,
        ServiceCallback<List<Measurement>>, SwipeRefreshLayout.OnRefreshListener {

    private MeasurementChart chart;
    private ImageButton btnLeft;
    private ImageButton btnRight;
    private TextView tvScope;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MeasurementService service = MeasurementService.getInstance();

    private MeasurementChart.Scope scope = MeasurementChart.Scope.DAY;
    private Pair<Date, Date> range;

    public MeasurementsFragment() {
        // Required empty public constructor
    }

    public static MeasurementsFragment newInstance() {
        return new MeasurementsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart = view.findViewById(R.id.measurementChart);
        btnLeft = view.findViewById(R.id.btnLeft);
        btnRight = view.findViewById(R.id.btnRight);
        tvScope = view.findViewById(R.id.tvScope);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        tvScope.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        scope = MeasurementChart.Scope.DAY;
        range = DateUtil.getStartEndOfCurrentDay();
        chart.initChart(scope);
        sendDataRequest(Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                range = DateUtil.before(range, scope == MeasurementChart.Scope.DAY? DateUtil.DAY : DateUtil.WEEK);
                break;
            case R.id.btnRight:
                range = DateUtil.after(range, scope == MeasurementChart.Scope.DAY? DateUtil.DAY : DateUtil.WEEK);
                break;
            case R.id.tvScope:
                if (scope == MeasurementChart.Scope.DAY) {
                    scope = MeasurementChart.Scope.WEEK;
                    range = DateUtil.getStartEndOfCurrentWeek();
                } else {
                    scope = MeasurementChart.Scope.DAY;
                    range = DateUtil.getStartEndOfCurrentDay();
                }
                break;
        }
        chart.initChart(scope);
        sendDataRequest(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void sendDataRequest(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(range.first);
        calendar.set(Calendar.YEAR, year);
        Date from = calendar.getTime();

        calendar.setTime(range.second);
        calendar.set(Calendar.YEAR, year);
        Date to = calendar.getTime();

        service.getMeasurements(from, to, this);
        setScopeText();
    }

    @Override
    public void onSuccess(List<Measurement> data) {
        if (!data.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data.get(0).getTime());
            int year = calendar.get(Calendar.YEAR);
            chart.addSeries(String.valueOf(year), data, range.first);

            // request data from prev year
            sendDataRequest(year - 1);
        } else {
            chart.show();
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    private void setScopeText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E d.M", Locale.getDefault());
        String txt = dateFormat.format(range.first);
        if (scope == MeasurementChart.Scope.WEEK) {
            txt += " - " + dateFormat.format(range.second);
        }
        tvScope.setText(txt);
    }

    @Override
    public void onRefresh() {
        service.clearCache();
        swipeRefreshLayout.setRefreshing(false);
        onStart();
    }
}
