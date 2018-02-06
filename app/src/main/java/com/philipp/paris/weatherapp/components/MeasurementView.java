package com.philipp.paris.weatherapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import com.philipp.paris.weatherapp.R;


/**
 * view which displays the most recent measurement data
 */
public class MeasurementView extends GridLayout {
    public MeasurementView(Context context) {
        super(context);
        init(context);
    }

    public MeasurementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.measurement_view, this, true);
    }


}
