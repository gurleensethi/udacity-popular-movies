package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ViewHolder> {
    interface ActionListener {
        void onReadMoreCLicked(MovieReview review);
    }

    private List<MovieReview> mReviews = new ArrayList<>();
    private ActionListener mActionListener;

    @NonNull
    @Override
    public ReviewsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_review, parent, false);
        return new ReviewsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void updateData(List<MovieReview> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public void addActionListener(ActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.authorNameTextView)
        TextView mAuthorNameTextView;
        @BindView(R.id.contentTextView)
        TextView mContentTextView;
        @BindView(R.id.readMoreButton)
        Button mReadMoreButton;
        @BindView(R.id.contentConstraintLayout)
        ConstraintLayout mContentConstraintLayout;
        @BindView(R.id.shimmerLayout)
        ShimmerFrameLayout mShimmerLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

            mReadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionListener != null) {
                        mActionListener.onReadMoreCLicked(mReviews.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bindView(int position) {
            MovieReview review = mReviews.get(position);
            if (review == null) {
                mShimmerLayout.startShimmer();
                return;
            }

            mShimmerLayout.setVisibility(View.GONE);
            mContentConstraintLayout.setVisibility(View.VISIBLE);

            mAuthorNameTextView.setText(review.getAuthor());
            mContentTextView.setText(review.getContent());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
