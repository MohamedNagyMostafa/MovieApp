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
        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();

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

        if(MOVIE_POSTER_IMAGE_DATABASE != null)
            viewHolder.MOVIE_POSTER_IMAGE.setImageBitmap(
                    Utility.convertByteArrayToBitmap(MOVIE_POSTER_IMAGE_DATABASE)
            );
        else{
            viewHolder.MOVIE_POSTER_IMAGE.setImageResource(R.drawable.imageisnotvalid);
        }

        // Set Star for Movies which existed in favorite list
        if(isMovieInFavoriteList(
                cursor.getString(
                        MovieDataBaseControl.getMovieOriginalTitle(
                                columnsCount)))){
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


    private static class ViewHolder {
        public final TextView MOVIE_RELEASE_DATE;
        public final ImageView MOVIE_POSTER_IMAGE;
        public final RatingBar MOVIE_RATING_BAR;
        public final ImageView MOVIE_FAVORITE_IMAGE;
        public final View MOVIE_LINE_VIEW;

        public ViewHolder(View view) {
            MOVIE_RELEASE_DATE = (TextView)
                    view.findViewById(R.id.release_date_text);
            MOVIE_POSTER_IMAGE = (ImageView)
                    view.findViewById(R.id.poster_image_imageView);
            MOVIE_RATING_BAR = (RatingBar)
                    view.findViewById(R.id.rating_ratingBar);
            MOVIE_LINE_VIEW =
                    view.findViewById(R.id.line_view);
            MOVIE_FAVORITE_IMAGE =(ImageView)
                    view.findViewById(R.id.movie_favorite_image);
        }
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