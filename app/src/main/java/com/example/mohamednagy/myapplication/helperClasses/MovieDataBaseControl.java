package com.example.mohamednagy.myapplication.helperClasses;

/**
 * Created by mohamednagy on 11/7/2016.
 */

import com.example.mohamednagy.myapplication.database.MovieContract;

/**
 * Use to set database columns and connected them with integer id
 * Create Projection for all columns in tables
 */
public class MovieDataBaseControl {

    // MainMovie table
    public static final String []PROJECTION = {
            MovieContract.MovieMainEntry.TABLE_NAME + "." + MovieContract.MovieMainEntry._ID,
            MovieContract.MovieMainEntry.MOVIE_ID_COLUMN,
            MovieContract.MovieMainEntry.ORIGINAL_TITLE_COLUMN,
            MovieContract.MovieMainEntry.OVERVIEW_MOVIE_COLUMN,
            MovieContract.MovieMainEntry.FAVORITE_MOVIE_COLUMN,
            MovieContract.MovieMainEntry.VOTE_COUNT_COLUMN,
            MovieContract.MovieMainEntry.POSTER_IMAGE_COLUMN,
            MovieContract.MovieMainEntry.RELEASE_DATE_COLUMN,
            MovieContract.MovieMainEntry.VOTE_RATING_COLUMN,
            MovieContract.MovieMainEntry.BACKDROP_IMAGE_COLUMN
    };

    // Columns keys
    public static final int _ID_MAIN_COLUMN = 0;
    public static final int MOVIE_MAIN_ID = 1;
    public static final int MOVIE_MAIN_ORIGINAL_TITLE = 2;
    public static final int MOVIE_MAIN_OVERVIEW = 3;
    public static final int MOVIE_MAIN_FAVORITE = 4;
    public static final int MOVIE_MAIN_VOTE_COUNT = 5;
    public static final int MOVIE_MAIN_POSTER_IMAGE = 6;
    public static final int MOVIE_MAIN_RELEASE_DATE = 7;
    public static final int MOVIE_MAIN_VOTE_RATING = 8;
    public static final int MOVIE_MAIN_BACKDROP_IMAGE = 9;

    // FavoriteMovie table
    public static final String []PROJECTION_FAVORITE_TABLE = {
            MovieContract.FavoriteMovieEntry.TABLE_NAME + "." + MovieContract.FavoriteMovieEntry._ID,
            MovieContract.FavoriteMovieEntry.MOVIE_ID_COLUMN,
            MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN,
            MovieContract.FavoriteMovieEntry.OVERVIEW_MOVIE_COLUMN,
            MovieContract.FavoriteMovieEntry.VOTE_COUNT_COLUMN,
            MovieContract.FavoriteMovieEntry.POSTER_IMAGE_COLUMN,
            MovieContract.FavoriteMovieEntry.RELEASE_DATE_COLUMN,
            MovieContract.FavoriteMovieEntry.VOTE_RATING_COLUMN,
            MovieContract.FavoriteMovieEntry.BACKDROP_IMAGE_COLUMN
    };

    // Columns keys
    public static final int _ID__FAVORITE_COLUMN = 0;
    public static final int FAVORITE_MOVIE_ID = 1;
    public static final int FAVORITE_MOVIE_ORIGINAL_TITLE = 2;
    public static final int FAVORITE_MOVIE_OVERVIEW = 3;
    public static final int FAVORITE_MOVIE_VOTE_COUNT = 4;
    public static final int FAVORITE_MOVIE_POSTER_IMAGE = 5;
    public static final int FAVORITE_MOVIE_RELEASE_DATE = 6;
    public static final int FAVORITE_MOVIE_VOTE_RATING = 7;
    public static final int FAVORITE_MOVIE_BACKDROP_IMAGE = 8;


    public static int getMovieOriginalTitle(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_ORIGINAL_TITLE :
                FAVORITE_MOVIE_ORIGINAL_TITLE;
    }

    public static int getMovieOverview(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_OVERVIEW :
                FAVORITE_MOVIE_OVERVIEW;
    }

    public static int getMovieReleaseDate(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_RELEASE_DATE :
                FAVORITE_MOVIE_RELEASE_DATE;
    }

    public static int getMovieBackdropImage(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_BACKDROP_IMAGE :
                FAVORITE_MOVIE_BACKDROP_IMAGE;
    }

    public static int getMoviePosterImage(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_POSTER_IMAGE :
                FAVORITE_MOVIE_POSTER_IMAGE;
    }

    public static int getMovieId(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_ID :
                FAVORITE_MOVIE_ID;
    }

    public static int getMovieVoteCount(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_VOTE_COUNT :
                FAVORITE_MOVIE_VOTE_COUNT;
    }

    public static int getMovieVoteRating(int size){
        return (size == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                MOVIE_MAIN_VOTE_RATING :
                FAVORITE_MOVIE_VOTE_RATING;
    }

    public static int getMovieMainFavorite(){
        return MOVIE_MAIN_FAVORITE;
    }

}
