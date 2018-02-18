package com.example.mohamednagy.myapplication.downloadData;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.mohamednagy.myapplication.BuildConfig;
import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.model.Review;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamednagy on 10/8/2016.
 * Get api data and parse it and base it to database to storing
 */
public class DownloadNetworkData {

    public static void FetchMovieDataFromURL(String sortType, Context context) {
        URL url = null;

        if (sortType != null) {

            try {
                url = Utility.UrlBuilder.createMoviesUrl(sortType);
                Log.e("url",url.toString());
            } catch (MalformedURLException e) {
                Log.e("URL create Error", e.toString());
            }

            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                assert url != null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                String pageJSON = getPageFromStreamAsJSON(inputStream);

                ArrayList<ContentValues> contentValuesArrayList
                        = ParserJSON.MovieListParser.getMovieDataFromJson(pageJSON);

                assert contentValuesArrayList != null;

                ContentValues[] contentValues = new ContentValues[contentValuesArrayList.size()];
                contentValuesArrayList.toArray(contentValues);

                context.getContentResolver().delete(
                        MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,null,null);
                context.getContentResolver().bulkInsert(
                        MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,
                        contentValues);

                /*
                 * Test
                 * Log.e("inserted",String.valueOf(inserted));
                 */
            } catch (IOException e) {
                Log.e("Error during Fetching", e.toString());
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();

                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("inputStream close", e.toString());
                    }
            }
        }
    }

    public static List<Review> FetchReviewDataFromURL(String movieId, Context context) {
        URL url = null;
        List<Review> reviewList = new ArrayList<>();

        if (movieId != null) {

            try {
                url = Utility.UrlBuilder.createReviewsUrl(movieId);
                Log.e("url", url.toString());
            } catch (MalformedURLException e) {
                Log.e("URL create Error", e.toString());
            }

            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                assert url != null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                String pageJSON = getPageFromStreamAsJSON(inputStream);

                reviewList = ParserJSON.ReviewListParser.getReviewDataFromJson(pageJSON);
            } catch (IOException e) {
                Log.e("Error during Fetching", e.toString());
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();

                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("inputStream close", e.toString());
                    }
            }
        }

        return reviewList;
    }

    private static String getPageFromStreamAsJSON
            (InputStream inputStream){

        StringBuilder stringBuilder =
                new StringBuilder();
        String pageAsJSON = null;
        try{
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream);
            BufferedReader bufferedReader =
                    new BufferedReader(inputStreamReader);

            String pageLine = bufferedReader.readLine();
            while(pageLine != null){
                stringBuilder.append(pageLine);
                pageLine = bufferedReader.readLine();
            }
            pageAsJSON = stringBuilder.toString();
        }catch (IOException e){
            Log.e("inputStream Null",e.toString());
        }

        return pageAsJSON;
    }

}
