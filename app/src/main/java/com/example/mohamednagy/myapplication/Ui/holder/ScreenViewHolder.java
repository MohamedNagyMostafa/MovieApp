package com.example.mohamednagy.myapplication.Ui.holder;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.R;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class ScreenViewHolder {
    public static class DetailsViewHolder{
        public final ImageView BACKDROP_IMAGE_VIEW;
        public final ImageView RATING_IMAGE_VIEW;
        public final ImageView DATE_IMAGE_VIEW;
        public final ImageView VOTE_IMAGE_VIEW;
        public final ImageView TRAILER_ARROW_IMAGE_VIEW;

        public final TextView ORIGINAL_TITLE_TEXT_VIEW;
        public final TextView DATE_TEXT_VIEW;
        public final TextView OVERVIEW_TEXT_VIEW;
        public final TextView VOTE_COUNT_TEXT_VIEW;
        public final TextView RATING_TEXT_VIEW;

        public final RatingBar RATING_BAR;
        public final LinearLayout TRAILER_LAYOUT;
        public final FrameLayout VIDEO_FRAME_LAYOUT;
        public final SwipeRefreshLayout SWIPE_REFERESH_LAYOUT;
        public final FloatingActionButton FLOATING_ACTION_BUTTON;
        public final RecyclerView MOVIE_TRAILER_RECYCLE_VIDE;
        public final LinearLayout FAVORITE_LAYOUT;

        public DetailsViewHolder(View deatilsView){
            BACKDROP_IMAGE_VIEW = deatilsView.findViewById(R.id.backdrop_imageView);
            RATING_IMAGE_VIEW = deatilsView.findViewById(R.id.ratingImageView);
            DATE_IMAGE_VIEW = deatilsView.findViewById(R.id.dateImageView);
            VOTE_IMAGE_VIEW = deatilsView.findViewById(R.id.voteImageView);
            TRAILER_ARROW_IMAGE_VIEW = deatilsView.findViewById(R.id.trailerImageView);

            ORIGINAL_TITLE_TEXT_VIEW = deatilsView.findViewById(R.id.original_title_textView);
            DATE_TEXT_VIEW = deatilsView.findViewById(R.id.details_release_date_textView);
            OVERVIEW_TEXT_VIEW = deatilsView.findViewById(R.id.overView_Movie_textView);
            VOTE_COUNT_TEXT_VIEW = deatilsView.findViewById(R.id.vote_count_textView);
            RATING_TEXT_VIEW = deatilsView.findViewById(R.id.rating_text_view);

            RATING_BAR = deatilsView.findViewById(R.id.details_rating_bar);
            TRAILER_LAYOUT= deatilsView.findViewById(R.id.movie_trailer);
            SWIPE_REFERESH_LAYOUT = deatilsView.findViewById(R.id.swipe_refresh_detail);

            FLOATING_ACTION_BUTTON = deatilsView.findViewById(R.id.favorite_floating_btn);

            MOVIE_TRAILER_RECYCLE_VIDE = deatilsView.findViewById(R.id.movie_trailer_recycle_view);
            VIDEO_FRAME_LAYOUT = deatilsView.findViewById(R.id.movie_trailer_video_view);
            FAVORITE_LAYOUT = deatilsView.findViewById(R.id.favorite_layout);
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

        // Lists.
        public static class MovieTrailerRecycle extends RecyclerView.ViewHolder{
            public final TextView TRAILER_NAME_TEXT_VIEW;
            public final TextView TRAILER_TYPE_TEXT_VIEW;
            public final ImageView TRAILER_IMAGE_VIEW;

            public MovieTrailerRecycle(View movieTrailersView){
                super(movieTrailersView);
                TRAILER_NAME_TEXT_VIEW = movieTrailersView.findViewById(R.id.trailer_name_text_view);
                TRAILER_TYPE_TEXT_VIEW = movieTrailersView.findViewById(R.id.trailer_type_text_view);
                TRAILER_IMAGE_VIEW = movieTrailersView.findViewById(R.id.youtube_video_image_view);
            }

            public void setValues(String trailerName, String trailerType){
                TRAILER_TYPE_TEXT_VIEW.setText(trailerName);
                TRAILER_TYPE_TEXT_VIEW.setText(trailerType);
            }
        }
    }

    public static class MainViewHolder{
        public final GridView MOVIES_GRID_VIEW;
        public final View MOVIES_EMPTY_VIEW;
        public final SwipeRefreshLayout SWIPE_REFRESH_LAYOUT;

        public MainViewHolder(View mainView){
            MOVIES_GRID_VIEW = mainView.findViewById(R.id.grid_movies);
            MOVIES_EMPTY_VIEW = mainView.findViewById(R.id.empty_view);
            SWIPE_REFRESH_LAYOUT = mainView.findViewById(R.id.swipe_refresh);
        }

        public static class AdapterViewHolder{
            public final TextView MOVIE_RELEASE_DATE;
            public final ImageView MOVIE_POSTER_IMAGE;
            public final RatingBar MOVIE_RATING_BAR;
            public final ImageView MOVIE_FAVORITE_IMAGE;
            public final View MOVIE_LINE_VIEW;

            public AdapterViewHolder(View view) {
                MOVIE_RELEASE_DATE = view.findViewById(R.id.release_date_text);
                MOVIE_POSTER_IMAGE = view.findViewById(R.id.poster_image_imageView);
                MOVIE_RATING_BAR = view.findViewById(R.id.rating_ratingBar);
                MOVIE_LINE_VIEW = view.findViewById(R.id.line_view);
                MOVIE_FAVORITE_IMAGE = view.findViewById(R.id.movie_favorite_image);
            }
        }
    }

    public static class ReviewsViewHolder{

        public RecyclerView REVIEW_RECYCLE_VIEW;
        public SwipeRefreshLayout SWIPE_REFRESH_LAYOUT;
        public LinearLayout EMPTY_LAYOUT;

        public ReviewsViewHolder(View reviewsView){
            REVIEW_RECYCLE_VIEW = reviewsView.findViewById(R.id.recycle_view);
            SWIPE_REFRESH_LAYOUT = reviewsView.findViewById(R.id.swipe_refresh_review_list);
            EMPTY_LAYOUT = reviewsView.findViewById(R.id.review_empty_view);
        }

        public static class AdapterViewHolder extends RecyclerView.ViewHolder{
            public final TextView USER_NAME_TEXT_VIEW;
            public final TextView USER_REVIEW_TEXT_VIEW;
            public final Button MORE_BUTTON;

            public AdapterViewHolder(View adapterView) {
                super(adapterView);
                USER_NAME_TEXT_VIEW = adapterView.findViewById(R.id.user_name_text_view);
                USER_REVIEW_TEXT_VIEW = adapterView.findViewById(R.id.user_comment_text_view);
                MORE_BUTTON = adapterView.findViewById(R.id.more_button);
            }
        }
    }
}
