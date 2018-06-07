package com.thetehnocafe.gurleensethi.popularmovies.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.thetehnocafe.gurleensethi.popularmovies.Helpers;
import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.moviedetail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class HomeFragment extends Fragment {

    private static final int GRID_COLUMNS_PORTRAIT = 2;
    private static final int GRID_COLUMNS_LANDSCAPE = 3;
    private static final String TAG_OPTION_BOTTOM_SHEET = "option_bottom_sheet";
    private float MAX_FAB_TRANSLATION = 0;
    private static final int FAB_DP = 32;

    private RecyclerView mMoviesRecyclerView;
    private FloatingActionButton mOptionsFloatingActionButton;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;
    private HomeViewModel mViewModel;

    private final OptionsBottomSheetFragment.ActionCallback mOptionsActionCallback = new OptionsBottomSheetFragment.ActionCallback() {
        @Override
        public void onChangeSortOption(SortOption sortOption) {
            mViewModel.setSortOption(sortOption);
        }
    };

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mOptionsFloatingActionButton = getView().findViewById(R.id.optionsFloatingActionButton);
        mOptionsFloatingActionButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mOptionsFloatingActionButton.getViewTreeObserver().isAlive()) {
                    mOptionsFloatingActionButton.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                MAX_FAB_TRANSLATION = getFloatingButtonMaxTranslation(mOptionsFloatingActionButton.getMeasuredHeight());
                return false;
            }
        });

        mMoviesRecyclerView = getView().findViewById(R.id.moviesRecyclerView);

        configureRecyclerAdapter(getResources().getConfiguration().orientation);

        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter();
        mMoviesRecyclerAdapter.addActionCallback(new MoviesRecyclerAdapter.ActionCallback() {
            @Override
            public void onMovieClicked(Movie movie) {
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, android.R.anim.fade_out);
            }
        });

        if (savedInstanceState == null) {
            //By default loads the popular movies
            mViewModel.setSortOption(SortOption.POPULAR);
        }

        initViews();
    }

    private void initViews() {
        mMoviesRecyclerView.setAdapter(mMoviesRecyclerAdapter);

        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Helpers.handleViewHideOnScroll(mOptionsFloatingActionButton, dy, MAX_FAB_TRANSLATION);
            }
        });

        mOptionsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsBottomSheetFragment optionsBottomSheetFragment = OptionsBottomSheetFragment.getInstance();
                optionsBottomSheetFragment.addActionCallback(mOptionsActionCallback);
                optionsBottomSheetFragment.show(getActivity().getSupportFragmentManager(), TAG_OPTION_BOTTOM_SHEET);
            }
        });

        mViewModel.getMovies()
                .observe(this, new Observer<Resource<List<Movie>>>() {
                    @Override
                    public void onChanged(@Nullable Resource<List<Movie>> movieResource) {
                        switch (movieResource.getStatus()) {
                            case SUCCESS: {
                                mMoviesRecyclerAdapter.updateData(movieResource.getData());
                                break;
                            }
                            case LOADING: {
                                mMoviesRecyclerAdapter.updateData(new ArrayList(Arrays.asList(null, null, null, null, null, null)));
                                break;
                            }
                            case ERROR: {
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        configureRecyclerAdapter(newConfig.orientation);
    }

    private int getFloatingButtonMaxTranslation(int height) {
        return dpToPixel(FAB_DP) + height;
    }

    private int dpToPixel(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void configureRecyclerAdapter(int orientation) {
        boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), isPortrait ? GRID_COLUMNS_PORTRAIT : GRID_COLUMNS_LANDSCAPE));
    }
}
