package com.example.mohamednagy.myapplication.Animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by mohamednagy on 11/12/2016.
 */
public class AppAnimation {

    public static void gridMovieAnimation(
            ImageView moviePoster, RatingBar movieRatingBar,
            TextView releaseDate, final View lineView, @Nullable ImageView starFavoriteImageView){

        moviePoster.animate().translationX(0).setDuration(1300)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lineView.setVisibility(View.VISIBLE);
            }
        });;
        movieRatingBar.animate().alpha(1).setDuration(2000);

        releaseDate.animate().alpha(1).setDuration(2000);

        if(starFavoriteImageView != null)
            starFavoriteImageView.animate().rotation(720).translationX(0).setDuration(1300);

    }

    public static void detailMovieAnimation(ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.animate().alpha(0.8f).setDuration(2000);
        }
    }

    public static void floatingButtonFavoriteAnimation(
            FloatingActionButton floatingActionButton,boolean animate){
        if(animate)
            floatingActionButton.animate().rotation(400f).setDuration(1500);
        else
            floatingActionButton.animate().rotation(-400f).setDuration(1500);
    }

    public static void landscapeAnimation(
            LinearLayout linearLayout,
            FloatingActionButton floatingActionButton,
            RelativeLayout relativeLayouts){

        linearLayout.animate().rotation(0);
        floatingActionButton.animate().alpha(4).setDuration(2000);
        relativeLayouts.animate().rotation(0);
    }

    public static void starFavoriteMovieAnimation(ImageView starFavoriteImageView,boolean isClicked){
        if(!isClicked) {
            starFavoriteImageView.animate()
                    .alpha(0.7f)
                    .scaleX(1).scaleY(1)
                    .rotation(360)
                    .setDuration(2000);
        }
        else {
            starFavoriteImageView.animate()
                    .alpha(0f)
                    .scaleX(0).scaleY(0)
                    .rotation(-360)
                    .setDuration(2000);

        }
    }

    public static void videoPlayingAnimation(@Nullable final LinearLayout favoriteLinearLayout, final Toolbar detailToolbar,
                                             final FrameLayout trailerFrame){
        if(favoriteLinearLayout != null)
            favoriteLinearLayout.animate().alpha(0).setDuration(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    favoriteLinearLayout.setVisibility(View.GONE);
                }}
            );
        if(detailToolbar != null)
            detailToolbar.animate().alpha(0).setDuration(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    detailToolbar.setVisibility(View.GONE);
                    Toast.makeText(detailToolbar.getContext(), "done",Toast.LENGTH_LONG).show();
                }}
            );
        trailerFrame.setVisibility(View.VISIBLE);
    }

    public static void videoPauseAnimation(final LinearLayout favoriteLinearLayout, final Toolbar detailToolbar){
        if(favoriteLinearLayout != null)
            favoriteLinearLayout.animate().alpha(1).setDuration(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    favoriteLinearLayout.setVisibility(View.VISIBLE);
                }
            });
        if(detailToolbar != null)
            detailToolbar.animate().alpha(1).setDuration(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    detailToolbar.setVisibility(View.VISIBLE);
                }
            });
    }
}
