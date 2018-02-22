package com.example.mohamednagy.myapplication.Ui.movie_details.ui_helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.helperClasses.Utility;
import com.example.mohamednagy.myapplication.model.Review;
import com.example.mohamednagy.myapplication.model.Trailer;
import com.example.mohamednagy.myapplication.video.VideoHandler;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

abstract public class TrailersAdapter extends RecyclerView.Adapter<ScreenViewHolder.DetailsViewHolder.MovieTrailerRecycle> {
    private List<Trailer> mTrailerList;
    private VideoHandler mVideoHandler;

    public TrailersAdapter(List<Trailer> trailerList, VideoHandler videoHandler){
        mTrailerList = trailerList;
        mVideoHandler = videoHandler;
    }

    @Override
    public ScreenViewHolder.DetailsViewHolder.MovieTrailerRecycle onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScreenViewHolder.DetailsViewHolder.MovieTrailerRecycle(LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.trailer_video_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(ScreenViewHolder.DetailsViewHolder.MovieTrailerRecycle holder, int position) {
        final Trailer trailer = mTrailerList.get(position);

        holder.setValues(trailer.getVideoName(), trailer.getVideoType());
        holder.TRAILER_IMAGE_VIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                onTrailerClick();
                mVideoHandler.setUrlAndStart(trailer.getVideoKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailerList != null)?mTrailerList.size():0;
    }

    public void swapList(List<Trailer> trailerList){
        mTrailerList = trailerList;
    }

    public abstract void onTrailerClick();

}
