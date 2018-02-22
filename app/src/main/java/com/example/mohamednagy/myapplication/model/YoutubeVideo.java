package com.example.mohamednagy.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.android.youtube.player.YouTubePlayer;

/**
 * Created by Mohamed Nagy on 2/22/2018.
 */

public class YoutubeVideo implements Parcelable {
    public static final int PAUSE   = 0x1;
    public static final int RUNNING = 0x2;
    public static final int LOADING = 0x3;
    public static final int ENDED   = 0x4;
    public static final int IDLE    = 0x5;

    private String mKey;
    private Integer mDuration;
    private Integer mState;

    public YoutubeVideo(@Nullable String key, Integer duration, Integer state){
        mKey = key;
        mDuration = duration;
        mState = state;
    }

    protected YoutubeVideo(Parcel in) {
        mKey = in.readString();
        mDuration = in.readInt();
        mState = in.readInt();
    }

    public static final Creator<YoutubeVideo> CREATOR = new Creator<YoutubeVideo>() {
        @Override
        public YoutubeVideo createFromParcel(Parcel in) {
            return new YoutubeVideo(in);
        }

        @Override
        public YoutubeVideo[] newArray(int size) {
            return new YoutubeVideo[size];
        }
    };

    public int getDuration() {
        return mDuration;
    }

    public String getKey() {
        return mKey;
    }

    public int getState() {
        return mState;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mKey);
        parcel.writeInt(mDuration);
        parcel.writeInt(mState);
    }
}
