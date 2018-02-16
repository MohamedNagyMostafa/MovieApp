package com.example.mohamednagy.myapplication.Animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
}
