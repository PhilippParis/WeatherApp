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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
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
    private static final String TAG = "MeasurementView";

    private MeasurementService service;
    private TextView tvTemperature;
    private TextView tvMinTemperature;
    private TextView tvMaxTemperature;
    private TextView tvHumidity;
    private TextView tvPressure;
    private ProgressBar progressBar;
    private ViewGroup vgDataViews;
    private ViewGroup vgErrorView;


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
        progressBar = findViewById(R.id.progressBar);
        vgDataViews = findViewById(R.id.vgDataViews);
        vgErrorView = findViewById(R.id.vgError);

        /* init services */
        service = new MeasurementService(context);

        /* display data */
        updateUI();
    }

    private void updateUI() {
        showProgressBar();
        service.getMeasurementsToday(this);
    }

    @Override
    public void onResponse(List<Weather> data) {
        showDataViews();

        Weather current = data.get(data.size() - 1);
        Float currentTemperature = current.getTemperature();
        Float currentHumidity = current.getHumidity() * 100;
        Float currentPressure = current.getPressure();

        Float minTemperature = Float.MAX_VALUE;
        Float maxTemperature = Float.MIN_VALUE;

        for (Weather w : data) {
            if (minTemperature > w.getTemperature()) {
                minTemperature = w.getTemperature();
            }
            if (maxTemperature < w.getTemperature()) {
                maxTemperature = w.getTemperature();
            }
        }

        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", currentTemperature));
        tvHumidity.setText(String.format(Locale.getDefault(), "%.0f %%", currentHumidity));
        tvPressure.setText(String.format(Locale.getDefault(), "%.0f hPa", currentPressure));
        tvMinTemperature.setText(String.format(Locale.getDefault(), "%.1f°", minTemperature));
        tvMaxTemperature.setText(String.format(Locale.getDefault(), "%.1f°", maxTemperature));
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, t.getMessage(), t);
        showErrorView();
    }

    private void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
        vgDataViews.setVisibility(GONE);
        vgErrorView.setVisibility(GONE);
    }

    private void showDataViews() {
        progressBar.setVisibility(GONE);
        vgDataViews.setVisibility(VISIBLE);
        vgErrorView.setVisibility(GONE);
    }

    private void showErrorView() {
        progressBar.setVisibility(GONE);
        vgDataViews.setVisibility(GONE);
        vgErrorView.setVisibility(VISIBLE);
    }
}
