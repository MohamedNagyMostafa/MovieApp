package com.example.mohamednagy.myapplication.loaderTasks.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class DataNetworkLoader<T> extends AsyncTaskLoader<T> {

    public DataNetworkLoader(Context context) {
        super(context);
    }

    @Override
    public T loadInBackground() {
        return null;
    }
}
