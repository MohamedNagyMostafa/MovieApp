package com.example.mohamednagy.myapplication.downloadData;

import android.content.ContentValues;
import android.util.Log;

import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class ParserJSON {
    private static final String RESULT_NODE = "results";
    private static final String POSTER_PATH_NODE = "poster_path";
    private static final String BACKDROP_PATH_NODE = "backdrop_path";
    private static final String ORIGINAL_TITLE_NODE = "original_title";
    private static final String OVERVIEW_NODE = "overview";
    private static final String RELEASE_DATE_NODE = "release_date";
    private static final String VOTE_AVERAGE_NODE = "vote_average";
    private static final String VOTE_COUNT_NODE = "vote_count";
    private static final String ID_NODE = "id";

    private static final String NULL = "null";

    public static ArrayList<ContentValues> getMovieDataFromJson
            (String pageJSON){
        String posterImagePath, backdropImagePath, originalTitle, overView, releasedDate;
        float voteAverage;
        int voteCount, movieID;
        byte[] posterImageAsBytes;

        try {
            if (pageJSON != null) {
                JSONObject jsonObject = new JSONObject(pageJSON);
                JSONArray results = jsonObject.getJSONArray(RESULT_NODE);

                ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();

                for (int movieIndex = 0; movieIndex < results.length(); movieIndex++) {
                    JSONObject movieJSON = results.getJSONObject(movieIndex);
                    ContentValues contentValues = new ContentValues();

                    posterImagePath = movieJSON.optString(POSTER_PATH_NODE);
                    backdropImagePath = movieJSON.optString(BACKDROP_PATH_NODE);
                    originalTitle = movieJSON.optString(ORIGINAL_TITLE_NODE);
                    overView = movieJSON.optString(OVERVIEW_NODE);
                    releasedDate = movieJSON.optString(RELEASE_DATE_NODE);
                    voteAverage = (float) movieJSON.optDouble(VOTE_AVERAGE_NODE);
                    voteCount = movieJSON.optInt(VOTE_COUNT_NODE);
                    movieID = movieJSON.optInt(ID_NODE);

                    if(!posterImagePath.equals(NULL))
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

    public static List<Review> getReviewDataFromJson(String pageJSON){
                return null;
    }
}
