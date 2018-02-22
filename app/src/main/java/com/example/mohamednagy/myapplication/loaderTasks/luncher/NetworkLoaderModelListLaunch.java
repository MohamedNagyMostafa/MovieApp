package com.example.mohamednagy.myapplication.loaderTasks.luncher;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkModelsListCallback;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class NetworkLoaderModelListLaunch<T> implements LoaderManager.LoaderCallbacks<List<T>> {
    private NetworkModelsListCallback<List<T>> mNetworkModelsListCallback;

    public NetworkLoaderModelListLaunch(NetworkModelsListCallback<List<T>> networkModelsListCallback){
        mNetworkModelsListCallback = networkModelsListCallback;
    }

    public void execute(){
        mNetworkModelsListCallback.launchNetworkLoader(this,null);
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        return mNetworkModelsListCallback.createNetworkLoader();
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        mNetworkModelsListCallback.updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        mNetworkModelsListCallback = null;
    }
}
