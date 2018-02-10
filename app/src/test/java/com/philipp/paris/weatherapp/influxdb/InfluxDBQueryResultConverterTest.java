package com.philipp.paris.weatherapp.influxdb;

import com.philipp.paris.weatherapp.influxdb.util.TestDataUtil;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQueryResult;
import com.philipp.paris.weatherapp.web.influxdb.conversion.InfluxDBQueryResultConverter;

import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class InfluxDBQueryResultConverterTest {
    private InfluxDBQueryResultConverter converter = new InfluxDBQueryResultConverter();


    @Test
    public void convertSingleSeriesResponse() throws Exception {
        // SETUP
        TestDataUtil.TestSeries series = new TestDataUtil.TestSeries("series1");
        TestDataUtil.TestEntity e1 = TestDataUtil.createTestData("2015-01-29T21:55:43.702900257Z", 1.0);
        TestDataUtil.TestEntity e2 = TestDataUtil.createTestData("2016-01-29T21:55:43.702900257Z", 2.0);
        TestDataUtil.TestEntity e3 = TestDataUtil.createTestData("2017-01-29T21:55:43.702900257Z", 3.0);
        series.add(e1, e2, e3);

        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), TestDataUtil.asJson(series));

        // EXEC
        InfluxDBQueryResult result = converter.convert(body);

        // VERIFY
        assertEquals(1, result.seriesCount());
        TestDataUtil.assertSeries(series, result.getSeries(0));
    }

    @Test
    public void convertMultiSeriesResponse() throws Exception {
        // SETUP
        TestDataUtil.TestSeries series1 = new TestDataUtil.TestSeries("series1");
        TestDataUtil.TestEntity e11 = TestDataUtil.createTestData("2015-01-29T21:55:43.702900257Z", 1.0);
        TestDataUtil.TestEntity e12 = TestDataUtil.createTestData("2016-01-29T21:55:43.702900257Z", 2.0);
        TestDataUtil.TestEntity e13 = TestDataUtil.createTestData("2017-01-29T21:55:43.702900257Z", 3.0);
        series1.add(e11, e12, e13);

        TestDataUtil.TestSeries series2 = new TestDataUtil.TestSeries("series2");
        TestDataUtil.TestEntity e21 = TestDataUtil.createTestData("2015-03-29T21:55:43.702900257Z", 21.0);
        TestDataUtil.TestEntity e22 = TestDataUtil.createTestData("2016-03-29T21:55:43.702900257Z", 22.0);
        TestDataUtil.TestEntity e23 = TestDataUtil.createTestData("2017-03-29T21:55:43.702900257Z", 23.0);
        series2.add(e21, e22, e23);

        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), TestDataUtil.asJson(series1, series2));

        // EXEC
        InfluxDBQueryResult result = converter.convert(body);

        // VERIFY
        assertEquals(2, result.seriesCount());
        TestDataUtil.assertSeries(series1, result.getSeries(0));
        TestDataUtil.assertSeries(series2, result.getSeries(1));
    }

    @Test(expected = IOException.class)
    public void convertErrorResponse() throws IOException {
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"),
                "{\"error\": \"unable to parse authentication credentials\"}");

        // EXEC
        converter.convert(body);
    }

    @Test(expected = IOException.class)
    public void convertInvalidDataResponse() throws IOException {
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), "{\"key\":\"value\"}");

        // EXEC
        converter.convert(body);
    }

    @Test(expected = IOException.class)
    public void convertInvalidJsonResponse() throws IOException {
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), "{invalid json}");

        // EXEC
        converter.convert(body);
    }
}
