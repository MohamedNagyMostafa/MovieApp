package com.example.mohamednagy.myapplication.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by mohamednagy on 11/5/2016.
 */
public class MovieProvider extends ContentProvider{

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int MOVIE = 100;

    private static final int MOVIE_WITH_ID = 101;

    private static final int MOVIE_FAVORITE = 110;

    private static final int MOVIE_FAVORITE_WITH_ID = 111;

    private MovieDbHelper movieDbHelper;



     private static UriMatcher buildUriMatcher(){

         final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
         final String MOVIE_PATH =
                 MovieContract.MovieMainEntry.TABLE_NAME;
         final String MOVIE_WITH_ID_PATH =
                 MovieContract.MovieMainEntry.TABLE_NAME + "/#";
         final String MOVIE_FAVORITE_PATH =
                 MovieContract.FavoriteMovieEntry.TABLE_NAME;
         final String MOVIE_FAVORITE_WITH_ID_PATH =
                 MovieContract.FavoriteMovieEntry.TABLE_NAME + "/#";

         MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,MOVIE_PATH,MOVIE);
         MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,MOVIE_WITH_ID_PATH,MOVIE_WITH_ID);
         MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,MOVIE_FAVORITE_PATH,MOVIE_FAVORITE);
         MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,MOVIE_FAVORITE_WITH_ID_PATH,MOVIE_FAVORITE_WITH_ID);

        return MATCHER;
    }



    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match){
            case MOVIE :
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieMainEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_WITH_ID :
                cursor = getQueryCursorWithId(
                        uri, projection,
                        sortOrder, MovieContract.MovieMainEntry.TABLE_NAME,
                        MovieContract.MovieMainEntry._ID);
                break;

            case MOVIE_FAVORITE :
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_FAVORITE_WITH_ID :
                cursor = getQueryCursorWithId(
                        uri,projection,
                        sortOrder, MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        MovieContract.FavoriteMovieEntry._ID);
                break;

            default :
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    private Cursor getQueryCursorWithId(
            Uri uri,String[] projection,
            String sortOrder,String tableName,
            String _idColumn){

        long id = getIdFromUri(uri);

        String selection = _idColumn + "=?";

        String[] selectionArgs = {String.valueOf(id)};

        return movieDbHelper.getReadableDatabase().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public static long getIdFromUri(Uri uri){
        return ContentUris.parseId(uri);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int matcher = uriMatcher.match(uri);

        switch (matcher){

            case MOVIE :
                return MovieContract.MovieMainEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID :
                return MovieContract.MovieMainEntry.CONTENT_ITEM_TYPE;
            case MOVIE_FAVORITE :
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE ;
            case MOVIE_FAVORITE_WITH_ID :
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            default :
                throw new UnsupportedOperationException("Unknown Uri : " + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int match = uriMatcher.match(uri);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        long columnId = 0 ;
        Uri uriInsert;

        switch (match){

            case MOVIE :
                db.beginTransaction();
                columnId = db.insert(
                        MovieContract.MovieMainEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                db.setTransactionSuccessful();
                break;
            case MOVIE_FAVORITE :
                db.beginTransaction();
                columnId = db.insert(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                db.setTransactionSuccessful();
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        if(columnId  > 0 ){
            uriInsert = MovieContract.MovieMainEntry.buildMovieContentUri(columnId);
            /*
             * Test
             * Log.e("col ",String.valueOf(columnId));
             */

        }

        else{
            throw new android.database.SQLException("Failed to insert row into " + uri + " Which Reqiured :" + match);
        }

        db.endTransaction();
        getContext().getContentResolver().notifyChange(uri,null);

        return uriInsert;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = uriMatcher.match(uri);
        int counter = 0;
        SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        switch (match){
            case MOVIE :
                for(ContentValues contentValues : values){
                    long id = sqLiteDatabase.insert(
                            MovieContract.MovieMainEntry.TABLE_NAME,null,contentValues);
                    counter++;
                }
                sqLiteDatabase.setTransactionSuccessful();
                break;
            case MOVIE_FAVORITE :
                for(ContentValues contentValues : values){
                    long id = sqLiteDatabase.insert(
                            MovieContract.FavoriteMovieEntry.TABLE_NAME,null,contentValues);
                    counter++;
                }
                sqLiteDatabase.setTransactionSuccessful();
        }
        sqLiteDatabase.endTransaction();
        return counter;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int match = uriMatcher.match(uri);
        int columnId ;

        switch(match){

            case MOVIE :
                columnId = movieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieMainEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            case MOVIE_WITH_ID :
                columnId = deleteMovieWithId(
                        uri, MovieContract.MovieMainEntry.TABLE_NAME,
                        MovieContract.MovieMainEntry._ID);
                break;

            case MOVIE_FAVORITE :
                columnId = movieDbHelper.getWritableDatabase().delete(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            case MOVIE_FAVORITE_WITH_ID :
                columnId = deleteMovieWithId(
                        uri, MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        MovieContract.FavoriteMovieEntry._ID);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        if(columnId != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return columnId;
    }

    private int deleteMovieWithId(Uri uri,String tableName,String _idColumn){

        String selection = _idColumn +"=?";
        String[] selectionArgs = {String.valueOf(ContentUris.parseId(uri))};

        return movieDbHelper.getWritableDatabase().delete(
                tableName,
                selection,
                selectionArgs
        );
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int match = uriMatcher.match(uri);
        int columnId;

        switch (match){

            case MOVIE :
                columnId = movieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieMainEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );

                break;

            case MOVIE_WITH_ID :
                columnId = updateMovieWithId(uri,contentValues,
                        MovieContract.MovieMainEntry.TABLE_NAME,
                        MovieContract.MovieMainEntry._ID);
                break;

            case MOVIE_FAVORITE:
                columnId = movieDbHelper.getWritableDatabase().update(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );

                break;

            case MOVIE_FAVORITE_WITH_ID :
                columnId = updateMovieWithId(uri,contentValues,
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        MovieContract.FavoriteMovieEntry._ID);
                break;

            default:
                throw new UnsupportedOperationException("URI unknown : " + uri);
        }
        if(columnId != 0)
            getContext().getContentResolver().notifyChange(uri,null);

        return columnId;
    }

    private int updateMovieWithId(Uri uri , ContentValues contentValues,
                                  String tableName, String _idColumn){

        String selection = _idColumn + "=?";
        String []selectionArgs = {String.valueOf(ContentUris.parseId(uri))};

        return movieDbHelper.getWritableDatabase().update(
                tableName,
                contentValues,
                selection,
                selectionArgs
        );
    }

}
