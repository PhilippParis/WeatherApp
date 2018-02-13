package com.philipp.paris.weatherapp.components.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.converters.DateAxisValueFormatter;
import com.philipp.paris.weatherapp.domain.Measurement;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeasurementChart extends GridLayout {
    private static final int TEMP_PADDING = 5;
    private static final float CORRECTION_FACTOR_WEEK = 100000f / (24f*60f*60f);
    private static final float CORRECTION_FACTOR_DAY = 1;

    private String[] colors = {"#FF303F9F", "#311B92", "#01579B", "#1B5E20", "#E65100"};

    public enum Scope {DAY, WEEK}

    private LineChart chart;
    private ProgressBar progressBar;
    private YAxis yAxis;
    private XAxis xAxis;
    private Float maxAbsTemperature = Float.MIN_VALUE;
    private Scope scope;
    private LineData lineData;

    public MeasurementChart(Context context) {
        super(context);
        init(context);
    }

    public MeasurementChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void initChart(Scope scope) {
        this.scope = scope;
        maxAbsTemperature = Float.MIN_VALUE;
        lineData = new LineData();
        showProgressBar();
    }

    public void addSeries(String label, List<Measurement> data) {
        lineData.addDataSet(toLineDataSet(label, data,
                Color.parseColor(colors[lineData.getDataSetCount()])));

        yAxis.setAxisMinimum(-1.0f * (maxAbsTemperature + TEMP_PADDING));
        yAxis.setAxisMaximum(maxAbsTemperature + TEMP_PADDING);

        lineData.setHighlightEnabled(false);
        lineData.setDrawValues(false);
    }

    public void show() {
        if (lineData.getDataSetCount() > 0) {
            chart.getLegend().setEnabled(lineData.getDataSetCount() > 1);
            chart.setData(lineData);
            chart.invalidate();
        }

        progressBar.setVisibility(GONE);
        chart.setVisibility(VISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
        chart.setVisibility(GONE);
    }

    private LineDataSet toLineDataSet(String label, List<Measurement> data, int color) {
        List<Entry> temperature = new ArrayList<>();
        Date origin = data.get(0).getTime();

        for (Measurement w : data) {
            float timestamp = (w.getTime().getTime() - origin.getTime())  *
                    (scope == Scope.DAY? CORRECTION_FACTOR_DAY : CORRECTION_FACTOR_WEEK);
            temperature.add(new Entry(timestamp, w.getTemperature()));

            if (maxAbsTemperature < Math.abs(w.getTemperature())) {
                maxAbsTemperature = Math.abs(w.getTemperature());
            }
        }

        formatAxis(scope, origin);
        LineDataSet temperatureDataSet = new LineDataSet(temperature, label);
        temperatureDataSet.setDrawCircles(false);
        temperatureDataSet.setLineWidth(2.5f);
        temperatureDataSet.setColor(color);
        temperatureDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return temperatureDataSet;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.measurement_chart, this, true);

        chart = findViewById(R.id.chart);
        progressBar = findViewById(R.id.progressBar);

        // interaction
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);
        chart.setHardwareAccelerationEnabled(true);

        // axis
        chart.getAxisRight().setEnabled(false);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineColor(Color.TRANSPARENT);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(12);
        xAxis.mLabelHeight = 20;
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMinimum(0);

        yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawZeroLine(true);
        yAxis.setAxisLineColor(Color.TRANSPARENT);

        // styling
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setDescription(null);
        chart.setNoDataText(getResources().getString(R.string.error_no_data));
        chart.setNoDataTextColor(Color.GRAY);
        chart.setNoDataTextTypeface(Typeface.DEFAULT);

        // legend
        Legend legend = chart.getLegend();
        legend.setDrawInside(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    }

    private void formatAxis(Scope scope, Date refTimestamp) {
        DateFormat format = null;
        switch (scope) {
            case DAY:
                format = new SimpleDateFormat("HH:00", Locale.getDefault());
                chart.getViewPortHandler().setMaximumScaleX(5);
                xAxis.setAxisMaximum(DateUtil.DAY * CORRECTION_FACTOR_DAY);
                xAxis.setGranularity(DateUtil.HOUR * CORRECTION_FACTOR_DAY);
                xAxis.setCenterAxisLabels(false);
                break;
            case WEEK:
                format = new SimpleDateFormat("E", Locale.getDefault());
                chart.getViewPortHandler().setMaximumScaleX(10);
                xAxis.setAxisMaximum(DateUtil.WEEK * CORRECTION_FACTOR_WEEK);
                xAxis.setGranularity(DateUtil.DAY * CORRECTION_FACTOR_WEEK);
                xAxis.setCenterAxisLabels(true);
                break;
        }
        xAxis.setValueFormatter(new DateAxisValueFormatter(refTimestamp, format));
    }
}
