package com.philipp.paris.influxdb;

import android.util.Log;

import com.philipp.paris.influxdb.query.InfluxDBQuery;
import com.philipp.paris.influxdb.util.TestCallback;
import com.philipp.paris.influxdb.util.TestDataUtil;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore({"okhttp3.*", "retrofit2.*"})
public class InfluxDBTest {
    private MockWebServer server;

    private InfluxDB db;

    private String getStringFromFile(String file) throws IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(file).getPath());
        return new String(Files.readAllBytes(path), Charset.defaultCharset());
    }

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Log.class);
        server = new MockWebServer();
        server.start();

        db = InfluxDB.Builder().connect("db", server.url("/").toString());
    }

    @Test
    public void response_error_shouldCallOnFailureWithErrorMsg() throws Exception {
        // SETUP
        TestCallback callback = new TestCallback();

        // MOCK
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile("response_error.json")));

        // EXEC
        db.query(new InfluxDBQuery("query"), callback);

        synchronized (callback) {
            callback.wait(100000);
        }

        // VERIFY
        assertTrue(callback.requestFailed());
        assertEquals("HTTP Response conversion failed: 'error_msg'", callback.getThrowable().getMessage());
    }

    @Test
    public void response_invalid_json_shouldCallOnFailure() throws Exception {
        // SETUP
        TestCallback callback = new TestCallback();

        // MOCK
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("invalid json"));

        // EXEC
        db.query(new InfluxDBQuery("query"), callback);

        synchronized (callback) {
            callback.wait(100000);
        }

        // VERIFY
        assertTrue(callback.requestFailed());
        assertTrue(callback.getThrowable().getMessage().startsWith(("HTTP Response conversion failed:")));
    }

    @Test
    public void response_empty_shouldReturnEmptyInfluxDBQueryResult() throws Exception {
        // SETUP
        TestCallback callback = new TestCallback();

        // MOCK
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile("response_empty.json")));

        // EXEC
        db.query(new InfluxDBQuery("query"), callback);

        synchronized (callback) {
            callback.wait(100000);
        }

        // VERIFY
        assertFalse(callback.requestFailed());
        assertEquals(0, callback.getResult().seriesCount());
    }

    @Test
    public void response_single_series_shouldReturnInfluxDBQueryResult() throws Exception {
        // SETUP
        TestCallback callback = new TestCallback();
        TestDataUtil.TestSeries series = new TestDataUtil.TestSeries("series1");
        TestDataUtil.TestEntity e1 = TestDataUtil.createTestData("2015-01-29T21:55:43.702900257Z", 1.0);
        TestDataUtil.TestEntity e2 = TestDataUtil.createTestData("2016-01-29T21:55:43.702900257Z", 2.0);
        TestDataUtil.TestEntity e3 = TestDataUtil.createTestData("2017-01-29T21:55:43.702900257Z", 3.0);
        series.add(e1, e2, e3);

        // MOCK
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile("response_single_series.json")));

        // EXEC
        db.query(new InfluxDBQuery("query"), callback);

        synchronized (callback) {
            callback.wait(100000);
        }

        // VERIFY
        assertFalse(callback.requestFailed());
        assertEquals(1, callback.getResult().seriesCount());
        TestDataUtil.assertSeries(series, callback.getResult().getSeries(0));
    }

    @Test
    public void response_multi_series_shouldReturnInfluxDBQueryResult() throws Exception {
        // SETUP
        TestCallback callback = new TestCallback();
        TestDataUtil.TestSeries series1 = new TestDataUtil.TestSeries("series1");
        TestDataUtil.TestEntity e11 = TestDataUtil.createTestData("2015-01-29T21:55:43.702900257Z", 1.0);
        TestDataUtil.TestEntity e12 = TestDataUtil.createTestData("2016-01-29T21:55:43.702900257Z", 2.0);
        TestDataUtil.TestEntity e13 = TestDataUtil.createTestData("2017-01-29T21:55:43.702900257Z", 3.0);
        series1.add(e11, e12, e13);

        TestDataUtil.TestSeries series2 = new TestDataUtil.TestSeries("series2");
        TestDataUtil.TestEntity e21 = TestDataUtil.createTestData("2018-01-29T21:55:43.702900257Z", 4.0);
        series2.add(e21);


        // MOCK
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile("response_multi_series.json")));

        // EXEC
        db.query(new InfluxDBQuery("query"), callback);

        synchronized (callback) {
            callback.wait(100000);
        }

        // VERIFY
        assertFalse(callback.requestFailed());
        assertEquals(2, callback.getResult().seriesCount());
        TestDataUtil.assertSeries(series1, callback.getResult().getSeries(0));
        TestDataUtil.assertSeries(series2, callback.getResult().getSeries(1));
    }



}
