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
                field.setDouble(cc, Double.parseDouble(value));
            } else if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                field.setLong(cc, ((Double) Double.parseDouble(value)).longValue());
            } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
                field.setInt(cc, ((Double) Double.parseDouble(value)).intValue());
            } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
                field.setBoolean(cc, Boolean.valueOf(String.valueOf(value)));
            } else {
                throw new ConversionFailedException("unsupported type '" + fieldType.toString() + "'");
            }
        } finally {
            field.setAccessible(isAccessible);
        }
    }
}
