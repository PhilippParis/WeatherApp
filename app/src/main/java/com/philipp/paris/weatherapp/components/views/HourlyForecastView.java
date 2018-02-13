package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.util.WeatherIconUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyForecastView extends GridLayout implements View.OnClickListener {
    private class Item extends LinearLayout {
        private TextView tvTime;
        private ImageView ivIcon;
        private ImageView ivWindDir;
        private TextView tvTemperature;
        private TextView tvPrecipitation;
        private TextView tvProbability;
        private TextView tvWindSpeed;
        private DateFormat dateFormat = new SimpleDateFormat("HH:00", Locale.getDefault());

        private ViewGroup vgTemperature;
        private ViewGroup vgWind;
        private ViewGroup vgPrecipitiation;

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
            tvPrecipitation = findViewById(R.id.tvPrecipitation);
            tvProbability = findViewById(R.id.tvProbability);
            tvWindSpeed = findViewById(R.id.tvWindSpeed);
            ivIcon = findViewById(R.id.ivIcon);
            ivWindDir = findViewById(R.id.ivWindDir);

            tvTime.setText(dateFormat.format(data.getTime()));
            tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", data.getTemperature()));
            ivIcon.setImageResource(WeatherIconUtil.getDrawableID(getContext(), data.getIconKey(), data.getTime()));

            ivWindDir.setRotation(data.getWindDirection());
            tvWindSpeed.setText(String.format(Locale.getDefault(), "%d km/h", data.getWspd()));

            float precipitation = data.getQpf() > data.getSnow()? data.getQpf() : data.getSnow();
            tvPrecipitation.setText(String.format(Locale.getDefault(), "%d mm", (int) precipitation));
            tvProbability.setText(String.format(Locale.getDefault(), "%d %%", (int) (data.getPop() * 100)));

            vgTemperature = findViewById(R.id.vgTemperature);
            vgWind = findViewById(R.id.vgWind);
            vgPrecipitiation = findViewById(R.id.vgPrecipitation);
        }

        private void setMode(Mode mode) {
            switch (mode) {
                case WIND:
                    vgPrecipitiation.setVisibility(GONE);
                    vgTemperature.setVisibility(GONE);
                    vgWind.setVisibility(VISIBLE);
                    break;
                case TEMPERATURE:
                    vgPrecipitiation.setVisibility(GONE);
                    vgTemperature.setVisibility(VISIBLE);
                    vgWind.setVisibility(GONE);
                    break;
                case PRECIPITATION:
                    vgPrecipitiation.setVisibility(VISIBLE);
                    vgTemperature.setVisibility(GONE);
                    vgWind.setVisibility(GONE);
                    break;
            }
        }
    }

    private enum Mode {TEMPERATURE, WIND, PRECIPITATION}

    private LinearLayout layoutBase;
    private Mode currentMode = Mode.TEMPERATURE;

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

    @Override
    public void onClick(View view) {
        switch (currentMode) {
            case WIND: currentMode = Mode.PRECIPITATION; break;
            case TEMPERATURE: currentMode = Mode.WIND; break;
            case PRECIPITATION: currentMode = Mode.TEMPERATURE; break;
        }
        updateUI();
    }

    private void updateUI() {
        for (int i = 0; i < layoutBase.getChildCount(); i++) {
            Item item = (Item) layoutBase.getChildAt(i);
            item.setMode(currentMode);
        }
    }

    public void setData(List<ForecastHour> data, Date from, Date to) {
        setData(getDataInRange(data, from, to));
    }

    public void setData(List<ForecastHour> data) {
        layoutBase.removeAllViews();
        for (ForecastHour hour : data) {
            Item item = new Item(getContext(), hour);
            item.setOnClickListener(this);
            layoutBase.addView(item);
        }
    }

    private List<ForecastHour> getDataInRange(List<ForecastHour> data, Date from, Date to) {
        List<ForecastHour> inRange = new ArrayList<>();
        for (ForecastHour forecastHour : data) {
            if (forecastHour.getTime().after(from) && forecastHour.getTime().before(to)) {
                inRange.add(forecastHour);
            }
        }
        return inRange;
    }
}
