package com.philipp.paris.weatherapp.web.influxdb.conversion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.philipp.paris.weatherapp.web.influxdb.InfluxDBQueryResult;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class InfluxDBQueryResultConverter implements Converter<ResponseBody, InfluxDBQueryResult> {
    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_SERIES = "series";
    private static final String JSON_KEY_SERIES_NAME = "name";
    private static final String JSON_KEY_SERIES_COLUMNS = "columns";
    private static final String JSON_KEY_SERIES_ENTRIES = "values";


    @Override
    public InfluxDBQueryResult convert(ResponseBody value) throws IOException {
        try {
            JsonObject jsonObject = new JsonParser().parse(value.string()).getAsJsonObject();
            InfluxDBQueryResult queryResult = new InfluxDBQueryResult();

            JsonArray jsonArray = jsonObject.getAsJsonArray(JSON_KEY_RESULTS);
            for (JsonElement e : jsonArray) {
                queryResult.addSeries(parseSeries(e.getAsJsonObject().getAsJsonArray(JSON_KEY_SERIES).get(0).getAsJsonObject()));
            }
            return queryResult;
        } catch (Exception e) {
            return null;
        }
    }

    private InfluxDBQueryResult.Series parseSeries(JsonObject seriesObject) throws Exception {
        String seriesName = getSeriesName(seriesObject);
        String[] columns = getSeriesColumns(seriesObject);
        String[][] values = getSeriesValues(seriesObject, columns.length);

        return new InfluxDBQueryResult.Series(seriesName, columns, values);
    }

    private String getSeriesName(JsonObject seriesObject) throws Exception {
        return seriesObject.get(JSON_KEY_SERIES_NAME).getAsString();
    }

    private String[] getSeriesColumns(JsonObject seriesObject) throws Exception {
        JsonArray jsonArrayColumns = seriesObject.getAsJsonArray(JSON_KEY_SERIES_COLUMNS);
        String[] cols = new String[jsonArrayColumns.size()];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = jsonArrayColumns.get(i).toString().replace("\"", "");
        }
        return cols;
    }

    private String[][] getSeriesValues(JsonObject seriesObject, int colCount) throws Exception {
        JsonArray jsonArrayEntries = seriesObject.getAsJsonArray(JSON_KEY_SERIES_ENTRIES);
        String[][] values = new String[jsonArrayEntries.size()][colCount];
        for (int r = 0; r < jsonArrayEntries.size(); r++) {
            JsonArray row = jsonArrayEntries.get(r).getAsJsonArray();
            for (int c = 0; c < colCount; c++) {
                values[r][c] = row.get(c).getAsString().replace("\"", "");
            }
        }
        return values;
    }
}