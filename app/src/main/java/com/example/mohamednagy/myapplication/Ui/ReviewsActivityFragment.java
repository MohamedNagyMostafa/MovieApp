package com.example.mohamednagy.myapplication.Ui;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.loaderTasks.callbacks.NetworkReviewsCallback;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkLoader;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkMovieLoader;
import com.example.mohamednagy.myapplication.loaderTasks.loaders.DataNetworkReviewLoader;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsActivityFragment extends Fragment
    implements NetworkReviewsCallback<List<Review>>{

    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewsList;
    private ScreenViewHolder.ReviewsViewHolder reviewsViewHolder;
    private String movieId;

    public ReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reviews, container, false);
        reviewsViewHolder = new ScreenViewHolder.ReviewsViewHolder(rootView);
        reviewsList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviewsList);
        // Recycle Settings.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setLayoutManager(layoutManager);
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setItemAnimator(new DefaultItemAnimator());
        reviewsViewHolder.REVIEW_RECYCLE_VIEW.setAdapter(reviewsAdapter);

        Bundle bundle = getActivity().getIntent().getExtras();

        if(bundle != null){
            movieId = bundle.getString(Utility.ExtrasHandler.MOVIE_EXTRA_KEY);
        }
        return rootView;
    }

    @Override
    public void updateUi(List<Review> reviewList) {
        reviewsAdapter.swapList(reviewList);
        reviewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void launchNetworkLoader(LoaderManager.LoaderCallbacks<List<Review>> networkLoader, @Nullable Boolean dataChanged) {
        getLoaderManager().initLoader(DataNetworkReviewLoader.REVIEW_LOADER_ID, Utility.DataTypeHandling.convertDataToBundle(movieId), networkLoader);
    }

    @Override
    public DataNetworkLoader<List<Review>> createNetworkLoader() {
        return new DataNetworkLoader<>(getContext());
    }
}

