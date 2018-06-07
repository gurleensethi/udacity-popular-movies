package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thetehnocafe.gurleensethi.popularmovies.Helpers;
import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    private Movie MOVIE;

    private ImageView mBackdropImageView;
    private ImageView mMovieImageView;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mRatingTextView;
    private TextView mDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setUpTransparentStatusBar(getWindow());
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        MOVIE = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (MOVIE == null) {
            finish();
            return;
        }

        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbar = findViewById(R.id.collapsingToolbar);
        mBackdropImageView = findViewById(R.id.backdropImageView);
        mMovieImageView = findViewById(R.id.movieImageView);
        mNameTextView = findViewById(R.id.nameTextView);
        mDescriptionTextView = findViewById(R.id.descriptionTextView);
        mRatingTextView = findViewById(R.id.ratingsTextView);
        mDateTextView = findViewById(R.id.dateTextView);

        initViews();
    }

    @SuppressLint("CheckResult")
    private void initViews() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        mCollapsingToolbar.setTitleEnabled(false);

        RequestOptions backdropRequestOptions = new RequestOptions();
        RequestOptions posterRequestOptions = new RequestOptions();
        backdropRequestOptions.placeholder(R.drawable.ic_local_movies_white_24dp);
        posterRequestOptions.placeholder(R.drawable.ic_play_arrow_grey_24dp);

        Glide.with(this)
                .setDefaultRequestOptions(backdropRequestOptions)
                .load(Helpers.buildBackdropImageUrl(MOVIE.getBackdropPath()))
                .into(mBackdropImageView);

        Glide.with(this)
                .setDefaultRequestOptions(posterRequestOptions)
                .load(Helpers.buildImageUrl(MOVIE.getPosterPath()))
                .into(mMovieImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMovieImageView.setElevation(16f);
        }

        mNameTextView.setText(MOVIE.getTitle());
        mDescriptionTextView.setText(MOVIE.getOverview());
        mRatingTextView.setText(String.valueOf(MOVIE.getVoteAverage()));
        mDateTextView.setText(Helpers.convertReleaseDate(MOVIE.getReleaseData()));
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
