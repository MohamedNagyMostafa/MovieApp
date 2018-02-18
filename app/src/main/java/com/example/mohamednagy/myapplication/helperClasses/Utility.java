package com.example.mohamednagy.myapplication.helperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.mohamednagy.myapplication.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamednagy on 11/11/2016.
 */
public class Utility {

    /**
     * Extras Keys
     */
    public static class ExtrasHandler{
        public static final String URI_EXTRA_KEY = "uri";
        public static final String MOVIE_EXTRA_KEY = "movie id";
    }

    public static class PaneHandler{
        private static int paneUi;
        public static final int ONE_PANE_UI = 100;
        public static final int TWO_PANE_UI = 101;

        // Pane state Controller
        public static void setCurrentPaneUi(int currentPaneUi){
            paneUi = currentPaneUi;
        }

        public static int getCurrentPaneUi(){
            return paneUi;
        }

    }

    public static void openLinkOnBrowser(String url){

    }
    public static class LoaderHandler{
        private static boolean loaderState = false;

        public static boolean getLoaderState(){
            return loaderState;
        }

        public static void setLoaderState(boolean currentLoaderState){
            loaderState = currentLoaderState;
        }
    }
    public static class DataTypeHandling{
        public static String optText(String string){
            return (string == null)?"":string;
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

        public static byte[] convertUrlImageToByteArray(String url)throws NullPointerException{

            InputStream inputStream = null;
            try {
                inputStream =
                        UrlBuilder.createUrlImage(url).openStream();
            }catch (IOException e){
                Log.e("ee",e.toString());
            }
            Bitmap imageAsBitmap = BitmapFactory.decodeStream(inputStream);


            return converBitmapToByteArray(imageAsBitmap);
        }

        public static Bundle convertDataToBundle(String data){
            Bundle dataBundle = new Bundle();
            dataBundle.putString(ExtrasHandler.MOVIE_EXTRA_KEY, data);

            return dataBundle;
        }

    }


    public static class UrlBuilder{
        private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        private static final String REVIEW_PATH = "reviews";
        private static final String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500/";

        // Build correct url for image path
        public static URL createUrlImage(String imageUrl) {
            URL url = null;
            try{
                url = new URL( BASE_URL_IMAGE + imageUrl);
            }catch (MalformedURLException e){
                Log.e("eror",e.toString());
            }
            return url;
        }

        // Build correct Movies url
        public static URL createMoviesUrl(String sortType)
                throws MalformedURLException{

            final String API_KEY = "api_key";
            // More secure.
            final String Key_PARAM = BuildConfig.MOVIE_API;

            Uri UriBuilder = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(sortType)
                    .appendQueryParameter(API_KEY,Key_PARAM)
                    .build();

            return new URL(UriBuilder.toString());
        }

        public static URL createReviewsUrl(String movieId) throws MalformedURLException {
            final String API_KEY = "api_key";
            // More secure.
            final String Key_PARAM = BuildConfig.MOVIE_API;

            Uri UriBuilder = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(REVIEW_PATH)
                    .appendQueryParameter(API_KEY,Key_PARAM)
                    .build();

            return new URL(UriBuilder.toString());
        }

    }
}
