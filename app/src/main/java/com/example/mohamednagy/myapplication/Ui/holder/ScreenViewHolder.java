package com.example.mohamednagy.myapplication.Ui.holder;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.DetailsActivity;
import com.example.mohamednagy.myapplication.R;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class ScreenViewHolder {
    public static class DetailsViewHolder{
        public final ImageView BACKDROP_IMAGE_VIEW;
        public final ImageView RATING_IMAGE_VIEW;
        public final ImageView DATE_IMAGE_VIEW;
        public final ImageView VOTE_IMAGE_VIEW;

        public final TextView ORIGINAL_TITLE_TEXT_VIEW;
        public final TextView DATE_TEXT_VIEW;
        public final TextView OVERVIEW_TEXT_VIEW;
        public final TextView VOTE_COUNT_TEXT_VIEW;
        public final TextView RATING_TEXT_VIEW;

        public final RatingBar RATING_BAR;
        public final LinearLayout TRAILER_LAYOUT;
        public final SwipeRefreshLayout SWIPE_REFERESH_LAYOUT;

        public final FloatingActionButton FLOATING_ACTION_BUTTON;

        public DetailsViewHolder(View deatilsView){
            BACKDROP_IMAGE_VIEW = (ImageView) deatilsView.findViewById(R.id.backdrop_imageView);
            RATING_IMAGE_VIEW = (ImageView) deatilsView.findViewById(R.id.ratingImageView);
            DATE_IMAGE_VIEW = (ImageView) deatilsView.findViewById(R.id.dateImageView);
            VOTE_IMAGE_VIEW = (ImageView) deatilsView.findViewById(R.id.voteImageView);

            ORIGINAL_TITLE_TEXT_VIEW = (TextView) deatilsView.findViewById(R.id.original_title_textView);
            DATE_TEXT_VIEW = (TextView) deatilsView.findViewById(R.id.details_release_date_textView);
            OVERVIEW_TEXT_VIEW = (TextView) deatilsView.findViewById(R.id.overView_Movie_textView);
            VOTE_COUNT_TEXT_VIEW = (TextView) deatilsView.findViewById(R.id.vote_count_textView);
            RATING_TEXT_VIEW = (TextView) deatilsView.findViewById(R.id.rating_text_view);

            RATING_BAR = (RatingBar) deatilsView.findViewById(R.id.details_rating_bar);
            TRAILER_LAYOUT= (LinearLayout) deatilsView.findViewById(R.id.movie_trailer);
            SWIPE_REFERESH_LAYOUT = (SwipeRefreshLayout) deatilsView.findViewById(R.id.swipe_refresh_detail);

            FLOATING_ACTION_BUTTON = (FloatingActionButton) deatilsView.findViewById(R.id.favorite_floating_btn);

        }

        public void setValues(String originalTitle, String date, String overView, String voteCount,
                              String rating){
            ORIGINAL_TITLE_TEXT_VIEW.setText(originalTitle);
            DATE_TEXT_VIEW.setText(date);
            OVERVIEW_TEXT_VIEW.setText(overView);
            VOTE_COUNT_TEXT_VIEW.setText(voteCount);
            RATING_TEXT_VIEW.setText(rating);

            RATING_BAR.setRating(Float.valueOf(rating));

            AppAnimation.detailMovieAnimation(
                    FLOATING_ACTION_BUTTON,
                    DATE_IMAGE_VIEW,
                    RATING_IMAGE_VIEW,
                    VOTE_IMAGE_VIEW
            );
        }

        public void setValues(String originalTitle, String date, String overView, String voteCount,
                              String rating, Bitmap bitmapImage){
            ORIGINAL_TITLE_TEXT_VIEW.setText(originalTitle);
            DATE_TEXT_VIEW.setText(date);
            OVERVIEW_TEXT_VIEW.setText(overView);
            VOTE_COUNT_TEXT_VIEW.setText(voteCount);
            RATING_TEXT_VIEW.setText(rating);
            BACKDROP_IMAGE_VIEW.setImageBitmap(bitmapImage);

            AppAnimation.detailMovieAnimation(
                    FLOATING_ACTION_BUTTON,
                    DATE_IMAGE_VIEW,
                    RATING_IMAGE_VIEW,
                    VOTE_IMAGE_VIEW
            );
        }
    }
}
