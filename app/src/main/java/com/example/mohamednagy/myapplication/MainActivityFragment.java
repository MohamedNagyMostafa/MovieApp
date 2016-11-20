package com.example.mohamednagy.myapplication;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.database.MovieContract;
import com.example.mohamednagy.myapplication.helperClasses.MovieDataBaseControl;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.CursorUiLoader;
import com.example.mohamednagy.myapplication.loaderTasks.DataNetworkLoader;
import com.example.mohamednagy.myapplication.loaderTasks.Loaders;
import com.example.mohamednagy.myapplication.loaderTasks.NetworkLoaderLaunch;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
    implements Loaders{

    private MoviesAdapter moviesAdapter;
    private String sortType;
    private String lastSort;
    private UriListener uriListener;

    private static final int DATA_NETWORK_LOADER_ID = 1;
    private static final int CURSOR_LOADER_ID = 2;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateMoviesGrid();
                }
            };

    private GridView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

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
                    else
                        Log.e("uriListenr ","is null .......");


                }
            };

    public void setUriListener (UriListener uriListener){
        Log.e("set","is done .........");
        this.uriListener = uriListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortType =getCurrentSort();

        Log.e("onCreate","is called 000000");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("detail onCreateView","is called 000000");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        moviesAdapter = new MoviesAdapter(getContext(),null,0);
        GridView gridMovies = (GridView) rootView.findViewById(R.id.grid_movies);
        View emptyView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        gridMovies.setEmptyView(emptyView);
        gridMovies.setOnItemClickListener(onItemClickListener);
        gridMovies.setAdapter(moviesAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.detailTextView);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return rootView;
    }

    private void updateMoviesGrid(){
        Log.e("update","grid done");
            if (!sortType.equals(getCurrentSort()) || databaseHasData()) {
                swipeRefreshLayout.setRefreshing(true);
                Log.e("enter1","Done");
                if(!networkIsConnected()) {
                    Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
                }
                    if (databaseHasData() || getCurrentSort().equals(getString(R.string.settings_sort_favorite))) {
                    Log.e("called1", "0000000");
                    CursorUiLoader cursorUiLoader = new CursorUiLoader(this, isSortChanged());
                } else {
                    Log.e("called2", "0000000");
                    Utility.setLoaderState(true);
                    NetworkLoaderLaunch networkLoaderLaunch =
                            new NetworkLoaderLaunch(this, isSortChanged());
                }
            }else{
                swipeRefreshLayout.setRefreshing(true);

                Log.e("called3", "0000000");

                Utility.setLoaderState(true);

                NetworkLoaderLaunch networkLoaderLaunch =
                        new NetworkLoaderLaunch(this, isSortChanged());
            }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateMoviesGrid();

        /// rotated mobile .. check refresh state ..
        if(getLoaderManager().getLoader(DATA_NETWORK_LOADER_ID) != null &&
                getLoaderManager().getLoader(DATA_NETWORK_LOADER_ID).isStarted()){
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private String getCurrentSort(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());

        return sharedPreferences.getString(
                getString(R.string.settings_sort_key),
                getString(R.string.settings_sort_default));
    }

    private boolean isSortChanged(){
        return (!sortType.equals(getCurrentSort()));
    }

    private boolean networkIsConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void launchNetworkLoader(LoaderManager.LoaderCallbacks<Void> networkLoader,boolean dataChanged) {
        if(dataChanged){
            Log.e("restart network","00000000");
            getLoaderManager().restartLoader(DATA_NETWORK_LOADER_ID,null,networkLoader);
        }else{
            Log.e("init network","00000000");
            getLoaderManager().initLoader(DATA_NETWORK_LOADER_ID,null,networkLoader);
        }
    }

    @Override
    public DataNetworkLoader createNetworkLoader() {
        Log.e("detail","asyncLoader for network is created 000000");
        return new DataNetworkLoader(getContext(),getCurrentSort());
    }

    @Override
    public void connectNetworkLoaderToCursorLoader(boolean dataChanged) {
        Log.e("finished network","finished  00000");
        CursorUiLoader cursorUiLoader = new CursorUiLoader(this,dataChanged);
    }

    @Override
    public void launchCursorLoader(LoaderManager.LoaderCallbacks<Cursor> cursorLoader, boolean datachanged) {
        if(datachanged){
            getLoaderManager().restartLoader(CURSOR_LOADER_ID,null,cursorLoader);
        }else{
            getLoaderManager().initLoader(CURSOR_LOADER_ID,null,cursorLoader);
        }
    }

    @Override
    public CursorLoader createCursorLoader() {

        Log.e("detail","asyncLoader for cursor is created 00000");
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
        Log.e("detail","asyncLoader for network is finished and swap adapter 00000");
        moviesAdapter.swapCursor(cursor);
        swipeRefreshLayout.setRefreshing(false);
        sortType = getCurrentSort();
        getLoaderManager().destroyLoader(DATA_NETWORK_LOADER_ID);
        Utility.setLoaderState(false);
    }

    boolean databaseHasData(){

        Cursor cursor = null;
        int numberOfRows = 0;

        try {
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

        Log.e("check","last sort " + lastSort + " current sort " + getCurrentSort() + " sort type " + sortType);
        if(numberOfRows > 0){
            Log.e("number ",String.valueOf(numberOfRows));
            if(lastSort == null && getCurrentSort().equals(sortType)){
                return true;
            }else if(sortType.equals(getString(R.string.settings_sort_favorite)) &&
                    lastSort.equals(getCurrentSort()) ){
                lastSort = null;
                return true;
            }
        }
        return false;
    }

}
