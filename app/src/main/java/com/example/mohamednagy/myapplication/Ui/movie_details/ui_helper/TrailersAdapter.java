package com.example.mohamednagy.myapplication.Ui.movie_details.ui_helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.R;
import com.example.mohamednagy.myapplication.Ui.holder.ScreenViewHolder;
import com.example.mohamednagy.myapplication.model.Trailer;
import com.example.mohamednagy.myapplication.video.YoutubeVideoFragmentHandler;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/19/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<ScreenViewHolder.DetailsViewHolder.MovieTrailerRecycle> {
    private List<Trailer> mTrailerList;
    private YoutubeVideoFragmentHandler mYoutubeVideoFragmentHandler;

    public TrailersAdapter(List<Trailer> trailerList, YoutubeVideoFragmentHandler youtubeVideoFragmentHandler){
        mTrailerList = trailerList;
        mYoutubeVideoFragmentHandler = youtubeVideoFragmentHandler;
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
                mYoutubeVideoFragmentHandler.setVideoKeyAndPlay(trailer.getVideoKey());
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

}
