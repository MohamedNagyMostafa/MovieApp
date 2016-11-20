package com.example.mohamednagy.myapplication.Animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by mohamednagy on 11/12/2016.
 */
public class AppAnimation {

    public static void gridMovieAnimation(
            ImageView moviePoster, RatingBar movieRatingBar,
            TextView releaseDate, final View lineView){

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
}
