package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Newsfeed> fetchNewsfeedData(String requestUrl) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Newsfeed> newsfeeds = extractFeatureFromJson(jsonResponse);
        return newsfeeds;
    }

    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String formatDate(String rawDate) {

        String jsonDate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDate, Locale.getDefault());
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "EEE , MMM d yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.getDefault());
            return finalDateFormatter.format(parsedJsonDate);
        } catch (java.text.ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }

    public static List<Newsfeed> extractFeatureFromJson(String newsfeedJSON) {

        if (TextUtils.isEmpty(newsfeedJSON)) {
            return null;
        }
        List<Newsfeed> newsfeeds = new ArrayList<>();
        try {
            JSONObject newsfeedRoot = new JSONObject(newsfeedJSON);
            JSONObject response = newsfeedRoot.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNewsfeed = results.getJSONObject(i);

                String title = currentNewsfeed.getString("webTitle");
                String sectionName = currentNewsfeed.getString("sectionName");
                String publicationDate = currentNewsfeed.getString("webPublicationDate");
                publicationDate = formatDate(publicationDate);
                String url = currentNewsfeed.getString("webUrl");

                Newsfeed newsfeed = new Newsfeed(title, sectionName, publicationDate, url);
                newsfeeds.add(newsfeed);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the NewsFeed JSON Results", e);
        }
        return newsfeeds;
    }
}