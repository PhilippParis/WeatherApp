package com.philipp.paris.weatherapp.components.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.views.HourlyForecastView;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.util.WeatherIconUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayDetailFragment extends Fragment {
    private static final String ARG_PARAM_DAY = "ARG_PARAM_DAY";
    private static final String ARG_PARAM_HOUR = "ARG_PARAM_HOUR";

    private ForecastDay forecastDay;
    private List<ForecastHour> forecastHours;

    private TextView tvTemperatureMin;
    private TextView tvTemperature;
    private TextView tvText;
    private TextView tvDate;
    private ImageView ivIcon;
    private HourlyForecastView hourlyForecastView;

    public DayDetailFragment() {
        // Required empty public constructor
    }

    public static DayDetailFragment newInstance(ForecastDay forecastDay, ArrayList<ForecastHour> forecastHours) {
        DayDetailFragment fragment = new DayDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_DAY, forecastDay);
        args.putSerializable(ARG_PARAM_HOUR, forecastHours);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forecastDay = (ForecastDay) getArguments().getSerializable(ARG_PARAM_DAY);
            forecastHours = (List<ForecastHour>) getArguments().getSerializable(ARG_PARAM_HOUR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDate = view.findViewById(R.id.tvDate);
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvTemperatureMin = view.findViewById(R.id.tvTemperatureMin);
        tvText = view.findViewById(R.id.tvText);
        ivIcon = view.findViewById(R.id.ivIcon);
        hourlyForecastView = view.findViewById(R.id.hourlyForecastView);

        tvDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(forecastDay.getTime()));
        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", forecastDay.getTemperature()));
        tvTemperatureMin.setText(String.format(Locale.getDefault(), "%.1f°", forecastDay.getTemperatureMin()));
        tvText.setText(forecastDay.getText());
        ivIcon.setImageResource(WeatherIconUtil.getDrawableID(getContext(), forecastDay.getIconKey()));

        Pair<Date, Date> range = DateUtil.getStartEndOfDay(forecastDay.getTime());
        hourlyForecastView.setData(forecastHours, range.first, range.second);
    }

}
