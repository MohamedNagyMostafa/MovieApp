package com.example.mohamednagy.myapplication.model;

/**
 * Created by Mohamed Nagy on 2/18/2018.
 */

public class Trailer {
    private String mVideoKey;
    private String mVideoName;
    private String mVideoType;

    public Trailer(){}

    public Trailer(String videoName, String videoType, String videoKey){
        mVideoKey = videoKey;
        mVideoName = videoName;
        mVideoType = videoType;
    }

    public void setVideoKey(String mVideoKey) {
        this.mVideoKey = mVideoKey;
    }

    public void setVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public void setVideoType(String mVideoType) {
        this.mVideoType = mVideoType;
    }

    public String getVideoKey() {
        return mVideoKey;
    }

    public String getVideoName() {
        return mVideoName;
    }

    public String getVideoType() {
        return mVideoType;
    }
}
