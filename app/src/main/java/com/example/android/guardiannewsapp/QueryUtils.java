
package com.example.android.guardiannewsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl("http://content.guardianapis.com/search?q=debates&api-key=test");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return newsList;
    }

    /** Returns new URL object from the given string URL.*/
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    /** Make an HTTP request to the given URL and return a String as the response. */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                urlConnection.disconnect();
                return "API request failed. Response Code: " + urlConnection.getResponseCode();
            }
        } catch (IOException e) {
//            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.IOexceptionHTTP), e);
                        Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /*Convert the {@link InputStream} into a String which contains the whole JSON response from the server.*/
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

    /* Return a list of {@link Book} objects that has been built up from parsing the given JSON response. */
    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
 //               ArrayList<String> authors = new ArrayList<>();

                // Get a single news item at position i within the list of newsList
                JSONObject currentNewsItem = results.getJSONObject(i);
                String title = currentNewsItem.getString("webTitle");
                String webUrl = currentNewsItem.getString("webUrl");
                String wD = currentNewsItem.getString("webPublicationDate");
                String webDate = wD.substring(0, Math.min(wD.length(), 10));



                //decoding the image
                URL imgUrl = new URL(webUrl + "#img-1");
                Bitmap bmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());


                News newsItem = new News(title, webUrl, webDate, bmp);
                newsList.add(newsItem);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results.", e);

        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem making the image URL.", e);

        }
              catch (IOException e) {
            Log.e(LOG_TAG, "Problem connecting to retrieve the image.", e);

        }

        return newsList;
    }

}
