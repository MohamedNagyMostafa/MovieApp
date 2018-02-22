package com.example.mohamednagy.myapplication.model;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class Review {
    private String mUserName;
    private String mReviewUrl;
    private String mReview;

    public Review(){}

    public Review(String userName, String review, String reviewUrl){
        mUserName = userName;
        mReview = review;
        mReviewUrl = reviewUrl;
    }

    public String getReview() {
        return mReview;
    }

    public String getReviewUrl() {
        return mReviewUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setReview(String mReview) {
        this.mReview = mReview;
    }

    public void setReviewUrl(String mReviewUrl) {
        this.mReviewUrl = mReviewUrl;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}

