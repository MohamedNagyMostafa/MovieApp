package com.example.mohamednagy.myapplication.Ui.movie_details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.movie_details.ui_helper.TrailersAdapter;
import com.example.mohamednagy.myapplication.Ui.movie_main_list.ui_helper.FavoriteStarListener;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.Ui.reviews_list.ReviewsActivity;
import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.MovieDataBaseControl;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkModelsListCallback;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkLoader;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkModelListLoader;
import com.example.mohamednagy.myapplication.loaderTasks.luncher.NetworkLoaderModelListLaunch;
import com.example.mohamednagy.myapplication.loaderTasks.luncher.NetworkLoaderMoviesLaunch;
import com.example.mohamednagy.myapplication.model.Review;
import com.example.mohamednagy.myapplication.model.Trailer;
import com.example.mohamednagy.myapplication.saver.DataSaver;
import com.example.mohamednagy.myapplication.video.VideoHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>, NetworkModelsListCallback<List<Trailer>> {

    private String imageURL;
    private Uri uri ;
    private Integer movieId;

    private static final int CURSOR_LOADER_DETAIL_ID = 4;

    private static FavoriteStarListener mFavoriteStarListener;
    private ScreenViewHolder.DetailsViewHolder mDetailsViewHolder;
    private NetworkLoaderModelListLaunch<Trailer> networkLoaderModelListLaunch;
    private TrailersAdapter mTrailersAdapter;

    private DataSaver.DetailsActivityData mDetailsDataSaver;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(!imageURL.equals("null")) {
                        mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setRefreshing(true);
                        downloadBackDropImage(imageURL);
                    }
                }
            };

    private View.OnClickListener onFloatingClickListenerFavorite =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*
                     * Test
                     * Log.e("Column ",String.valueOf(ContentUris.parseId(uri)));
                     */
                    boolean isFavorite;
                    // check if data is existed in favorite table
                    if (movieInFavoriteList()) {
                        String selection = MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN + " =?";
                        String[] selectionArg = {mDetailsViewHolder.ORIGINAL_TITLE_TEXT_VIEW.getText().toString()};
                        getContext().getContentResolver().delete(
                                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                                selection,
                                selectionArg
                        );

                        /*
                     * Test
                     * Log.e("delete num ", String.valueOf(delectnum) + " is Done");
                     */
                        // Delete movie as favorite from database
                        setMovieAsFavoriteList(false);

                        isFavorite = true;
                    } else {
                        ContentValues contentValues = getContentValues();

                        getContext().getContentResolver().insert(
                                MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI
                                , contentValues);

                        isFavorite = false;

                        // Insert movie as favorite to database
                        setMovieAsFavoriteList(true);

                        Toast.makeText(
                                getActivity(),"Movie inserted in favorite list",Toast.LENGTH_SHORT)
                                .show();

                        /*
                         * Test
                         * Log.e("insert num ", String.valueOf(ContentUris.parseId(uriColumn)) + " is Done");
                         */
                    }
                    // Floating button rotation
                    AppAnimation.floatingButtonFavoriteAnimation(mDetailsViewHolder.FLOATING_ACTION_BUTTON,isFavorite);
                    // Display favorite star for current movie in movies list
                    mFavoriteStarListener.showFavoriteStar(isFavorite);

                    // In favorite list
                    if (uri.getPath().contains(MovieContract.MovieMainEntry.TABLE_NAME)) {
                        setFloatingActionButton();
                    } else {
                        mDetailsViewHolder.FLOATING_ACTION_BUTTON.setVisibility(View.INVISIBLE);
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


                }
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_details, container,false);
        mDetailsViewHolder = new ScreenViewHolder.DetailsViewHolder(rootView);
        mTrailersAdapter = new TrailersAdapter(null,
                new VideoHandler(mDetailsViewHolder.buildYoutubeFrame(getChildFragmentManager()), getContext()),
                mDetailsViewHolder);
        /// Get data from MainActivity (Intent/Arguments)
        /// One/Two Pane

        Bundle bundle = getArguments();

        if(bundle != null) {
            uri = Uri.parse(bundle.getString(Utility.ExtrasHandler.URI_EXTRA_KEY));
            //Log.e("bundle","is not null");
        }


        mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setColorSchemeResources(R.color.colorPrimaryDark);
        mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setOnRefreshListener(onRefreshListener);

        /// set click listeners
        mDetailsViewHolder.FLOATING_ACTION_BUTTON.setOnClickListener(onFloatingClickListenerFavorite);
        mDetailsViewHolder.TRAILER_LAYOUT.setOnClickListener(onClickListenerTrailer);
        /// scrollbar for overview TextView
        mDetailsViewHolder.OVERVIEW_TEXT_VIEW.setMovementMethod(new ScrollingMovementMethod());
        mDetailsDataSaver = new DataSaver.DetailsActivityData();
        // Recycle Settings.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mDetailsViewHolder.MOVIE_TRAILER_RECYCLE_VIDE.setLayoutManager(layoutManager);
        mDetailsViewHolder.MOVIE_TRAILER_RECYCLE_VIDE.setItemAnimator(new DefaultItemAnimator());
        mDetailsViewHolder.MOVIE_TRAILER_RECYCLE_VIDE.setAdapter(mTrailersAdapter);

        if(savedInstanceState != null){
            Log.e("save","done");
            String[] savedData = savedInstanceState.getStringArray(mDetailsDataSaver.DATA_SAVER_ID);
            Bitmap bitmapImage = Utility.DataTypeHandling.convertByteArrayToBitmap(
                    savedInstanceState.getByteArray(mDetailsDataSaver.DATA_IMAGE_SAVER_ID));

            assert savedData != null;
            // store data.
            mDetailsDataSaver.setMovieData(savedData);
            mDetailsDataSaver.setImageData(bitmapImage);
            // set previous data.
            mDetailsViewHolder.setValues(
                    Utility.DataTypeHandling.optText(savedData[DataSaver.DetailsActivityData.MOVIE_ORIGINAL_TITLE]),
                    Utility.DataTypeHandling.optText(savedData[DataSaver.DetailsActivityData.MOVIE_RELEASE_DATE]),
                    Utility.DataTypeHandling.optText(savedData[DataSaver.DetailsActivityData.MOVIE_OVERVIEW]),
                    Utility.DataTypeHandling.optText(savedData[DataSaver.DetailsActivityData.MOVIE_VOTE_COUNT]),
                    Utility.DataTypeHandling.optText(savedData[DataSaver.DetailsActivityData.MOVIE_VOTE_RATE]),
                    bitmapImage
            );

        } else if(bundle != null) {
            /// Set UI
            getLoaderManager().initLoader(CURSOR_LOADER_DETAIL_ID, null, this);
        }

        if(rootView.findViewById(R.id.land_space_mode) != null){

            AppAnimation.landscapeAnimation(
                    (LinearLayout) rootView.findViewById(R.id.movie_items_list),
                    mDetailsViewHolder.FLOATING_ACTION_BUTTON,
                    (RelativeLayout) rootView.findViewById(R.id.movie_image_component)
                    );
        }


        return rootView;
    }

    public static void setFavoriteStarListener(
            FavoriteStarListener favoriteStarListener){
        mFavoriteStarListener = favoriteStarListener;
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

        movieId = data.getInt(MovieDataBaseControl.getMovieId(columnsCount));

        // Start load trailers.
        trailersLoad();
        // image background
        imageURL = BACKDROP_IMAGE_DATABASE;

        if(!imageURL.equals("null")) {
            mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setRefreshing(true);
            downloadBackDropImage(imageURL);
        }else{
            mDetailsViewHolder.BACKDROP_IMAGE_VIEW.setImageResource(R.drawable.imageisnotvalidbackdrop);
        }

        mDetailsViewHolder.setValues(
                ORIGINAL_TITLE_DATABASE,
                RELEASE_DATE_DATABASE,
                OVERVIEW_DATABASE,
                String.valueOf(VOTE_COUNT_DATABASE),
                String.valueOf(VOTE_RATING_DATABASE)
        );
        // store data.
        mDetailsDataSaver.setMovieData(
                ORIGINAL_TITLE_DATABASE,
                String.valueOf(movieId),
                OVERVIEW_DATABASE,
                RELEASE_DATE_DATABASE,
                String.valueOf(VOTE_COUNT_DATABASE),
                String.valueOf(VOTE_RATING_DATABASE)
        );

        setFloatingActionButton();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }

    private boolean movieInFavoriteList(){

        String selection = MovieContract.FavoriteMovieEntry.ORIGINAL_TITLE_COLUMN + "=?";
        String[] selectionArgs = {mDetailsViewHolder.ORIGINAL_TITLE_TEXT_VIEW.getText().toString()};


        Cursor cursor = null;
        boolean check =false ;
        try {
            cursor = getContext().getContentResolver().query(
                    MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null
            );

            assert cursor != null;

            check =  cursor.moveToFirst();
        }catch (NullPointerException e){
            Log.e("Error",e.toString());
        }finally {
            if(cursor !=null)
                cursor.close();
        }
        return check;
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

        assert cursor != null;
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
            mDetailsViewHolder.FLOATING_ACTION_BUTTON.setImageResource(R.drawable.ic_star_rate_black_18dp);
        }else{
            mDetailsViewHolder.FLOATING_ACTION_BUTTON.setImageResource(R.drawable.ic_star_border_white_48dp);
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
            if(!mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.isRefreshing())
                openReviewsScreen();
            else
                Toast.makeText(getContext(),"Please wait until image loading be completed", Toast.LENGTH_LONG).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("done", "saved");
        outState.putStringArray(
                mDetailsDataSaver.DATA_SAVER_ID,
                mDetailsDataSaver.getMovieData()
        );
        outState.putByteArray(
                mDetailsDataSaver.DATA_IMAGE_SAVER_ID,
                mDetailsDataSaver.getImageData()
        );
        super.onSaveInstanceState(outState);
    }


    private void setMovieAsFavoriteList(boolean isFavorite){

        String selection = MovieContract.MovieMainEntry.FAVORITE_MOVIE_COLUMN +"=?";
        String[] selectionArgs = {mDetailsViewHolder.ORIGINAL_TITLE_TEXT_VIEW.getText().toString()};

        ContentValues contentValues = new ContentValues();
        if(isFavorite)
            contentValues.put(MovieContract.MovieMainEntry.FAVORITE_MOVIE_COLUMN,
                    MovieContract.MovieMainEntry.IS_FAVORITE);
        else
            contentValues.put(MovieContract.MovieMainEntry.FAVORITE_MOVIE_COLUMN,
                    MovieContract.MovieMainEntry.IS_NOT_FAVORITE);

        getContext().getContentResolver().update(
                MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,
                contentValues,
                selection,
                selectionArgs
        );
    }

    private void downloadBackDropImage(String imageURL){
        Picasso.with(getContext())
                .load(Utility.UrlBuilder.createUrlImage(imageURL).toString())
                .into(mDetailsViewHolder.BACKDROP_IMAGE_VIEW, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        mDetailsDataSaver.setImageData(
                                ((BitmapDrawable)mDetailsViewHolder.BACKDROP_IMAGE_VIEW.getDrawable())
                                        .getBitmap());

                        mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setRefreshing(false);
                    }

                    @Override
                    public void onError() {
                        Log.e("image load","Error");
                        mDetailsViewHolder.SWIPE_REFERESH_LAYOUT.setRefreshing(false);
                    }
                });
    }

    private void openReviewsScreen(){
        Intent reviewScreenIntent = new Intent(getActivity(), ReviewsActivity.class);
        reviewScreenIntent.putExtra(Utility.ExtrasHandler.MOVIE_EXTRA_KEY,  String.valueOf(movieId));
        getActivity().startActivity(reviewScreenIntent);
    }

    @Override
    public void updateUi(List<Trailer> trailerList) {
        Log.e("update ui", "done");
        mTrailersAdapter.swapList(trailerList);
        mTrailersAdapter.notifyDataSetChanged();
    }

    @Override
    public void launchNetworkLoader(LoaderManager.LoaderCallbacks<List<Trailer>> networkLoader, @Nullable Boolean dataChanged) {
        getLoaderManager().initLoader(DataNetworkModelListLoader.TRAILERS_LOADER_ID, null, networkLoader);
    }

    @Override
    public DataNetworkLoader<List<Trailer>> createNetworkLoader() {
        return new DataNetworkModelListLoader<>(getContext(), String.valueOf(movieId));
    }

    private void trailersLoad(){
        networkLoaderModelListLaunch = new NetworkLoaderModelListLaunch<Trailer>(this);
        networkLoaderModelListLaunch.execute();
    }
}
