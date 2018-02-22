package com.example.mohamednagy.myapplication.video;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.Animation.AppAnimation;
import com.example.mohamednagy.myapplication.BuildConfig;
import com.example.mohamednagy.myapplication.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

abstract public class VideoHandler implements  YouTubePlayer.PlaybackEventListener{

    private FragmentManager mFragmentManager;


    protected VideoHandler(FragmentManager fragmentManager){
        mFragmentManager = fragmentManager;
    }

    public void setUrlAndStart(final String url){
        buildYoutubeFrame().initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restore) {
                if(youTubePlayer != null){
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setPlaybackEventListener(VideoHandler.this);
                    if(!restore) {
                        youTubePlayer.loadVideo(url);
                    }else{
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
}
