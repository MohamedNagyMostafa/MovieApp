package com.example.mohamednagy.myapplication.loaderTasks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.mohamednagy.myapplication.downloadData.DownloadDetailsImage;

/**
 * Created by mohamednagy on 11/10/2016.
 */
public class ImageDownloadLaunch implements LoaderManager.LoaderCallbacks<Bitmap> {

    private DownloadDetailsImage downloadDetailsImage;

    public ImageDownloadLaunch (DownloadDetailsImage downloadDetailsImage){
        this.downloadDetailsImage = downloadDetailsImage;
        downloadDetailsImage.launchImageLoader(this);
    }
    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        return downloadDetailsImage.createImageLoader();
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        downloadDetailsImage.setImageToView(data);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        downloadDetailsImage = null;
    }
}
