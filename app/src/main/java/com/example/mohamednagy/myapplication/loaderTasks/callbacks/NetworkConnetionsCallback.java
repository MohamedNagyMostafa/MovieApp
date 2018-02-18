package com.example.mohamednagy.myapplication.loaderTasks.callbacks;

import android.support.v4.app.LoaderManager;
import android.support.annotation.Nullable;

import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkLoader;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public interface NetworkConnetionsCallback<T> {
    void launchNetworkLoader(LoaderManager.LoaderCallbacks<T> networkLoader, @Nullable Boolean dataChanged);
    DataNetworkLoader<T> createNetworkLoader();
}
