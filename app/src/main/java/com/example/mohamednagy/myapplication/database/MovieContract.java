package com.example.mohamednagy.myapplication.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohamednagy on 11/4/2016.
 */
public class MovieContract {

    static final String CONTENT_AUTHORITY = "com.example.mohamednagy.myapplication";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String AUTO_INCREMENT = " AUTOINCREMENT";

    private static final String NOT_NULL = " NOT_NULL";

    private static final String INTEGER = " INTEGER";

    private static final String DEFAULT = " DEFAULT";

    private static final String TEXT = " TEXT";

    private static final String BLOB = " BLOB";

    private static final String REAL = " REAL";

    private static final String UNIQUE = "UNIQUE";

    private static final String  ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE";

    private static final String PRIMARY_KEY = " PRIMARY KEY";

    public static final class MovieMainEntry implements BaseColumns{

        public static final String TABLE_NAME = "Movie_Main";

        public static final Uri MOVIE_MAIN_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        // TABLE COLUMNS
        public static final String POSTER_IMAGE_COLUMN = "poster";

        public static final String BACKDROP_IMAGE_COLUMN = "backdrop";

        public static final String RELEASE_DATE_COLUMN = "date";

        public static final String MOVIE_ID_COLUMN = "id";

        public static final String ORIGINAL_TITLE_COLUMN = "title";

        public static final String VOTE_COUNT_COLUMN = "vote";

        public static final String VOTE_RATING_COLUMN = "rating";

        public static final String FAVORITE_MOVIE_COLUMN= "favorite";

        public static final String OVERVIEW_MOVIE_COLUMN = "overview";

        public static final int NUMBER_OF_COLUMNS =  10;

        public static final int IS_FAVORITE= 100;

        public static final int IS_NOT_FAVORITE = 101;

        //TABLE BUILD COMPONENTS
        static final String CREATE_MOVIE_MAIN_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" + _ID + INTEGER + PRIMARY_KEY + AUTO_INCREMENT + "," +
                POSTER_IMAGE_COLUMN + BLOB + NOT_NULL + "," +
                BACKDROP_IMAGE_COLUMN + TEXT + "," +
                RELEASE_DATE_COLUMN + TEXT + NOT_NULL + "," +
                MOVIE_ID_COLUMN + INTEGER + NOT_NULL + "," +
                ORIGINAL_TITLE_COLUMN + TEXT + NOT_NULL + "," +
                OVERVIEW_MOVIE_COLUMN + TEXT + NOT_NULL + "," +
                VOTE_COUNT_COLUMN + INTEGER + NOT_NULL + "," +
                VOTE_RATING_COLUMN + REAL + NOT_NULL + "," +
                FAVORITE_MOVIE_COLUMN + INTEGER + NOT_NULL +  DEFAULT + " " +
                String.valueOf(IS_NOT_FAVORITE)+"," +
                UNIQUE +"("+ORIGINAL_TITLE_COLUMN+")"+ON_CONFLICT_REPLACE + ");";

        static Uri buildMovieContentUri(long id){
            return ContentUris.withAppendedId(MOVIE_MAIN_CONTENT_URI,id);
        }

    }

    public static final class FavoriteMovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "favorite_movies";

        public static final Uri MOVIE_FAVORITE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        // TABLE COLUMNS
        public static final String POSTER_IMAGE_COLUMN = "poster";

        public static final String BACKDROP_IMAGE_COLUMN = "backdrop";

        public static final String RELEASE_DATE_COLUMN = "date";

        public static final String MOVIE_ID_COLUMN = "id";

        public static final String ORIGINAL_TITLE_COLUMN = "title";

        public static final String VOTE_COUNT_COLUMN = "vote";

        public static final String VOTE_RATING_COLUMN = "rating";

        public static final String OVERVIEW_MOVIE_COLUMN = "overview";

        //TABLE BUILD COMPONENTS
        static final String CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" + _ID + INTEGER + PRIMARY_KEY + AUTO_INCREMENT + "," +
                POSTER_IMAGE_COLUMN + BLOB  + "," +
                BACKDROP_IMAGE_COLUMN + TEXT + NOT_NULL + "," +
                RELEASE_DATE_COLUMN + TEXT + NOT_NULL + "," +
                MOVIE_ID_COLUMN + INTEGER + NOT_NULL + "," +
                ORIGINAL_TITLE_COLUMN + TEXT + NOT_NULL + "," +
                OVERVIEW_MOVIE_COLUMN + TEXT + NOT_NULL + "," +
                VOTE_COUNT_COLUMN + INTEGER + NOT_NULL + "," +
                VOTE_RATING_COLUMN + REAL + NOT_NULL + "," +
                UNIQUE +"("+ORIGINAL_TITLE_COLUMN+")"+ON_CONFLICT_REPLACE + ");";
    }
}
