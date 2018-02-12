package com.philipp.paris.weatherapp.components.views.listadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.philipp.paris.weatherapp.components.views.SimpleDayForecastView;
import com.philipp.paris.weatherapp.domain.ForecastDay;

import java.util.ArrayList;
import java.util.List;

public class ForecastDayAdapter extends BaseAdapter {

    Context context;
    List<ForecastDay> data = new ArrayList<>();

    public ForecastDayAdapter(Context context, List<ForecastDay> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SimpleDayForecastView v = new SimpleDayForecastView(context);
        v.setData(data.get(i));
        return v;
    }
}
