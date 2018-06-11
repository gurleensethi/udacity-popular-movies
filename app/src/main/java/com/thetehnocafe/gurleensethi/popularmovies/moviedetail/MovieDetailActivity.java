package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thetehnocafe.gurleensethi.popularmovies.Helpers;
import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "movie";
    public static final String TAG_REVIEW_BOTTOM_SHEET = "review_bottom_sheet";
    private long MOVIE_ID;

    @BindView(R.id.backdropImageView)
    ImageView mBackdropImageView;
    @BindView(R.id.movieImageView)
    ImageView mMovieImageView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.nameTextView)
    TextView mNameTextView;
    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;
    @BindView(R.id.ratingsTextView)
    TextView mRatingTextView;
    @BindView(R.id.dateTextView)
    TextView mDateTextView;
    @BindView(R.id.trailersRecyclerView)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.reviewsRecyclerView)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.reviewsMessageTextView)
    TextView mReviewMessageTextView;
    @BindView(R.id.trailersMessageTextView)
    TextView mTrailersMessageTextView;
    @BindView(R.id.shareTrailerButton)
    Button mShareTrailerButton;
    @BindView(R.id.favouriteToggleButton)
    ToggleButton mFavouriteToggleButton;

    private MovieDetailViewModel mViewModel;
    private TrailerRecyclerAdapter mTrailersRecyclerAdapter;
    private ReviewsRecyclerAdapter mReviewRecyclerAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setUpTransparentStatusBar(getWindow());
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        MOVIE_ID = getIntent().getLongExtra(EXTRA_MOVIE_ID, 0);

        if (MOVIE_ID == 0) {
            finish();
            return;
        }

        initViewModel();
        initViews();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        mCollapsingToolbar.setTitleEnabled(false);

        mTrailersRecyclerAdapter = new TrailerRecyclerAdapter();
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersRecyclerView.setAdapter(mTrailersRecyclerAdapter);
        mTrailersRecyclerAdapter.addActionListener(new TrailerRecyclerAdapter.ActionListener() {
            @Override
            public void onVideoClicked(MovieVideo movieVideo) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Helpers.buildYoutubeURL(movieVideo.getKey())));
                startActivity(Intent.createChooser(intent, "View Trailer:"));
            }
        });

        mReviewRecyclerAdapter = new ReviewsRecyclerAdapter();
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mReviewsRecyclerView.setAdapter(mReviewRecyclerAdapter);
        mReviewRecyclerAdapter.addActionListener(new ReviewsRecyclerAdapter.ActionListener() {
            @Override
            public void onReadMoreCLicked(MovieReview review) {
                ReviewBottomSheetFragment dialog = new ReviewBottomSheetFragment.Builder()
                        .setAuthorName(review.getAuthor())
                        .setContent(review.getContent())
                        .build();

                dialog.show(getSupportFragmentManager(), TAG_REVIEW_BOTTOM_SHEET);
            }
        });

        mFavouriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mViewModel.toggleFavourite(isChecked);
            }
        });
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void initViewModel() {
        MovieDetailViewModel.MovieDetailViewModelFactory factory = new MovieDetailViewModel.MovieDetailViewModelFactory(MOVIE_ID);
        mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);

        mViewModel.getMovie()
                .observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        if (movie == null) {
                            finish();
                            return;
                        }

                        updateUI(movie);
                    }
                });

        mViewModel.getMovieVideoLiveData()
                .observe(this, new Observer<Resource<List<MovieVideo>>>() {
                    @Override
                    public void onChanged(@Nullable final Resource<List<MovieVideo>> listResource) {
                        switch (listResource.getStatus()) {
                            case SUCCESS: {
                                if (listResource.getData().size() == 0) {
                                    mTrailersMessageTextView.setText(R.string.no_trailers_available);
                                    mTrailersMessageTextView.setVisibility(View.VISIBLE);
                                    mTrailersRecyclerView.setVisibility(View.GONE);
                                } else {
                                    mTrailersRecyclerAdapter.updateData(listResource.getData());
                                    mShareTrailerButton.setVisibility(View.VISIBLE);
                                    mShareTrailerButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MovieVideo video = listResource.getData().get(0);
                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            intent.setType("text/plain");
                                            intent.putExtra(Intent.EXTRA_TEXT, String.format(
                                                    getString(R.string.share_trailer_content),
                                                    mViewModel.getMovie().getValue().getOriginalTitle(),
                                                    Helpers.buildYoutubeURL(video.getKey())
                                            ));
                                            startActivity(Intent.createChooser(intent, getString(R.string.share_trailer)));
                                        }
                                    });
                                }
                                break;
                            }
                            case ERROR: {
                                mTrailersMessageTextView.setText(R.string.unable_to_get_trailers);
                                mTrailersMessageTextView.setVisibility(View.VISIBLE);
                                mTrailersRecyclerView.setVisibility(View.GONE);
                                break;
                            }
                            case LOADING: {
                                mTrailersRecyclerAdapter.updateData(new ArrayList(Arrays.asList(null, null, null, null)));
                                break;
                            }
                        }
                    }
                });

        mViewModel.getMovieReviewLiveData()
                .observe(this, new Observer<Resource<List<MovieReview>>>() {
                    @Override
                    public void onChanged(@Nullable Resource<List<MovieReview>> listResource) {
                        switch (listResource.getStatus()) {
                            case SUCCESS: {
                                if (listResource.getData().size() == 0) {
                                    mReviewMessageTextView.setText(R.string.no_reviews_available);
                                    mReviewMessageTextView.setVisibility(View.VISIBLE);
                                    mReviewsRecyclerView.setVisibility(View.GONE);
                                } else {
                                    mReviewRecyclerAdapter.updateData(listResource.getData());
                                }
                                break;
                            }
                            case ERROR: {
                                mReviewMessageTextView.setText(R.string.unable_to_get_reviews);
                                mReviewMessageTextView.setVisibility(View.VISIBLE);
                                mReviewsRecyclerView.setVisibility(View.GONE);
                                break;
                            }
                            case LOADING: {
                                mReviewRecyclerAdapter.updateData(new ArrayList(Arrays.asList(null, null, null, null)));
                                break;
                            }
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void updateUI(Movie movie) {
        RequestOptions backdropRequestOptions = new RequestOptions();
        RequestOptions posterRequestOptions = new RequestOptions();
        backdropRequestOptions.placeholder(R.drawable.ic_local_movies_white_24dp);
        posterRequestOptions.placeholder(R.drawable.ic_play_arrow_grey_24dp);

        Glide.with(this)
                .setDefaultRequestOptions(backdropRequestOptions)
                .load(Helpers.buildBackdropImageUrl(movie.getBackdropPath()))
                .into(mBackdropImageView);

        Glide.with(this)
                .setDefaultRequestOptions(posterRequestOptions)
                .load(Helpers.buildImageUrl(movie.getPosterPath()))
                .into(mMovieImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMovieImageView.setElevation(16f);
        }

        mNameTextView.setText(movie.getTitle());
        mDescriptionTextView.setText(movie.getOverview());
        mRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mDateTextView.setText(Helpers.convertReleaseDate(movie.getReleaseData()));
        mFavouriteToggleButton.setChecked(movie.isFavourite());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
