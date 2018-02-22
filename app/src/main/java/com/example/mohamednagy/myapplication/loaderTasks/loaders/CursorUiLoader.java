package com.example.mohamednagy.myapplication.loaderTasks.loaders;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkMoviesCallback;

/**
 * Created by mohamednagy on 11/8/2016.
 */
public class CursorUiLoader implements LoaderManager.LoaderCallbacks<Cursor>{

    private NetworkMoviesCallback loaders;

    public CursorUiLoader(NetworkMoviesCallback loaders,boolean dataChanged){
        this.loaders = loaders;
        loaders.launchCursorLoader(this,dataChanged);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loaders.createCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loaders.updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loaders.updateAdapter(null);
    }
}
