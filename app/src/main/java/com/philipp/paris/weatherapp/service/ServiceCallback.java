package com.philipp.paris.weatherapp.service;

import java.util.List;

public interface ServiceCallback<T> {
    void onResponse(List<T> data);
    void onError(Throwable t);
}
