package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.util.WeatherIconUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class SimpleDayForecastView extends LinearLayout {
    private TextView tvTime;
    private TextView tvCondition;
    private ImageView ivIcon;
    private ImageView ivRain;
    private ImageView ivSnow;
    private TextView tvTemperature;
    private TextView tvTemperatureMin;
    private TextView tvRain;
    private TextView tvWind;
    private DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());

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
        tvCondition = findViewById(R.id.tvCondition);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvTemperatureMin = findViewById(R.id.tvTemperatureMin);
        tvRain = findViewById(R.id.tvRain);
        tvWind = findViewById(R.id.tvWind);
        ivIcon = findViewById(R.id.ivIcon);
        ivRain = findViewById(R.id.ivRain);
        ivSnow = findViewById(R.id.ivSnow);
    }

    public void setData(ForecastDay data) {
        tvTime.setText(dateFormat.format(data.getTime()));
        tvCondition.setText(data.getCondition());
        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", data.getTemperature()));
        tvTemperatureMin.setText(String.format(Locale.getDefault(), "%.0f°", data.getTemperatureMin()));
        ivIcon.setImageResource(WeatherIconUtil.getDrawableID(getContext(), data.getIconKey(), data.getTime()));
        tvWind.setText(String.format(Locale.getDefault(), "%.0f km/h", data.getWindMax()));
        if (data.getQpfAllDay() >= data.getSnowAllDay()) {
            ivRain.setVisibility(VISIBLE);
            ivSnow.setVisibility(GONE);
            tvRain.setText(String.format(Locale.getDefault(), "%.0f mm", data.getQpfAllDay()));
        } else {
            ivRain.setVisibility(GONE);
            ivSnow.setVisibility(VISIBLE);
            tvRain.setText(String.format(Locale.getDefault(), "%.0f cm", data.getSnowAllDay()));
        }
    }
}
