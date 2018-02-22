package com.example.mohamednagy.myapplication.video;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.BuildConfig;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

public class VideoHandler {

    private FragmentManager mFragmentManager;
    private ScreenViewHolder.DetailsViewHolder mDetailsViewHolder;
    private Toolbar mToolbar;

    public VideoHandler(FragmentManager fragmentManager, ScreenViewHolder.DetailsViewHolder detailsViewHolder, Toolbar toolbar){
        mFragmentManager = fragmentManager;
        mDetailsViewHolder = detailsViewHolder;
        mToolbar = toolbar;
    }

    public void setUrlAndStart(final String url){
        buildYoutubeFrame().initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restore) {
                if(youTubePlayer != null){
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    setPlayerEvents(youTubePlayer);
                    if(!restore) {
                        youTubePlayer.loadVideo(url);
                    } else {
                        AppAnimation.videoPlayingAnimation(mDetailsViewHolder.FAVORITE_LAYOUT,
                                mToolbar, mDetailsViewHolder.VIDEO_FRAME_LAYOUT);
                        youTubePlayer.play();
                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    private YouTubePlayerSupportFragment buildYoutubeFrame(){
        YouTubePlayerSupportFragment youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_trailer_video_view, youtubePlayerFragment);
        fragmentTransaction.commit();
        return youtubePlayerFragment;

    }

    private void setPlayerEvents(YouTubePlayer youTubePlayer){
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                AppAnimation.videoPlayingAnimation(mDetailsViewHolder.FAVORITE_LAYOUT,
                        mToolbar, mDetailsViewHolder.VIDEO_FRAME_LAYOUT);
            }

            @Override
            public void onPaused() {
                AppAnimation.videoPauseAnimation(mDetailsViewHolder.FAVORITE_LAYOUT,
                        mToolbar);
            }

            @Override
            public void onStopped() {
                AppAnimation.videoPauseAnimation(mDetailsViewHolder.FAVORITE_LAYOUT,
                        mToolbar);
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });
    }
}
