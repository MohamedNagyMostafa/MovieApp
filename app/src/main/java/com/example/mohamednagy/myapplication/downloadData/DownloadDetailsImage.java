package com.example.mohamednagy.myapplication.downloadData;

import android.graphics.Bitmap;
import android.support.v4.app.LoaderManager;

import com.example.mohamednagy.myapplication.loaderTasks.DownloadImageLoader;

/**
 * Created by mohamednagy on 10/16/2016.
 * Interface which have functions that used within download backdrop
 * image process .
 */
public interface DownloadDetailsImage {

    void launchImageLoader(LoaderManager.LoaderCallbacks loaderCallbacks);

    DownloadImageLoader createImageLoader();

    Bitmap downloadImage();

    void setImageToView(Bitmap imageAsBitmap);
}
