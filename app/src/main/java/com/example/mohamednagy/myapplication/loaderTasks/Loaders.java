package com.example.mohamednagy.myapplication.loaderTasks;

import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

/**
 * Created by mohamednagy on 11/8/2016.
 */
public interface  Loaders {
    void launchNetworkLoader(LoaderManager.LoaderCallbacks<Void> networkLoader,boolean dataChanged);
    DataNetworkLoader createNetworkLoader();
    void connectNetworkLoaderToCursorLoader(boolean dataChanged);

    void launchCursorLoader(LoaderManager.LoaderCallbacks<Cursor> cursorLoader,boolean datachanged);
    CursorLoader createCursorLoader();
    void updateAdapter(Cursor cursor);
}
