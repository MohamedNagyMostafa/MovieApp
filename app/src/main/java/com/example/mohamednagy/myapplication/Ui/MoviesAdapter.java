package com.example.mohamednagy.myapplication.Ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.MovieDataBaseControl;
import com.example.mohamednagy.myapplication.helperClasses.Utility;

/**
 * Created by mohamednagy on 10/8/2016.
 */
public class MoviesAdapter extends CursorAdapter {

    private Context mContext;

    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        /*
         * Test
         * Log.e("ui", "done");
         */
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycle_movies, viewGroup, false);
        ScreenViewHolder.MainViewHolder.AdapterViewHolder adapterViewHolder = new ScreenViewHolder.MainViewHolder.AdapterViewHolder(view);

        view.setTag(adapterViewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ScreenViewHolder.MainViewHolder.AdapterViewHolder viewHolder =
                (ScreenViewHolder.MainViewHolder.AdapterViewHolder) view.getTag();

        int columnsCount = cursor.getColumnCount();

        /*
         * Test
         * Log.e("columns count",String.valueOf(columnsCount));
         */
        final String MOVIE_RELEASE_DATA_DATABASE =
                cursor.getString(MovieDataBaseControl.getMovieReleaseDate(columnsCount));
        final float MOVIE_RATING_DATABASE =
                cursor.getFloat(MovieDataBaseControl.getMovieVoteRating(columnsCount));
        final byte[] MOVIE_POSTER_IMAGE_DATABASE =
                cursor.getBlob(MovieDataBaseControl.getMoviePosterImage(columnsCount));
        final String ORIGINAL_TITLE_MOVIE =
                cursor.getString(MovieDataBaseControl.getMovieOriginalTitle(columnsCount));


        if(MOVIE_POSTER_IMAGE_DATABASE != null)
            viewHolder.MOVIE_POSTER_IMAGE.setImageBitmap(
                    Utility.convertByteArrayToBitmap(MOVIE_POSTER_IMAGE_DATABASE)
            );
        else{
            viewHolder.MOVIE_POSTER_IMAGE.setImageResource(R.drawable.imageisnotvalid);
        }

        // Set Star for Movies which existed in favorite list
        if(isMovieInFavoriteList(ORIGINAL_TITLE_MOVIE)){
              /*
             * Test
             * Log.e("movie star","is valid");
             */
            viewHolder.MOVIE_FAVORITE_IMAGE.setAlpha(0.7f);
            viewHolder.MOVIE_FAVORITE_IMAGE.setScaleX(1f);
            viewHolder.MOVIE_FAVORITE_IMAGE.setScaleY(1f);

            AppAnimation.gridMovieAnimation(
                    viewHolder.MOVIE_POSTER_IMAGE, viewHolder.MOVIE_RATING_BAR,
                    viewHolder.MOVIE_RELEASE_DATE,viewHolder.MOVIE_LINE_VIEW,
                    viewHolder.MOVIE_FAVORITE_IMAGE);
        }else{
            viewHolder.MOVIE_FAVORITE_IMAGE.setTranslationX(0);

            AppAnimation.gridMovieAnimation(
                    viewHolder.MOVIE_POSTER_IMAGE, viewHolder.MOVIE_RATING_BAR,
                    viewHolder.MOVIE_RELEASE_DATE,viewHolder.MOVIE_LINE_VIEW,
                    null);
        }

        viewHolder.MOVIE_RATING_BAR.setRating(MOVIE_RATING_DATABASE / 2);
        viewHolder.MOVIE_RELEASE_DATE.setText(MOVIE_RELEASE_DATA_DATABASE);
    }

    private boolean isMovieInFavoriteList(String originalMovie_Title){

        String selection = MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN + "=?";
        String []selectionArgs = {originalMovie_Title};

        Cursor cursor= mContext.getContentResolver().query(
                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                MovieDataBaseControl.PROJECTION_FAVORITE_TABLE,
                selection,
                selectionArgs,
                null
        );

        return (cursor.moveToFirst());
    }

}