package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.util.WeatherIconUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class SimpleDayForecastView extends LinearLayout {
    private TextView tvTime;
    private ImageView ivIcon;
    private TextView tvTemperature;
    private TextView tvTemperatureMin;
    private TextView tvQPF;
    private TextView tvProbability;
    private TextView tvWindDirection;
    private TextView tvWind;

    public SimpleDayForecastView(Context context) {
        super(context);
        init(context);
    }

    public SimpleDayForecastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.simple_day_forecast_view_item, this, true);

        tvTime = findViewById(R.id.tvTime);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvTemperatureMin = findViewById(R.id.tvTemperatureMin);
        tvQPF = findViewById(R.id.tvQPF);
        tvProbability = findViewById(R.id.tvProb);
        tvWind = findViewById(R.id.tvWind);
        tvWindDirection = findViewById(R.id.tvDirection);
        ivIcon = findViewById(R.id.ivIcon);
    }

    public void setData(ForecastDay data) {
        tvTime.setText(DateUtil.format(getContext(), data.getTime(), "EEE, d MMM"));

        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", data.getTemperature()));
        tvTemperatureMin.setText(String.format(Locale.getDefault(), "%.0f°", data.getTemperatureMin()));
        ivIcon.setImageResource(WeatherIconUtil.getDrawableID(getContext(), data.getIconKey(), data.getTime()));
        tvWind.setText(String.format(Locale.getDefault(), "%.0f km/h", data.getWindMax()));
        tvWindDirection.setText(data.getWindDirection());
        tvProbability.setText(String.format(Locale.getDefault(), "%d %%", (int) (data.getPop() * 100)));
        if (data.getQpfAllDay() >= data.getSnowAllDay() && data.getQpfAllDay() > 0) {
            tvQPF.setText(String.format(Locale.getDefault(), "%.0f mm", data.getQpfAllDay()));
        } else if (data.getSnowAllDay() > 0) {
            tvQPF.setText(String.format(Locale.getDefault(), "%.0f cm", data.getSnowAllDay()));
        }
    }
}
