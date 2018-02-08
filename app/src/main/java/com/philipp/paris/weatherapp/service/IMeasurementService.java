package com.philipp.paris.weatherapp.service;


import com.philipp.paris.weatherapp.domain.Weather;

import java.util.Date;
import java.util.List;

public interface IMeasurementService {
    /**
     * @return returns measurements of the current day
     */
    void getMeasurementsToday(ServiceCallback<Weather> callback);

    /**
     * @param from start of the interval
     * @param to end of the interval
     * @return returns measurements in the interval
     */
    void getMeasurements(Date from, Date to, ServiceCallback<Weather> callback);

    /**
     * @return returns all measurements
     */
    void getMeasurements(ServiceCallback<Weather> callback);
}
