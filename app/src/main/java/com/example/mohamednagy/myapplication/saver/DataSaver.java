package com.example.mohamednagy.myapplication.saver;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.example.mohamednagy.myapplication.helperClasses.Utility;

/**
 * Created by Mohamed Nagy on 2/16/2018.
 */

public class DataSaver {

    public static class DetailsActivityData{

        public final String DATA_SAVER_ID = "details sc";
        public final String DATA_IMAGE_SAVER_ID = "details sc image";
        public final String DATA_RECYCLE_VIEW_POSITION_ID = "recycle position sc";
        public final String DATA_RECYCLE_VIEW_ITEMS_ID = "recycle items sc";

        public static final int MOVIE_ORIGINAL_TITLE = 0;
        public static final int MOVIE_ID =1;
        public static final int MOVIE_OVERVIEW = 2;
        public static final int MOVIE_RELEASE_DATE =3;
        public static final int MOVIE_VOTE_COUNT = 4;
        public static final int MOVIE_VOTE_RATE =5;

        private static final int DATA_SIZE = 7;

        private String[] mMovieData;
        private Bitmap mImageData;

        public DetailsActivityData(){
            init();
        }

        private void init(){
            mMovieData = new String[DATA_SIZE];
        }

        public void setMovieData(String originalTitle,String id,
                                     String overview, String releaseDate,
                                     String voteCount, String voteRate){
            mMovieData[MOVIE_ORIGINAL_TITLE] = originalTitle;
            mMovieData[MOVIE_ID] = id;
            mMovieData[MOVIE_OVERVIEW] = overview;
            mMovieData[MOVIE_RELEASE_DATE] = releaseDate;
            mMovieData[MOVIE_VOTE_COUNT] = voteCount;
            mMovieData[MOVIE_VOTE_RATE] = voteRate;

        }

        public void setMovieData(String[] movieData){
            this.mMovieData = movieData;
        }

        public void setImageData(Bitmap imageData) {
            this.mImageData = imageData;
        }

        public String[] getMovieData() {
            return mMovieData;
        }

        public byte[] getImageData(){
            return  Utility.DataTypeHandling.converBitmapToByteArray(mImageData);
        }
    }

    public static class MainActivityData{
        public static final int NO_VALUE = 0;
        public static final String GRID_VIEW_STATE_ID = "state";
        private Parcelable mGridViewState;

        public MainActivityData(){}

        public void setGridViewState(Parcelable gridViewState){
            mGridViewState = gridViewState;
        }
        // null to prevent any error during changing app settings.
        public Parcelable getSavingStateAndNull(){
            Parcelable restoreState = mGridViewState;
            mGridViewState = null;
            return restoreState;
        }
    }
}
