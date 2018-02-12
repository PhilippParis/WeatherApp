package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.util.WeatherIconUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class HourlyForecastView extends GridLayout {
    private class Item extends LinearLayout {
        private TextView tvTime;
        private ImageView ivIcon;
        private TextView tvTemperature;
        private DateFormat dateFormat = new SimpleDateFormat("HH:00", Locale.getDefault());

        public Item(Context context, ForecastHour data) {
            super(context);
            init(context, data);
        }

        private void init(Context context, ForecastHour data) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.hourly_forecast_view_item, this, true);

            tvTime = findViewById(R.id.tvTime);
            tvTemperature = findViewById(R.id.tvTemperature);
            ivIcon = findViewById(R.id.ivIcon);

            tvTime.setText(dateFormat.format(data.getTime()));
            tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", data.getTemperature()));
            ivIcon.setImageResource(WeatherIconUtil.getDrawableID(getContext(), data.getTime(), data.getIconKey()));
        }
    }

    LinearLayout layoutBase;

    public HourlyForecastView(Context context) {
        super(context);
        init(context);
    }

    public HourlyForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hourly_forecast_view, this, true);

        layoutBase = findViewById(R.id.layoutBase);
    }

    public void setData(List<ForecastHour> data, Date from, Date to) {
        layoutBase.removeAllViews();
        for (ForecastHour forecastHour : data) {
            if (forecastHour.getTime().after(from) && forecastHour.getTime().before(to)) {
                layoutBase.addView(new Item(getContext(), forecastHour));
            }
        }

    }
}
