package com.philipp.paris.weatherapp.components;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.service.impl.MeasurementService;

import java.util.List;
import java.util.Locale;


/**
 * view which displays the most recent measurement data
 */
public class MeasurementView extends GridLayout implements ServiceCallback<Weather> {
    private MeasurementService service;
    private TextView tvTemperature;
    private TextView tvMinTemperature;
    private TextView tvMaxTemperature;
    private TextView tvHumidity;
    private TextView tvPressure;


    public MeasurementView(Context context) {
        super(context);
        init(context);
    }

    public MeasurementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        /* init UI */
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.measurement_view, this, true);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvMinTemperature = findViewById(R.id.tvMinTemperature);
        tvMaxTemperature = findViewById(R.id.tvMaxTemperature);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvPressure = findViewById(R.id.tvPressure);

        /* init services */
        service = new MeasurementService(context);

        /* display data */
        updateUI();
    }

    private void updateUI() {
        service.getMeasurementsToday(this);

        Float currentTemperature = -20.3f;//m.get(m.size() - 1).getTemperature();
        Float minTemperature = -7.23f;
        Float maxTemperature = 30.23f;

        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", currentTemperature));
        tvMinTemperature.setText(String.format(Locale.getDefault(), "%.1f°", minTemperature));
        tvMaxTemperature.setText(String.format(Locale.getDefault(), "%.1f°", maxTemperature));
    }

    @Override
    public void onResponse(List<Weather> data) {
        Float currentTemperature = 1f;//m.get(m.size() - 1).getTemperature();
        Float minTemperature = -7.23f;
        Float maxTemperature = 30.23f;

        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", currentTemperature));
        tvMinTemperature.setText(String.format(Locale.getDefault(), "%.1f°", minTemperature));
        tvMaxTemperature.setText(String.format(Locale.getDefault(), "%.1f°", maxTemperature));
    }

    @Override
    public void onError() {
        // TODO
        //Snackbar.make(findViewById(R.id.tvTemperature), R.string.error_retrieving_measurements, 1).show();
    }
}
