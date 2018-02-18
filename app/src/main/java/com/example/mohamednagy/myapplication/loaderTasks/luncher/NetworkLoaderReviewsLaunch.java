package com.example.mohamednagy.myapplication.loaderTasks.luncher;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkConnetionsCallback;
import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkReviewsCallback;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class NetworkLoaderReviewsLaunch implements LoaderManager.LoaderCallbacks<List<Review>> {
    private NetworkReviewsCallback<List<Review>> mNetworkReviewsCallback;

    public NetworkLoaderReviewsLaunch(NetworkReviewsCallback<List<Review>> networkReviewsCallback){
        mNetworkReviewsCallback =  networkReviewsCallback;
        mNetworkReviewsCallback.launchNetworkLoader(this,null);
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return mNetworkReviewsCallback.createNetworkLoader();
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        mNetworkReviewsCallback.updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
        mNetworkReviewsCallback = null;
    }
}
