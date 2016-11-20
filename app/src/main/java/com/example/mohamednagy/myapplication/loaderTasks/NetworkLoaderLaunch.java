package com.example.mohamednagy.myapplication.loaderTasks;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * Created by mohamednagy on 11/8/2016.
 */
public class NetworkLoaderLaunch
    implements LoaderManager.LoaderCallbacks<Void>{

    private Loaders loaders ;
    private boolean dataChanged;

        public NetworkLoaderLaunch(Loaders loaders,boolean dataChanged){
            this.loaders = loaders;
            this.dataChanged =dataChanged;
            loaders.launchNetworkLoader(this,dataChanged);
        }

        @Override
        public Loader<Void> onCreateLoader(int id, Bundle args) {
            return loaders.createNetworkLoader();
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {
            loaders.connectNetworkLoaderToCursorLoader(dataChanged);
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {
            loaders = null;
        }

}
