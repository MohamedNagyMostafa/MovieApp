package com.example.mohamednagy.myapplication.Ui.movie_main_list;

import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.Ui.movie_details.DetailsFragment;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.movie_main_list.ui_helper.FavoriteStarListener;
import com.example.mohamednagy.myapplication.Ui.movie_main_list.ui_helper.MoviesAdapter;
import com.example.mohamednagy.myapplication.Ui.movie_main_list.ui_helper.UriListener;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.MovieDataBaseControl;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.CursorUiLoader;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkMovieLoader;
import com.example.mohamednagy.myapplication.loaderTasks.luncher.NetworkLoaderMoviesLaunch;
import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkMoviesCallback;
import com.example.mohamednagy.myapplication.permission.PermissionHandle;
import com.example.mohamednagy.myapplication.saver.DataSaver;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
    implements FavoriteStarListener, NetworkMoviesCallback<Void>{

    private MoviesAdapter moviesAdapter;
    private String sortType;
    private View currentMovieView;
    private String lastSort;
    private UriListener uriListener;
    private DataSaver.MainActivityData mainActivitySaver;

    private static final int CURSOR_LOADER_ID = 2;

    private ScreenViewHolder.MainViewHolder mainViewHolder;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(permissionInvestigation())
                        startLoadData();
                }
            };

    private GridView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!Utility.LoaderHandler.getLoaderState()) {

                        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                        currentMovieView = view;

                        Uri uri =(
                                (cursor.getColumnCount() == MovieContract.MovieMainEntry.NUMBER_OF_COLUMNS)?
                                        MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI :
                                        MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI)
                                .buildUpon().appendPath(
                                        String.valueOf(cursor.getInt(MovieDataBaseControl._ID_MAIN_COLUMN)))
                                .build();
                        // setting base on pane

                        if(uriListener != null)
                            uriListener.setUri(uri);
                    /*
                     * Test
                     * else
                     *  Log.e("uriListener ","is null");
                     */

                    }else{
                        Snackbar.make(getView(),
                                "Please wait until movies loading finish",
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sortType =getCurrentSort();

        /*
         * Test
         * Log.e("onCreate","is called 000000");
         */
        DetailsFragment.setFavoriteStarListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
         * Test
         * Log.e("detail onCreateView","is called 000000");
         */
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mainViewHolder = new ScreenViewHolder.MainViewHolder(rootView);
        moviesAdapter = new MoviesAdapter(getContext(),null,0);
        mainActivitySaver = new DataSaver.MainActivityData();

        mainViewHolder.MOVIES_GRID_VIEW.setEmptyView(mainViewHolder.MOVIES_EMPTY_VIEW);
        mainViewHolder.MOVIES_GRID_VIEW.setOnItemClickListener(onItemClickListener);
        mainViewHolder.MOVIES_GRID_VIEW.setAdapter(moviesAdapter);

        mainViewHolder.SWIPE_REFRESH_LAYOUT.setColorSchemeResources(R.color.colorPrimaryDark);
        mainViewHolder.SWIPE_REFRESH_LAYOUT.setOnRefreshListener(onRefreshListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        moviesAdapter.notifyDataSetChanged();
        mainViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(Utility.LoaderHandler.getLoaderState());
        updateMoviesGrid();
    }

    /**
     * Set Listener uri when MainActivity Class
     * is Called.
     * @param uriListener the Uri Listener . To Pass Uri to
     *                    MainActivity
     */
    public void setUriListener (UriListener uriListener){

        /*
         * Test
         * Log.e("setUriListener","is called");
         */
        this.uriListener = uriListener;
    }

    /**
     * Check if the last setting sort was favorite setting or not .
     */
    private boolean isFavoriteSetting(){
        return sortType.equals(getString(R.string.settings_sort_favorite));
    }

    /**
     * Update new data from network if no data in database
     * for the current sort .    Pass the data from database directly
     * to Ui if the current sort data is identical with the data in database .
     */
    private void updateMoviesGrid(){

        /*
         * Test
         * Log.e("updateMovieGrid","is called");
         */

            if (databaseHasData()) {
                    mainViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(Utility.LoaderHandler.getLoaderState());

                    if (!isFavoriteSetting()) {
                        Snackbar.make(getView(),
                                "Warning : This is old movies",
                                Toast.LENGTH_SHORT).show();
                    }

                    /*
                     * Test
                     * Log.e("CursorUiLoader", "is started");
                     */
                    CursorUiLoader cursorUiLoader = new CursorUiLoader(this, isSortChanged());
            }else{
                Log.e("pass1","accept");
                if(permissionInvestigation())
                    startLoadData();
                Log.e("pass3","accept");

            }
    }

    /**
     * Launch The network Loader  and change the state of refresh button
     * to be in loading state if the network is connected .
     * Otherwise display message for user to check his network connection .
     */
    private void startLoadData(){
        Log.e("pass2", "accpet");
        // To prevent produce two loaders at the same time
        if(Utility.LoaderHandler.getLoaderState())
            return;

        if(networkIsConnected()) {

            Utility.LoaderHandler.setLoaderState(true);
            mainViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(Utility.LoaderHandler.getLoaderState());

            NetworkLoaderMoviesLaunch networkLoaderMoviesLaunch =
                    new NetworkLoaderMoviesLaunch(this, isSortChanged());
        }else{

            Snackbar.make(getView(),
                    "Check your network connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get current sort if movies sort is changed .   Otherwise return
     * default movies sort .
     * @return current settings sort for movie list .
     */
    private String getCurrentSort(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());

        return sharedPreferences.getString(
                getString(R.string.settings_sort_key),
                getString(R.string.settings_sort_default));
    }

    /**
     * Check if movies list sort is changed or not by
     * Compare between The last movies list sort to
     * the current movies sort.    This method return
     * reversing comparing result.
     * @return state of list movies sort. .
     */
    private boolean isSortChanged(){
        return (!sortType.equals(getCurrentSort()));
    }

    /**
     * Get the state of networking connection of the current mobile
     * @return the network is connected in mobile or not
     */
    private boolean networkIsConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     *  Initialize network loader if movies list sort is not changed.   Otherwise
     *  restart network loader .
     * @param dataChanged result of check if sort of  movies list state
     *                    is changed or not
     */

    @Override
    public void launchNetworkLoader(LoaderManager.LoaderCallbacks<Void> loaderCallbacks, @Nullable Boolean dataChanged) {
        if(dataChanged){

            /*
             * Test
             * Log.e("restart network","00000000");
             */

            getLoaderManager().restartLoader(DataNetworkMovieLoader.DATA_NETWORK_LOADER_ID,null,loaderCallbacks);
        }else{

            /*
             * Test
             * Log.e("init network","00000000");
             */
            getLoaderManager().initLoader(DataNetworkMovieLoader.DATA_NETWORK_LOADER_ID,null,loaderCallbacks);
        }
    }

    @Override
    public DataNetworkMovieLoader createNetworkLoader() {

        /*
         * Test
         * Log.e("detail","asyncLoader for network is created");
         */
        return new DataNetworkMovieLoader(getContext(),getCurrentSort());
    }

    @Override
    public void connectNetworkLoaderToCursorLoader(boolean dataChanged) {

        /*
         * Test
         * Log.e("finished network","finished 0000000");
         */
        Utility.LoaderHandler.setLoaderState(false);
        CursorUiLoader cursorUiLoader = new CursorUiLoader(this,dataChanged);
    }

    @Override
    public void launchCursorLoader(android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> cursorLoader, boolean datachanged) {

        if (datachanged)
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, cursorLoader);
        else
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, cursorLoader);

    }

    @Override
    public CursorLoader createCursorLoader() {

        /*
         *  Test
         * Log.e("detail","asyncLoader for cursor is created 00000");
         */

        if(!getCurrentSort().equals(getString(R.string.settings_sort_favorite))) {
            return new CursorLoader(
                    getContext(),
                    MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,
                    MovieDataBaseControl.PROJECTION,
                    null,
                    null,
                    null);
        }
        else {
            lastSort = sortType;
            return new CursorLoader(
                    getContext(),
                    MovieContract.FavoriteMovieEntry.MOVIE_FAVORITE_CONTENT_URI,
                    MovieDataBaseControl.PROJECTION_FAVORITE_TABLE,
                    null,
                    null,
                    null);
        }

    }

    @Override
    public void updateAdapter(Cursor cursor) {

        /*
         * Test
         * Log.e("detail","asyncLoader for network is finished and swap adapter 00000");
         */
        // Update adapter.
        moviesAdapter.swapCursor(cursor);

        //for orientation.
        checkPreviousData();

        // Check refresh state for refresh button.
        mainViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(Utility.LoaderHandler.getLoaderState());
        // set current sort.
        sortType = getCurrentSort();
    }

    private void checkPreviousData(){
        Parcelable previousState = mainActivitySaver.getSavingStateAndNull();
        if(previousState != null)
            mainViewHolder.MOVIES_GRID_VIEW.onRestoreInstanceState(previousState);
    }

    /**
     * Check if current sort of movies list has old data
     * in data base or not and return the result .
     */
    boolean databaseHasData(){

        Cursor cursor = null;
        int numberOfRows = 0;

        try {
            // Get data from database
            cursor = getContext().getContentResolver().query(
                    MovieContract.MovieMainEntry.MOVIE_MAIN_CONTENT_URI,
                    MovieDataBaseControl.PROJECTION,
                    null,
                    null,
                    null
            );

            numberOfRows = cursor.getCount();
        }catch (NullPointerException e){
            Log.e("error",e.toString());
        }finally {
            if(cursor != null)
                cursor.close();
        }

        if(lastSort != null && lastSort.equals(getString(R.string.settings_sort_favorite)))
            lastSort = null;

        /*
         * Test
         * Log.e("check","last sort " + lastSort + " current sort " +
           getCurrentSort() + " sort type " + sortType);
         */

        // When App is started or back to movies list without changing in sort settings
        if(lastSort == null && getCurrentSort().equals(sortType)){

            if(numberOfRows > 0)
                return true;
            else
                return false;
        }

        // Move to favorite movies list
        if(lastSort != null && getCurrentSort()
                .equals(getString(R.string.settings_sort_favorite))){
            return true;
        }
        // without change....
        if(lastSort != null && !getCurrentSort()
                .equals(getString(R.string.settings_sort_favorite)) &&
                lastSort.equals(getCurrentSort())){
            // if current sort has data in database
            lastSort = null;
            return true;
        }

        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(DataNetworkMovieLoader.DATA_NETWORK_LOADER_ID);
    }

    /**
     * Display favorite star image on certain movie in movies list when
     * favorite button is clicked (movie is inserted into favorite database),
     * Favorite star image in certain movie view be invisible when this movie
     * is deleted from favorite database.    Set animation for two cases .
     * @param isClicked determine the movie is inserted or deleted from database
     */
    @Override
    public void showFavoriteStar(boolean isClicked) {
        AppAnimation.starFavoriteMovieAnimation(
                (ImageView)currentMovieView.findViewById(R.id.movie_favorite_image),
                isClicked);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("request","reuqests");

        switch (requestCode){
            case PermissionHandle.REQUEST_CODE:
                Log.e("enter","reuqests");
                if(grantResults.length > 0){
                    Log.e("find permission","reuqests");
                    boolean result = true;
                    for (int grantResult : grantResults) {
                        result = grantResult == PackageManager.PERMISSION_GRANTED && result;
                    }
                    if(result)
                        startLoadData();
                    else
                        Log.e("eeror","reject");;
                }else{
                    Log.e("eeror","reject");
                    //TODO add snackbar.
                }
        }
    }

    /**
     * Check Permission for android 6.0 and higher.
     * @return
     */
    private boolean permissionInvestigation(){
        boolean result = true;
        ArrayList<String> requiredPermissions = new ArrayList<>();

        if(PermissionHandle.checkPermission(PermissionHandle.ACCESS_NETWORK_STATE_PERMISSION, getContext())){
            Log.e("permisson1","faild");
            result = false;
            requiredPermissions.add(PermissionHandle.ACCESS_NETWORK_STATE_PERMISSION);
        }

        if(PermissionHandle.checkPermission(PermissionHandle.ACCESS_INTERNET_PERMISSION, getContext())){
            Log.e("permisson2","faild");

            result = false;
            requiredPermissions.add(PermissionHandle.ACCESS_INTERNET_PERMISSION);
        }

        if(requiredPermissions.size() > 0){
            Log.e("enrer to re","er");
            requestPermissions(requiredPermissions.toArray(new String[requiredPermissions.size()]),
                    PermissionHandle.REQUEST_CODE);
        }

        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DataSaver.MainActivityData.GRID_VIEW_STATE_ID,
                mainViewHolder.MOVIES_GRID_VIEW.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            mainActivitySaver.setGridViewState(
                    savedInstanceState.getParcelable(DataSaver.MainActivityData.GRID_VIEW_STATE_ID)
            );
        }
    }
}
