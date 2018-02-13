package com.philipp.paris.weatherapp.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.fragments.DayDetailFragment;
import com.philipp.paris.weatherapp.domain.ForecastDay;
import com.philipp.paris.weatherapp.domain.ForecastHour;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.service.ForecastService;
import com.philipp.paris.weatherapp.service.ServiceCallback;
import com.philipp.paris.weatherapp.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayDetailActivity extends AppCompatActivity {
    public static final String PAGER_POSITION = "PAGER_POSITION";
    private static final String TAG = "DayDetailActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ForecastService forecastService = ForecastService.getInstance();

    private List<ForecastDay> forecastDays;
    private List<ForecastHour> forecastHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById(R.id.container);

        showProgressBar();
        getData();
    }

    private void getData() {
        Settings settings = new Settings();
        forecastService.getForecast10DayHourly(
                new ServiceCallback<List<ForecastHour>>() {
                    @Override
                    public void onSuccess(List<ForecastHour> data) {
                        forecastHours = data;
                        getForecast10Day();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        showError();
                    }
                });
    }

    private void getForecast10Day() {
        Settings settings = new Settings();
        forecastService.getForecast10Day(
                new ServiceCallback<List<ForecastDay>>() {
                    @Override
                    public void onSuccess(List<ForecastDay> data) {
                        forecastDays = data;
                        showPager();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        showError();
                    }
                });
    }

    private void showError() {

    }

    private void showProgressBar() {

    }

    private void showPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), forecastDays, forecastHours);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra(PAGER_POSITION, 0));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<ForecastDay> forecastDays;
        private List<ForecastHour> forecastHours;

        public SectionsPagerAdapter(FragmentManager fm, List<ForecastDay> forecastDays, List<ForecastHour> forecastHours) {
            super(fm);
            this.forecastDays = forecastDays;
            this.forecastHours = forecastHours;
        }

        @Override
        public Fragment getItem(int position) {
            ForecastDay day = forecastDays.get(position);
            return DayDetailFragment.newInstance(day, getForecastHoursOfDay(day));
        }

        private ArrayList<ForecastHour> getForecastHoursOfDay(ForecastDay day) {
            ArrayList<ForecastHour> hours = new ArrayList<>();
            Pair<Date, Date> range = DateUtil.getStartEndOfDay(day.getTime());
            for (ForecastHour hour : forecastHours) {
                if (hour.getTime().after(range.first) && hour.getTime().before(range.second)) {
                    hours.add(hour);
                }
            }
            return hours;
        }

        @Override
        public int getCount() {
            return forecastDays.size();
        }
    }
}
