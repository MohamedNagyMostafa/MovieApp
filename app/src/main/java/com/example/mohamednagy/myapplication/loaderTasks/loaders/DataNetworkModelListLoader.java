package com.example.mohamednagy.myapplication.loaderTasks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.mohamednagy.myapplication.downloadData.DownloadNetworkData;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class DataNetworkModelListLoader<T> extends DataNetworkLoader<List<T>> {
    public static final int REVIEW_LOADER_ID = 0x02;
    public static final int TRAILERS_LOADER_ID = 0x03;

    private String mMovieId;

    public DataNetworkModelListLoader(Context context, String movieId) {
        super(context);
        Log.e("dddddddddddddddddddd","aaaaaaaaaaaaaaaaaaaaaaaaaaa " + movieId);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    @SuppressWarnings("unchecked")
    // As the T type while be equal to the dataType which will return from FetchxxxDataFromURL
    // If there's a suggestion for this situation, I will thankful.
    public List<T> loadInBackground() {
        Log.e("enter load","done");
        switch (getId()){
            case REVIEW_LOADER_ID:
                return (List<T>) DownloadNetworkData.FetchReviewDataFromURL(mMovieId);
            case TRAILERS_LOADER_ID:
                return (List<T>) DownloadNetworkData.FetchTrailerDataFromURL(mMovieId);
        }

        return null;
    }
}
