package com.example.mohamednagy.myapplication.video;

import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;

import com.example.mohamednagy.myapplication.BuildConfig;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.model.YoutubeVideo;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

abstract public class YoutubeVideoFragmentHandler extends YouTubePlayerSupportFragment{

    private YoutubeVideo mYoutubeVideo;
    private YouTubePlayer mYoutubePlayer;
    private OnYoutubeVideoHandlerListener mOnYoutubeVideoHandlerListener;

    protected YoutubeVideoFragmentHandler(){
        init();
    }

    private void init(){
        mYoutubeVideo = new YoutubeVideo(null, 0, YoutubeVideo.IDLE);

        initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restore) {
                if(youTubePlayer != null){
                    mYoutubePlayer = youTubePlayer;
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    public int getState(){
        return mYoutubeVideo.getState();
    }

    public void setOnYoutubeVideoHandlerListener(OnYoutubeVideoHandlerListener onYoutubeVideoHandlerListener){
        mOnYoutubeVideoHandlerListener = onYoutubeVideoHandlerListener;
        setYoutubePlayerListener();
    }

    public interface OnYoutubeVideoHandlerListener{
        void onLoading();
        void onEnding();
        void onPause();
    }

    private void setYoutubePlayerListener(){
        mYoutubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                mYoutubeVideo.setState(YoutubeVideo.RUNNING);
            }

            @Override
            public void onPaused() {
                mYoutubeVideo.setState(YoutubeVideo.PAUSE);
                mOnYoutubeVideoHandlerListener.onPause();
            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });

        mYoutubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                mYoutubeVideo.setState(YoutubeVideo.LOADING);
                mOnYoutubeVideoHandlerListener.onLoading();
            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {
            }

            @Override
            public void onVideoEnded() {
                mYoutubeVideo.setState(YoutubeVideo.ENDED);
                mOnYoutubeVideoHandlerListener.onEnding();
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
    }

    public Parcelable onSaveInstanceState(){
        mYoutubeVideo.setDuration(mYoutubePlayer.getCurrentTimeMillis());
        return mYoutubeVideo;
    }

    public void onRestoreInstanceState(Parcelable parcelable){
        mYoutubeVideo = (YoutubeVideo) parcelable;
        videoHandling();
    }

    public void setVideoKeyAndPlay(String key){
        mYoutubeVideo.setKey(key);
        mYoutubeVideo.setState(YoutubeVideo.IDLE);
        videoHandling();
    }

    private void videoHandling(){

        switch (mYoutubeVideo.getState()){
            case YoutubeVideo.PAUSE:
                mYoutubePlayer.loadVideo(mYoutubeVideo.getKey(), mYoutubeVideo.getDuration());
                mYoutubePlayer.pause();
                break;
            case YoutubeVideo.RUNNING:
                mYoutubePlayer.loadVideo(mYoutubeVideo.getKey(), mYoutubeVideo.getDuration());
                mYoutubePlayer.play();
                break;
            case YoutubeVideo.LOADING:
            case YoutubeVideo.IDLE:
                mYoutubePlayer.loadVideo(mYoutubeVideo.getKey());
                mYoutubePlayer.play();
        }
    }

}
