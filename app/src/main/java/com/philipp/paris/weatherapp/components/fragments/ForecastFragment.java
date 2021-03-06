package com.philipp.paris.weatherapp.components.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.activities.DayDetailActivity;
import com.philipp.paris.weatherapp.components.views.listadapter.ForecastDayAdapter;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.service.ForecastService;
import com.philipp.paris.weatherapp.service.ServiceCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = "ForecastFragment";
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ForecastService forecastService = ForecastService.getInstance();

    public ForecastFragment() {
        // Required empty public constructor
    }

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        listView.setOnItemClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        forecastService.getForecast10Day(new ServiceCallback<List<ForecastDay>>() {
                    @Override
                    public void onSuccess(List<ForecastDay> data) {
                        listView.setAdapter(new ForecastDayAdapter(getContext(), data));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    @Override
    public void onRefresh() {
        forecastService.clearCache();
        swipeRefreshLayout.setRefreshing(false);
        onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DayDetailActivity.class);
        intent.putExtra(DayDetailActivity.PAGER_POSITION, i);
        startActivity(intent);
    }
}
