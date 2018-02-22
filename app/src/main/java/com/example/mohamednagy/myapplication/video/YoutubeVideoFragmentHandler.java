package com.example.mohamednagy.myapplication.video;

import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.mohamednagy.myapplication.BuildConfig;
import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.model.YoutubeVideo;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

public class YoutubeVideoFragmentHandler extends YouTubePlayerSupportFragment{

    private YoutubeVideo mYoutubeVideo;
    private YouTubePlayer mYoutubePlayer;
    private OnYoutubeVideoHandlerListener mOnYoutubeVideoHandlerListener;

    public YoutubeVideoFragmentHandler(){
        init();
    }

    private void init(){
        mYoutubeVideo = new YoutubeVideo(null, 0, YoutubeVideo.IDLE);

        initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restore) {
                if(youTubePlayer != null){
                    mYoutubePlayer = youTubePlayer;
                    if(mOnYoutubeVideoHandlerListener != null)
                        setYoutubePlayerListener();
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
        if(mYoutubePlayer != null)
            setYoutubePlayerListener();
    }

    private void setYoutubePlayerListener(){
        mYoutubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                Log.e("youtube","youtube onPlaying");

                mYoutubeVideo.setState(YoutubeVideo.RUNNING);
                mOnYoutubeVideoHandlerListener.onPlay();
            }

            @Override
            public void onPaused() {
                // avoid called pause when review fragment is started.
                // and no video is in running state or pause state.
                if(mYoutubeVideo.getState() != YoutubeVideo.IDLE) {
                    mYoutubeVideo.setState(YoutubeVideo.PAUSE);
                    mOnYoutubeVideoHandlerListener.onPause();
                }
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
                Log.e("youtube","youtube onLoading");

                // Check to detect rotation during pause.
                if(mYoutubeVideo.getState() == YoutubeVideo.RESTORING_PAUSE) {
                    mOnYoutubeVideoHandlerListener.onPause();
                }else {
                    mYoutubeVideo.setState(YoutubeVideo.LOADING);
                    mOnYoutubeVideoHandlerListener.onLoading();
                }
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
        // detect if device is rotated.
        if(mYoutubeVideo.getState() == YoutubeVideo.PAUSE)
            mYoutubeVideo.setState(YoutubeVideo.RESTORING_PAUSE);

        return mYoutubeVideo;
    }

    public void onRestoreInstanceState(Parcelable parcelable){
        mYoutubeVideo = (YoutubeVideo) parcelable;
        reinitialized();
    }

    public void setVideoKeyAndPlay(String key){
        mYoutubeVideo.setKey(key);
        mYoutubeVideo.setState(YoutubeVideo.IDLE);
        videoHandling();
    }

    private void videoHandling(){
        switch (mYoutubeVideo.getState()){
            case YoutubeVideo.RESTORING_PAUSE:
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
                Log.e("youtube","youtube key " + mYoutubeVideo.getKey());
                mYoutubePlayer.loadVideo(mYoutubeVideo.getKey());
                mYoutubePlayer.play();
                break;
        }
    }

    private void reinitialized(){
        initialize(BuildConfig.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restore) {
                if(youTubePlayer != null){
                    Log.e("youtube","youtube reinti done");
                    mYoutubePlayer = youTubePlayer;
                    if(mOnYoutubeVideoHandlerListener != null)
                        setYoutubePlayerListener();
                    videoHandling();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }
    // avoid leaking in memory.
    public void release() {
        mYoutubePlayer.release();
    }

    public interface OnYoutubeVideoHandlerListener{
        void onLoading();
        void onEnding();
        void onPause();
        void onPlay();
    }


}
