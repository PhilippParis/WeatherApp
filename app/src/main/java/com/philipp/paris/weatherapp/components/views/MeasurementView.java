package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.Measurement;

import java.util.List;
import java.util.Locale;


/**
 * view which displays the most recent measurement data
 */
public class MeasurementView extends GridLayout {
    private static final String TAG = "MeasurementView";

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

        showProgressBar();
    }

    public void showData(List<Measurement> data) {
        if (data.size() == 0) {
            showError();
            return;
        }

        // only one instance -> hide min max temperatures
        if (data.size() == 1){
            findViewById(R.id.layoutMinMaxTemp).setVisibility(GONE);
        } else {
            findViewById(R.id.layoutMinMaxTemp).setVisibility(VISIBLE);
        }

        showDataViews();
        Measurement current = data.get(data.size() - 1);
        Float currentTemperature = current.getTemperature();
        Float currentHumidity = current.getHumidity() * 100;
        Float currentPressure = current.getPressure();

        Float minTemperature = Float.MAX_VALUE;
        Float maxTemperature = Float.MIN_VALUE;

        for (Measurement w : data) {
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

    public void showError() {
        progressBar.setVisibility(GONE);
        vgDataViews.setVisibility(GONE);
        vgErrorView.setVisibility(VISIBLE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
        vgDataViews.setVisibility(GONE);
        vgErrorView.setVisibility(GONE);
    }

    private void showDataViews() {
        progressBar.setVisibility(GONE);
        vgDataViews.setVisibility(VISIBLE);
        vgErrorView.setVisibility(GONE);
    }
}
