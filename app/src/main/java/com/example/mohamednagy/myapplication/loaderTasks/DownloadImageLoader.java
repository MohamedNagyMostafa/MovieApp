package com.example.mohamednagy.myapplication.loaderTasks;

import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.mohamednagy.myapplication.downloadData.DownloadDetailsImage;

/**
 * Created by mohamednagy on 10/8/2016.
 */
public  class DownloadImageLoader extends AsyncTaskLoader<Bitmap> {

    private DownloadDetailsImage downloadDetailsImageObject;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public DownloadImageLoader(Context context, DownloadDetailsImage downloadDetailsImageObject) {
        super(context);
        this.downloadDetailsImageObject = downloadDetailsImageObject;
    }

    @Override
    public Bitmap loadInBackground() {
        return downloadDetailsImageObject.downloadImage();
    }

}
