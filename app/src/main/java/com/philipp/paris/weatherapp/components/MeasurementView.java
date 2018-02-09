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
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.service.impl.MeasurementService;
import com.philipp.paris.weatherapp.util.StreamUtil;

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
    }

    @Override
    public void onResponse(List<Weather> data) {
        Weather current = data.get(data.size() - 1);
        Float currentTemperature = current.getTemperature();
        Float currentHumidity = current.getHumidity() * 100;
        Float currentPressure = current.getPressure();

        tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", currentTemperature));
        tvHumidity.setText(String.format(Locale.getDefault(), "%.0f %%", currentHumidity));
        tvPressure.setText(String.format(Locale.getDefault(), "%.0f hPa", currentPressure));

        tvMinTemperature.setText(String.format(Locale.getDefault(), "%.1f°", 0f));
        tvMaxTemperature.setText(String.format(Locale.getDefault(), "%.1f°", 0f));
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, t.getMessage(), t);
        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }
}
