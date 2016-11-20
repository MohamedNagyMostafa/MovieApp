package com.example.mohamednagy.myapplication;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.downloadData.DownloadDetailsImage;
import com.example.mohamednagy.myapplication.helperClasses.MovieDataBaseControl;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.DownloadImageLoader;
import com.example.mohamednagy.myapplication.loaderTasks.ImageDownloadLaunch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailsFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>,DownloadDetailsImage {

    private String imageURL;
    private Uri uri ;
    private int movieId;

    private static final int DONWLOAD_IMAGE_LOADER_ID = 3;
    private static final int CURSOR_LOADER_DETAIL_ID = 4;

    private ImageDownloadLaunch imageDownloadLaunch;

    private ImageView backDropImage;
    private ImageView trailerImageView;
    private ImageView ratingImageView;
    private ImageView dateImageView;
    private ImageView voteImageView;

    private TextView originalTextView;
    private TextView dateTextView;
    private TextView overviewTextView;
    private TextView countVoteTextView;
    private TextView ratingTextView;

    private RatingBar ratingBarView;
    private LinearLayout trailerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FloatingActionButton floatingActionButtonFavorite;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(!imageURL.equals("null")) {
                        swipeRefreshLayout.setRefreshing(true);
                        imageDownloadLaunch = new ImageDownloadLaunch(DetailsFragment.this);
                    }
                }
            };

    private View.OnClickListener onFloatingClickListenerFavorite =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean animate;
                    // check if data is existed in favorite table
                    if (movieInFavoriteList()) {
                        String selection = MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN + " =?";
                        String[] selectionArg = {originalTextView.getText().toString()};
                        int delectnum = getContext().getContentResolver().delete(
                                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                                selection,
                                selectionArg
                        );

                        Log.e("delete num ", String.valueOf(delectnum) + " is Done");

                        animate = true;
                    } else {
                        ContentValues contentValues = getContentValues();

                        Uri uriColumn = getContext().getContentResolver().insert(
                                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI
                                , contentValues);

                        animate = false;

                        Toast.makeText(
                                getActivity(),"Movie inserted in favorite list",Toast.LENGTH_SHORT)
                                .show();

                        Log.e("insert num ", String.valueOf(ContentUris.parseId(uriColumn)) + " is Done");
                    }

                    AppAnimation.floatingButtonFavoriteAnimation(floatingActionButtonFavorite,animate);

                    if (uri.getPath().contains(MovieContract.MovieMainEntry.TABLE_NAME)) {
                        setFloatingActionButton();
                    } else {
                        floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
                        Toast.makeText(
                                getContext(), "The movie is deleted from favorite list",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            };

    private View.OnClickListener onClickListenerTrailer =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String BASE_URL = "https://www.themoviedb.org/movie/";
                    final String TRAILER_URL = "/videos";

                    Intent webBrowser = new Intent(Intent.ACTION_VIEW);
                    webBrowser.setData(Uri.parse(BASE_URL + movieId + TRAILER_URL));
                    startActivity(webBrowser);

                }
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_details, container,false);

        /// Get data from MainActivity (Intent/Arguments)
        /// One/Two Pane
        Bundle bundle = getArguments();

        if(bundle != null) {
            uri = Uri.parse(bundle.getString(Utility.URI_EXTRA_KEY));
            Log.e("bundle","is not null");
        }

        backDropImage = (ImageView) rootView.findViewById(R.id.backdrop_imageView);
        trailerImageView = (ImageView) rootView.findViewById(R.id.trailerImageView);
        dateImageView = (ImageView) rootView.findViewById(R.id.dateImageView);
        voteImageView = (ImageView) rootView.findViewById(R.id.voteImageView);
        ratingImageView = (ImageView) rootView.findViewById(R.id.ratingImageView);
        originalTextView = (TextView) rootView.findViewById(R.id.original_title_textView);
        dateTextView = (TextView) rootView.findViewById(R.id.details_release_date_textView);
        overviewTextView = (TextView) rootView.findViewById(R.id.overView_Movie_textView);
        countVoteTextView = (TextView) rootView.findViewById(R.id.vote_count_textView);
        dateTextView = (TextView) rootView.findViewById(R.id.details_release_date_textView);
        ratingBarView = (RatingBar) rootView.findViewById(R.id.details_rating_bar);
        trailerLayout= (LinearLayout) rootView.findViewById(R.id.movie_trailer);
        ratingTextView = (TextView) rootView.findViewById(R.id.rating_text_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_detail);

        floatingActionButtonFavorite = (FloatingActionButton) rootView.findViewById(R.id.favorite_floating_btn);
        swipeRefreshLayout.setColorSchemeResources(R.color.detailTextView);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        /// set click listeners
        floatingActionButtonFavorite.setOnClickListener(onFloatingClickListenerFavorite);
        trailerLayout.setOnClickListener(onClickListenerTrailer);
        /// scrollbar for overview TextView
        overviewTextView.setMovementMethod(new ScrollingMovementMethod());
        if(bundle != null ) {
            /// Set UI
            getLoaderManager().initLoader(CURSOR_LOADER_DETAIL_ID, null, this);
        }

        return rootView;
    }

    @Override
    public void launchImageLoader(LoaderManager.LoaderCallbacks loaderCallbacks) {
        getLoaderManager().initLoader(DONWLOAD_IMAGE_LOADER_ID,null,loaderCallbacks);

    }

    @Override
    public DownloadImageLoader createImageLoader() {
        return new DownloadImageLoader(getActivity(),this);
    }

    @Override
    public Bitmap downloadImage() {
        URL url = Utility.createUrlImage(imageURL);
        InputStream inputStream = null;
        try {
            inputStream = url.openConnection().getInputStream();

            return BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            Log.e("eror",e.toString());
        }
        return null;
    }


    @Override
    public void setImageToView(Bitmap imageAsBitmap) {
        swipeRefreshLayout.setRefreshing(false);
        backDropImage.setImageBitmap(imageAsBitmap);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(uri.getPath(),String.valueOf(uri.getPath().contains(MovieContract.MovieMainEntry.TABLE_NAME)));
        if(uri.getPath().contains(MovieContract.MovieMainEntry.TABLE_NAME))
            return new CursorLoader(
                    getActivity(),
                    uri,
                    MovieDataBaseControl.PROJECTION,
                    null,
                    null,
                    null
            );
        else
            return new CursorLoader(
                    getActivity(),
                    uri,
                    MovieDataBaseControl.PROJECTION_FAVORITE_TABLE,
                    null,
                    null,
                    null
            );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst())
            return;

        /// To check if movie from favorite table or main table
        /// main table 9 columns
        /// favorite table 8 columns
        int columnsCount = data.getColumnCount();
        /// MovieDatabaseControl to get the data from correct table
        final String ORIGINAL_TITLE_DATABASE =
                data.getString(MovieDataBaseControl.getMovieOriginalTitle(columnsCount));
        final String BACKDROP_IMAGE_DATABASE =
                data.getString(MovieDataBaseControl.getMovieBackdropImage(columnsCount));
        final String OVERVIEW_DATABASE =
                data.getString(MovieDataBaseControl.getMovieOverview(columnsCount));
        final String RELEASE_DATE_DATABASE =
                data.getString(MovieDataBaseControl.getMovieReleaseDate(columnsCount));
        final int VOTE_COUNT_DATABASE =
                data.getInt(MovieDataBaseControl.getMovieVoteCount(columnsCount));
        final float VOTE_RATING_DATABASE =
                data.getFloat(MovieDataBaseControl.getMovieVoteRating(columnsCount));
        final int MOVIE_ID_DATABASE =
                data.getInt(MovieDataBaseControl.getMovieId(columnsCount));

        originalTextView.setText(ORIGINAL_TITLE_DATABASE);

        movieId = MOVIE_ID_DATABASE;

        // image background
        imageURL = BACKDROP_IMAGE_DATABASE;

        if(!imageURL.equals("null")) {
            swipeRefreshLayout.setRefreshing(true);
            imageDownloadLaunch = new ImageDownloadLaunch(this);
        }else{
            backDropImage.setImageResource(R.drawable.imageisnotvalidbackdrop);
        }

        overviewTextView.setText(OVERVIEW_DATABASE);
        dateTextView.setText(RELEASE_DATE_DATABASE);
        countVoteTextView.setText(
                String.valueOf(VOTE_COUNT_DATABASE)
        );

        ratingBarView.setRating(VOTE_RATING_DATABASE);
        ratingTextView.setText(String.valueOf(VOTE_RATING_DATABASE));

        AppAnimation.detailMovieAnimation(
                floatingActionButtonFavorite,
                dateImageView,
                ratingImageView,
                voteImageView
                );
        setFloatingActionButton();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean movieInFavoriteList(){

        String selection = MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN + "=?";
        String[] selectionArgs = {originalTextView.getText().toString()};

        Cursor cursor = getContext().getContentResolver().query(
                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
                );
        return (cursor.moveToFirst());
    }

    private ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                MovieDataBaseControl.PROJECTION,
                null,
                null,
                null
        );

        if(!cursor.moveToFirst())
            return null;

        int columnsCount = cursor.getColumnCount();

        contentValues.put(
                MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN,
                cursor.getString(MovieDataBaseControl.getMovieOriginalTitle(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.BACKDROP_IMAGE_COLUMN,
                cursor.getString(MovieDataBaseControl.getMovieBackdropImage(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.OVERVIEW_MOVIE_COLUMN,
                cursor.getString(MovieDataBaseControl.getMovieOverview(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.RELEASE_DATE_COLUMN,
                cursor.getString(MovieDataBaseControl.getMovieReleaseDate(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.VOTE_COUNT_COLUMN,
                cursor.getInt(MovieDataBaseControl.getMovieVoteCount(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.VOTE_RATING_COLUMN,
                cursor.getFloat(MovieDataBaseControl.getMovieVoteRating(columnsCount))
        );
        contentValues.put(
                MovieContract.FavoriteMovieEntry.POSTER_IMAGE_COLUMN,
                cursor.getBlob(MovieDataBaseControl.getMoviePosterImage(columnsCount))
        );

        return contentValues;
    }

    private void setFloatingActionButton(){

        if(movieInFavoriteList()) {
            floatingActionButtonFavorite.setImageResource(R.drawable.ic_star_rate_black_18dp);
        }else{
            floatingActionButtonFavorite.setImageResource(R.drawable.ic_star_border_white_48dp);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_reviews){

            final String BASE_URL = "https://www.themoviedb.org/movie/";
            final String TRAILER_URL = "/reviews";

            Intent webBrowser = new Intent(Intent.ACTION_VIEW);
            webBrowser.setData(Uri.parse(BASE_URL + movieId + TRAILER_URL));
            startActivity(webBrowser);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
