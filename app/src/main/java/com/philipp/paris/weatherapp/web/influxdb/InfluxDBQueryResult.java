package com.philipp.paris.weatherapp.web.influxdb;


import com.philipp.paris.weatherapp.web.influxdb.conversion.ConversionFailedException;
import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBQueryResultToObjectConverter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfluxDBQueryResult {
    static public class Series {
        private String name;
        private String[] columns;
        private String[][] values;

        public Series(String name, String[] columns, String[][] values) {
            this.name = name;
            this.columns = columns;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public String[] getColumns() {
            return columns;
        }

        public void setColumns(String[] columns) {
            this.columns = columns;
        }

        public int getRowCount() {
            return values.length;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public void set(int row, String col, String value) {
            values[row][Arrays.asList(columns).indexOf(col)] = value;
        }

        public void set(int row, int col, String value) {
            values[row][col] = value;
        }

        public String get(int row, String col) {
            return values[row][Arrays.asList(columns).indexOf(col)];
        }

        public String get(int row, int col) {
            return values[row][col];
        }


        public <T> List<T> toObject(Class<T> clazz) throws ConversionFailedException {
            List<T> objects = new ArrayList<>();
            for (int i = 0; i < values.length; i++) {
                T object = InfluxDBQueryResultToObjectConverter.convert(columns, values[i], clazz);
                objects.add(object);
            }
            return objects;
        }
    }

    private List<Series> series = new ArrayList<>();

    public InfluxDBQueryResult() {

    }

    public void addSeries(Series s) {
        series.add(s);
    }

    public Series getSeries(int i) {
        return series.get(i);
    }

    public int seriesCount() {
        return series.size();
    }
}
