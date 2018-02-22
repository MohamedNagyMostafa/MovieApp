package com.example.mohamednagy.myapplication.Ui.reviews_list;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.reviews_list.ui_helper.ReviewsAdapter;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkModelsListCallback;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkLoader;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkModelListLoader;
import com.example.mohamednagy.myapplication.loaderTasks.luncher.NetworkLoaderModelListLaunch;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsActivityFragment extends Fragment
    implements NetworkModelsListCallback<ArrayList<Review>> {

    private ReviewsAdapter reviewsAdapter;
    private String movieId;
    private NetworkLoaderModelListLaunch<Review> networkLoaderModelListLaunch;
    private ScreenViewHolder.ReviewsViewHolder reviewsViewHolder;

    public ReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reviews, container, false);
        reviewsViewHolder = new ScreenViewHolder.ReviewsViewHolder(rootView);
        List<Review> reviewsList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviewsList);
        // Recycle Settings.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setLayoutManager(layoutManager);
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setItemAnimator(new DefaultItemAnimator());
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setAdapter(reviewsAdapter);

        reviewsViewHolder.SWIPE_REFRESH_LAYOUT.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reviewsViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(true);
                networkLoaderModelListLaunch.execute();
            }
        });

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            movieId = bundle.getString(Utility.ExtrasHandler.MOVIE_EXTRA_KEY);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        networkLoaderModelListLaunch =
                new NetworkLoaderModelListLaunch<Review>(this);
        networkLoaderModelListLaunch.execute();
    }

    @Override
    public void updateUi(ArrayList<Review> reviewList) {
        reviewsViewHolder.SWIPE_REFRESH_LAYOUT.setRefreshing(false);
        reviewsAdapter.swapList(reviewList);
        reviewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void launchNetworkLoader(LoaderManager.LoaderCallbacks<ArrayList<Review>> networkLoader, @Nullable Boolean dataChanged) {
        getLoaderManager().initLoader(DataNetworkModelListLoader.REVIEW_LOADER_ID, null, networkLoader);
    }

    @Override
    public DataNetworkLoader<ArrayList<Review>> createNetworkLoader() {
        return new DataNetworkModelListLoader<>(getActivity(), movieId);
    }
}

