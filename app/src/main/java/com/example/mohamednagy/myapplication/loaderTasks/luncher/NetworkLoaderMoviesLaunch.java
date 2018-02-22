package com.example.mohamednagy.myapplication.loaderTasks.luncher;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkMoviesCallback;

/**
 * Created by mohamednagy on 11/8/2016.
 */
public class NetworkLoaderMoviesLaunch
    implements LoaderManager.LoaderCallbacks<Void> {

    private NetworkMoviesCallback<Void> mNetworkMoviesCallback ;
    private boolean dataChanged;

        public NetworkLoaderMoviesLaunch(NetworkMoviesCallback<Void> networkMoviesCallback, boolean dataChanged){
            this.mNetworkMoviesCallback = networkMoviesCallback;
            this.dataChanged =dataChanged;
            mNetworkMoviesCallback.launchNetworkLoader(this,dataChanged);
        }

        @Override
        public Loader<Void> onCreateLoader(int id, Bundle args) {
            return mNetworkMoviesCallback.createNetworkLoader();
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {
            mNetworkMoviesCallback.connectNetworkLoaderToCursorLoader(dataChanged);
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {
            mNetworkMoviesCallback = null;
        }

}
