package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thetehnocafe.gurleensethi.popularmovies.Helpers;
import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {

    interface ActionListener {
        void onVideoClicked(MovieVideo movieVideo);
    }

    private List<MovieVideo> mVideos = new ArrayList<>();
    private ActionListener mActionListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void updateData(List<MovieVideo> movies) {
        mVideos = movies;
        notifyDataSetChanged();
    }

    public void addActionListener(ActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.shimmerLayout)
        ShimmerFrameLayout mShimmerLayout;
        @BindView(R.id.trailerPosterImageView)
        ImageView mTrailerPosterImageView;
        @BindView(R.id.playImageView)
        ImageView mPlayImageView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            MovieVideo movieVideo = mVideos.get(position);
            if (movieVideo == null) {
                mShimmerLayout.startShimmer();
                return;
            }

            mShimmerLayout.setVisibility(View.GONE);
            mTrailerPosterImageView.setVisibility(View.VISIBLE);
            mPlayImageView.setVisibility(View.VISIBLE);

            Glide.with(itemView)
                    .load(Helpers.buildYouTubeThumbnailURL(movieVideo.getKey()))
                    .into(mTrailerPosterImageView);
        }

        @Override
        public void onClick(View v) {
            if (mActionListener != null) {
                mActionListener.onVideoClicked(mVideos.get(getAdapterPosition()));
            }
        }
    }
}
