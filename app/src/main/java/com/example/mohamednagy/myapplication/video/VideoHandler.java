package com.example.mohamednagy.myapplication.video;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.BuildConfig;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

public class VideoHandler {

    private final YouTubePlayerSupportFragment YOUTUBE_PLAYER_FRAGMENT;
    private final Context CONTEXT;

    public VideoHandler(YouTubePlayerSupportFragment youTubePlayerView, Context context){
        YOUTUBE_PLAYER_FRAGMENT = youTubePlayerView;
        CONTEXT =context;
    }


    public void setUrlAndStart(final String url){
        YOUTUBE_PLAYER_FRAGMENT.initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(youTubePlayer != null){
                    Log.e("done","done");
                    if(!b) {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        youTubePlayer.loadVideo(url);
                        youTubePlayer.play();
                    }

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(CONTEXT, "Failed to initialize.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
