package com.philipp.paris.weatherapp.service.impl;

import com.philipp.paris.weatherapp.domain.Weather;
import com.philipp.paris.weatherapp.service.IMeasurementService;

import java.util.Date;
import java.util.List;

public class MeasurementService implements IMeasurementService {
    @Override
    public List<Weather> getMeasurementsToday() {
        return null;
    }

    @Override
    public List<Weather> getMeasurements(Date from, Date to) {
        return null;
    }

    @Override
    public List<Weather> getMeasurements() {
        return null;
    }
}
