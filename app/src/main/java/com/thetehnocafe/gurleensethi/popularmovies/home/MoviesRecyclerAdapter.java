package com.thetehnocafe.gurleensethi.popularmovies.home;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thetehnocafe.gurleensethi.popularmovies.Helpers;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.BaseViewHolder> {

    interface ActionCallback {
        void onMovieClicked(Movie movie);
    }

    private List<Movie> movies = new ArrayList<>();
    private ActionCallback mActionCallback;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.item_recycler_movie) {
            return new ViewHolder(view);
        } else {
            return new ShimmerViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position) == null) {
            return R.layout.item_recycler_movie_shimmer;
        } else {
            return R.layout.item_recycler_movie;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ShimmerViewHolder extends BaseViewHolder {
        private final ShimmerFrameLayout shimmerFrameLayout;

        ShimmerViewHolder(View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerViewContainer);
            shimmerFrameLayout.startShimmer();
        }
    }

    @SuppressLint("CheckResult")
    class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.movieImageView)
        ImageView mPosterImageView;
        @BindView(R.id.nameTextView)
        TextView mNameTextView;
        @BindView(R.id.averageRatingTextView)
        TextView mAverageVoteTextView;
        private RequestOptions glideRequestOptions = new RequestOptions();

        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
            itemView.setOnTouchListener(Helpers.buildAnimatedTouchListener(300));

            glideRequestOptions.placeholder(R.drawable.ic_play_arrow_grey_24dp);
        }

        void bindData(int position) {
            Movie movie = movies.get(position);

            Glide.with(mPosterImageView.getContext())
                    .setDefaultRequestOptions(glideRequestOptions)
                    .load(Helpers.buildImageUrl(movie.getPosterPath()))
                    .into(mPosterImageView);

            mNameTextView.setText(movie.getOriginalTitle());
            mAverageVoteTextView.setText(String.valueOf(movie.getVoteAverage()));
        }

        @Override
        public void onClick(View v) {
            if (mActionCallback != null) {
                mActionCallback.onMovieClicked(movies.get(getAdapterPosition()));
            }
        }
    }

    public void updateData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addActionCallback(ActionCallback actionCallback) {
        this.mActionCallback = actionCallback;
    }
}
