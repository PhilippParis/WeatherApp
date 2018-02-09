package com.philipp.paris.weatherapp.web.influxdb.conversion;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class InfluxDBQueryResultToObjectConverter {
    public static <T> T convert(String[] columns, String[] values, Class<T> clazz) throws ConversionFailedException {
        try {
            T cc = clazz.newInstance();
            for (int i = 0; i < columns.length; i++) {
                assignField(cc, cc.getClass().getDeclaredField(columns[i]), values[i]);
            }
            return cc;
        } catch (Exception e) {
            throw new ConversionFailedException(e);
        }
    }

    private static <T> void assignField(T cc, Field field, String value) throws IllegalAccessException, ParseException, ConversionFailedException {
        Class<?> fieldType = field.getType();
        boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            if (String.class.isAssignableFrom(fieldType)) {
                field.set(cc, String.valueOf(value));
            } else if (Date.class.isAssignableFrom(field.getType())) {
                field.set(cc, ISO8601Utils.parse(String.valueOf(value), new ParsePosition(0)));
            } else if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
                field.set(cc, Double.parseDouble(value));
            } else if (float.class.isAssignableFrom(fieldType) || Float.class.isAssignableFrom(fieldType)) {
                field.set(cc, Float.parseFloat(value));
            } else if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                field.set(cc, Long.parseLong(value));
            } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
                field.set(cc, Integer.parseInt(value));
            } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
                field.set(cc, Boolean.parseBoolean(value));
            } else {
                throw new ConversionFailedException("unsupported type '" + fieldType.toString() + "'");
            }
        } finally {
            field.setAccessible(isAccessible);
        }
    }
}
