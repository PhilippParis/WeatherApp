package com.philipp.paris.weatherapp.service;

public interface ServiceCallback<T> {
    void onSuccess(T data);
    void onError(Throwable t);
}
