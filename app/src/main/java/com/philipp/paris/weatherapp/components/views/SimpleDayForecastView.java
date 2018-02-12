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
import com.philipp.paris.weatherapp.util.DateUtil;
import com.philipp.paris.weatherapp.util.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class SimpleDayForecastView extends LinearLayout {
    private TextView tvTime;
    private TextView tvCondition;
    private ImageView ivIcon;
    private TextView tvTemperature;
    private TextView tvTemperatureMin;
    private DateFormat dateFormat = new SimpleDateFormat("E, DD.mm", Locale.getDefault());

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
        ivIcon = findViewById(R.id.ivIcon);
    }

    public void setData(ForecastDay data) {
        tvTime.setText(dateFormat.format(data.getTime()));
        tvCondition.setText(data.getCondition());
        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", data.getTemperature()));
        tvTemperatureMin.setText(String.format(Locale.getDefault(), "%.1f°", data.getTemperatureMin()));
        new DownloadImageTask(ivIcon).execute(data.getIconUrl());
    }
}
