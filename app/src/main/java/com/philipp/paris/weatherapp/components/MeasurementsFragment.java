package com.philipp.paris.weatherapp.components;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philipp.paris.weatherapp.R;

public class MeasurementsFragment extends Fragment {

    public MeasurementsFragment() {
        // Required empty public constructor
    }

    public static MeasurementsFragment newInstance() {
        return new MeasurementsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurements, container, false);
    }

}
