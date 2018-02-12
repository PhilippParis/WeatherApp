package com.philipp.paris.weatherapp.service;

import com.philipp.paris.weatherapp.util.DateUtil;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<T> {
    private class CacheEntry<T> {
        private Date time;
        private T data;

        CacheEntry(Date time, T data) {
            this.time = time;
            this.data = data;
        }
    }
    private Map<String, CacheEntry<T>> data = new ConcurrentHashMap<>();

    public void write(String key, T entity) {
        data.put(key, new CacheEntry<>(DateUtil.getCurrentTime(), entity));
    }

    public boolean read(String key, ServiceCallback<T> callback) {
        if (data.containsKey(key)) {
            CacheEntry<T> entry = data.get(key);
            if (DateUtil.diff(entry.time, DateUtil.getCurrentTime(), DateUtil.MINUTE) < 10) {
                callback.onSuccess(entry.data);
                return true;
            } else {
                data.remove(key);
            }
        }
        return false;
    }

}
