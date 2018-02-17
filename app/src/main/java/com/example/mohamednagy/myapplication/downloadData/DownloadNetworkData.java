package com.example.mohamednagy.myapplication.downloadData;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.Utility;

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

/**
 * Created by mohamednagy on 10/8/2016.
 * Get api data and parse it and base it to database to storing
 */
public class DownloadNetworkData {

    public static void FetchDataFromURL(String sortType,
                                 String baseURL, Context context) {
        URL url = null;

        if (sortType != null) {

            try {
                url = createUrl(sortType, baseURL);
                Log.e("url",url.toString());
            } catch (MalformedURLException e) {
                Log.e("URL create Error", e.toString());
            }

            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                String pageJSON = getPageFromStreamAsJSON(inputStream);

                ArrayList<ContentValues> contentValuesArrayList
                        = getDataFromJson(pageJSON);

                assert contentValuesArrayList != null;

                ContentValues[] contentValues = new ContentValues[contentValuesArrayList.size()];
                contentValuesArrayList.toArray(contentValues);

                int deleted = context.getContentResolver().delete(
                        MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,null,null);
                int inserted = context.getContentResolver().bulkInsert(
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

    private static URL createUrl(String sortType,String baseURL)
            throws MalformedURLException{

        final String API_KEY = "api_key";
        final String Key_PARAM = "0602c564bf48e42db8295fe9d5f8f4a7";

        Uri UriBuilder = Uri.parse(baseURL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(API_KEY,Key_PARAM)
                .build();

        return new URL(UriBuilder.toString());
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

    private static ArrayList<ContentValues> getDataFromJson
            (String pageJSON){
        String posterImagePath;
        String backdropImagePath;
        String originalTitle;
        String overView;
        String releasedDate;
        float voteAverage;
        int voteCount;
        int movieID;
        byte[] posterImageAsBytes;

        try {
            if (pageJSON != null) {
                JSONObject jsonObject = new JSONObject(pageJSON);
                JSONArray results = jsonObject.getJSONArray("results");

                ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();

                for (int movieIndex = 0; movieIndex < results.length(); movieIndex++) {
                    JSONObject movieJSON = results.getJSONObject(movieIndex);
                    ContentValues contentValues = new ContentValues();

                    /*
                     */
                      Log.e("movie n ",String.valueOf(movieIndex));

                    posterImagePath = movieJSON.getString("poster_path");
                    backdropImagePath = movieJSON.getString("backdrop_path");
                    originalTitle = movieJSON.getString("original_title");
                    overView = movieJSON.getString("overview");
                    releasedDate = movieJSON.getString("release_date");
                    voteAverage = (float) movieJSON.getDouble("vote_average");
                    voteCount = movieJSON.getInt("vote_count");
                    movieID = movieJSON.getInt("id");

                    if(!posterImagePath.equals("null"))
                        posterImageAsBytes = Utility.convertUrlImageToByteArray(posterImagePath);
                    else{
                        posterImageAsBytes = null;
                    }

                    /*
                     * Test
                     * Log.e("image",String.valueOf(posterImageAsBytes));
                     */
                    contentValues.put(
                            MovieContract.MovieMainEntry.MOVIE_ID_COLUMN,movieID);
                    contentValues.put(
                            MovieContract.MovieMainEntry.ORIGINAL_TITLE_COLUMN,originalTitle);
                    contentValues.put(
                            MovieContract.MovieMainEntry.OVERVIEW_MOVIE_COLUMN,overView);
                    contentValues.put(
                            MovieContract.MovieMainEntry.RELEASE_DATE_COLUMN,releasedDate);
                    contentValues.put(
                            MovieContract.MovieMainEntry.VOTE_RATING_COLUMN,voteAverage);
                    contentValues.put(
                            MovieContract.MovieMainEntry.VOTE_COUNT_COLUMN,voteCount);
                    contentValues.put(
                            MovieContract.MovieMainEntry.POSTER_IMAGE_COLUMN,posterImageAsBytes);
                    contentValues.put(
                            MovieContract.MovieMainEntry.BACKDROP_IMAGE_COLUMN,backdropImagePath);

                    

                    contentValuesArrayList.add(contentValues);
                }
                return contentValuesArrayList;
            }
        }catch (JSONException e){
            Log.e("ead",e.toString());
        }
        return null;
    }

}
