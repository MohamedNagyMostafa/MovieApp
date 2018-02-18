package com.example.mohamednagy.myapplication.loaderTasks.loaders;

import android.content.Context;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.downloadData.DownloadNetworkData;
import com.example.mohamednagy.myapplication.helperClasses.Utility;

/**
 * Created by mohamednagy on 10/8/2016.
 */
public class DataNetworkMovieLoader extends DataNetworkLoader<Void> {
    public static final int DATA_NETWORK_LOADER_ID = 1;
    private String sortType ;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public DataNetworkMovieLoader(Context context, String sortType) {
        super(context);
        this.sortType = sortType;
    }

    @Override
    public Void loadInBackground() {
        if(!sortType.equals(getContext().getString(R.string.settings_sort_favorite)))
            DownloadNetworkData.FetchMovieDataFromURL(sortType,getContext());
        Utility.LoaderHandler.setLoaderState(false);
        return null;
    }

}
