package com.philipp.paris.weatherapp.influxdb.util;


import com.google.gson.internal.bind.util.ISO8601Utils;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQueryResult;
import com.philipp.paris.weatherapp.web.influxdb.conversion.ConversionFailedException;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestDataUtil {
    public static class TestSeries {
        private String name;
        private String[] columns = {"time", "value"};
        private List<TestEntity> data = new ArrayList<>();

        public TestSeries(String name) {
            this.name = name;
        }

        public void add(TestEntity... entity) {
            data.addAll(Arrays.asList(entity));
        }

        public String asJson() {
            StringBuilder values = new StringBuilder(data.get(0).asJson());
            for (int i = 1; i < data.size(); i++) {
                values.append(",").append(data.get(i).asJson());
            }

            return "{\"statement_id\": 0," +
                    "\"series\": [\n" +
                    "   {\"name\": \"" + name + "\", " +
                    "    \"columns\": [\"time\",\"value\"],\n" +
                    "    \"values\": [" + values.toString() + "]\n" +
                    "   }" +
                    "]}";
        }
    }

    public static class TestEntity {
        private Date time;
        private double value;

        public TestEntity() {
        }

        public TestEntity(Date time, double value) {
            this.time = time;
            this.value = value;
        }

        public Date getTime() {
            return time;
        }

        public double getValue() {
            return value;
        }

        public String asJson() {
           return "[\"" + ISO8601Utils.format(time, true) + "\"," + value + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestEntity entity = (TestEntity) o;

            if (Double.compare(entity.value, value) != 0) return false;
            return time.equals(entity.time);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = time.hashCode();
            temp = Double.doubleToLongBits(value);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    public static TestEntity createTestData(String date, double value) throws ParseException {
        return new TestEntity(toDate(date), value);
    }

    public static String asJson(TestSeries... series) {
        StringBuilder joined = new StringBuilder(series[0].asJson());
        for (int i = 1; i < series.length; i++) {
            joined.append(",").append(series[i].asJson());
        }
        return "{results: [" + joined.toString() + "]}";
    }

    public static void assertSeries(TestSeries expected, InfluxDBQueryResult.Series actual) throws ParseException, ConversionFailedException {
        assertEquals(expected.name, actual.getName());
        assertEquals(expected.data.size(), actual.getRowCount());
        assertEquals(expected.columns.length, actual.getColumnCount());
        assertArrayEquals(expected.columns, actual.getColumns());

        // verify entries
        for (int i = 0; i < expected.data.size(); i++) {
            assertEquals(expected.data.get(i).getTime(), toDate(actual.get(i,0)));
            assertEquals(expected.data.get(i).getValue(), Double.parseDouble(actual.get(i,1)), 0.001);
        }

        // verify conversion
        List<TestEntity> actualEntities = actual.toObject(TestEntity.class);
        for (int i = 0; i < expected.data.size(); i++) {
            assertEquals(expected.data.get(i), actualEntities.get(i));
        }
    }

    public static Date toDate(String date) throws ParseException {
        return ISO8601Utils.parse(date, new ParsePosition(0));
    }
}
