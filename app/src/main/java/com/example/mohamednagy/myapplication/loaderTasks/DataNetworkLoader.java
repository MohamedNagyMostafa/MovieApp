package com.example.mohamednagy.myapplication.loaderTasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.downloadData.DownloadNetworkData;
import com.example.mohamednagy.myapplication.helperClasses.Utility;

/**
 * Created by mohamednagy on 10/8/2016.
 */
public class DataNetworkLoader extends AsyncTaskLoader<Void> {

    private  String sortType ;
    private static final String BASE_URL =
            "https://api.themoviedb.org/3/discover/movie?";

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public DataNetworkLoader(Context context,String sortType) {
        super(context);
        this.sortType = sortType;
    }

    @Override
    public Void loadInBackground() {
        if(!sortType.equals(getContext().getString(R.string.settings_sort_favorite)))
            DownloadNetworkData.FetchDataFromURL(sortType,BASE_URL,getContext());
        Utility.setLoaderState(false);
        return null;
    }

}
