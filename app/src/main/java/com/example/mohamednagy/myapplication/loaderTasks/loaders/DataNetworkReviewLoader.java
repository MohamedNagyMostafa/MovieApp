package com.example.mohamednagy.myapplication.loaderTasks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.mohamednagy.myapplication.downloadData.DownloadNetworkData;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class DataNetworkReviewLoader extends DataNetworkLoader<List<Review>> {
    public static final int REVIEW_LOADER_ID = 2;
    private String mMovieId;

    public DataNetworkReviewLoader(Context context, String movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        return DownloadNetworkData.FetchReviewDataFromURL(mMovieId,getContext());
    }
}
