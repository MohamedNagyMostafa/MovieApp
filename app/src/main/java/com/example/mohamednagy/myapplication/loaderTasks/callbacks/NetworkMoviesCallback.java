package com.example.mohamednagy.myapplication.loaderTasks.callbacks;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public interface NetworkMoviesCallback<T> extends NetworkConnetionsCallback<T> {
    void connectNetworkLoaderToCursorLoader(boolean dataChanged);
    void launchCursorLoader(LoaderManager.LoaderCallbacks<Cursor> cursorLoader, boolean datachanged);
    CursorLoader createCursorLoader();
    void updateAdapter(Cursor cursor);
}
