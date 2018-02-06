package com.philipp.paris.weatherapp.service;


import com.philipp.paris.weatherapp.domain.Weather;

import java.util.Date;
import java.util.List;

public interface IMeasurementService {
    /**
     * @return returns measurements of the current day
     */
    List<Weather> getMeasurementsToday();

    /**
     * @param from start of the interval
     * @param to end of the interval
     * @return returns measurements in the interval
     */
    List<Weather> getMeasurements(Date from, Date to);

    /**
     * @return returns all measurements
     */
    List<Weather> getMeasurements();
}
