package com.example.mohamednagy.myapplication.Ui.reviews_list.ui_helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ScreenViewHolder.ReviewsViewHolder.AdapterViewHolder> {
    private List<Review> mReviewsList;

    public ReviewsAdapter(List<Review> reviewList){
        mReviewsList = reviewList;
    }

    @Override
    public ScreenViewHolder.ReviewsViewHolder.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScreenViewHolder.ReviewsViewHolder.AdapterViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.reviews_list_item_recycle, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final ScreenViewHolder.ReviewsViewHolder.AdapterViewHolder holder, int position) {
        final Review userReview = mReviewsList.get(position);

        holder.USER_NAME_TEXT_VIEW.setText(userReview.getUserName());
        holder.USER_REVIEW_TEXT_VIEW.setText(userReview.getReview());
        holder.MORE_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.Utils.openLinkOnBrowser(userReview.getReviewUrl(), holder.MORE_BUTTON.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null)?0:mReviewsList.size();
    }

    public void swapList(List<Review> reviewList){
        mReviewsList = reviewList;
    }
}
