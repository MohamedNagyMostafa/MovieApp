package com.example.mohamednagy.myapplication.helperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamednagy on 11/11/2016.
 */
public class Utility {

    private static boolean loaderState = false;


    public static final int ONE_PANE_UI = 100;
    public static final int TWO_PANE_UI = 101;
    public static final String URI_EXTRA_KEY = "uri";

    private static int paneUi;

    // Pane state Controller
    public static void setCurrentPaneUi(int currentPaneUi){
        paneUi = currentPaneUi;
    }

    public static int getCurrentPaneUi(){
        return paneUi;
    }

    // Build correct url for image path
    public static URL createUrlImage(String imageUrl) {
        final String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500/";
        URL url = null;
        try{
            url = new URL( BASE_URL_IMAGE + imageUrl);
        }catch (MalformedURLException e){
            Log.e("eror",e.toString());
        }
        return url;
    }


    public static byte[] convertUrlImageToByteArray(String url)throws NullPointerException{

        InputStream inputStream = null;
        try {
            inputStream =
                    createUrlImage(url).openStream();
        }catch (IOException e){
            Log.e("ee",e.toString());
        }
        Bitmap imageAsBitmap = BitmapFactory.decodeStream(inputStream);


        return converBitmapToByteArray(imageAsBitmap);
    }

    public static Bitmap convertByteArrayToBitmap(byte[] imageAsBytes){
        return BitmapFactory.decodeByteArray(imageAsBytes , 0, imageAsBytes.length);
    }

    public static byte[] converBitmapToByteArray(Bitmap imageAsBitmap){
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        imageAsBitmap.compress(Bitmap.CompressFormat.PNG,100,
                byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean getLoaderState(){
        return loaderState;
    }

    public static void setLoaderState(boolean currentLoaderState){
        loaderState = currentLoaderState;
    }

    public static String optText(String string){
        return (string == null)?"":string;
    }
}
