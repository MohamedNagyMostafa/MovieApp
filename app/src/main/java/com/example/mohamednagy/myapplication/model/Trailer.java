package com.example.mohamednagy.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed Nagy on 2/18/2018.
 */

public class Trailer implements Parcelable {
    private String mVideoKey;
    private String mVideoName;
    private String mVideoType;

    public Trailer(){}

    public Trailer(String videoName, String videoType, String videoKey){
        mVideoKey = videoKey;
        mVideoName = videoName;
        mVideoType = videoType;
    }

    protected Trailer(Parcel in) {
        mVideoKey = in.readString();
        mVideoName = in.readString();
        mVideoType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mVideoKey);
        parcel.writeString(mVideoName);
        parcel.writeString(mVideoType);
    }
}
