package com.philipp.paris.weatherapp.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.converters.DateAxisValueFormatter;
import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MeasurementChart extends GridLayout {
    private static final int TEMP_PADDING = 5;
    private static final float CORRECTION_FACTOR_WEEK = 100000f / (24f*60f*60f);
    private static final float CORRECTION_FACTOR_DAY = 1;

    private String[] colors = {"#B71C1C", "#311B92", "#01579B", "#1B5E20", "#E65100"};

    public enum Scope {DAY, WEEK}

    private LineChart chart;
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
    }

    public void addSeries(String label, List<Weather> data) {
        lineData.addDataSet(toLineDataSet(label, data,
                Color.parseColor(colors[lineData.getDataSetCount()])));

        yAxis.setAxisMinimum(-1.0f * (maxAbsTemperature + TEMP_PADDING));
        yAxis.setAxisMaximum(maxAbsTemperature + TEMP_PADDING);

        lineData.setHighlightEnabled(false);
        lineData.setDrawValues(false);
    }

    public void display() {
        chart.clear();
        chart.setData(lineData);
    }

    private LineDataSet toLineDataSet(String label, List<Weather> data, int color) {
        List<Entry> temperature = new ArrayList<>();
        Date origin = data.get(0).getTime();

        for (Weather w : data) {
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

        // legend
        chart.getLegend().setEnabled(false);
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

    public void setDummyData(Scope scope) {
        for (int j = 2015; j < 2020; j++) {
            List<Weather> weather = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.YEAR, j);
            long prevTime = calendar.getTime().getTime();
            float prevTemp = 0f;
            for (int i = 0; i < (scope == Scope.DAY? 144: 7 * 48); i++) {
                Weather w = new Weather();
                w.setTime(new Date(prevTime));
                w.setTemperature(prevTemp);
                weather.add(w);

                prevTemp = w.getTemperature() + new Random().nextFloat() * 2 - 1.0f;
                prevTime += (scope == Scope.DAY? 10 : 30) * DateUtil.MINUTE;
            }
            addSeries(String.valueOf(j), weather);
        }
    }
}