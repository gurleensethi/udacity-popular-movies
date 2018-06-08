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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

    private MovieDetailViewModel mViewModel;
    private TrailerRecyclerAdapter mTrailersRecyclerAdapter;

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
                    public void onChanged(@Nullable Resource<List<MovieVideo>> listResource) {
                        Log.d("TAG THIS", listResource.getStatus().toString());
                        switch (listResource.getStatus()) {
                            case SUCCESS: {
                                mTrailersRecyclerAdapter.updateData(listResource.getData());
                                break;
                            }
                            case ERROR: {
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
