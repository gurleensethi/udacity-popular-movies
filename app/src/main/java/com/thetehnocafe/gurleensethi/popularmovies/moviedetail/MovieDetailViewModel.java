package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.AppDatabase;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;
import com.thetehnocafe.gurleensethi.popularmovies.data.repository.MovieRepository;
import com.thetehnocafe.gurleensethi.popularmovies.network.NetworkService;

import java.util.List;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository movieRepository = new MovieRepository(NetworkService.getInstance().getTMDBApi(), AppDatabase.getInstance());
    private long movieId;

    private LiveData<Movie> movieLiveData;
    private LiveData<Resource<List<MovieVideo>>> movieVideoLiveData;
    private LiveData<Resource<List<MovieReview>>> movieReviewLiveData;

    public MovieDetailViewModel(long movieId) {
        this.movieId = movieId;
        movieLiveData = movieRepository.getMovie(movieId);
        movieVideoLiveData = movieRepository.getMovieVideos(movieId);
        movieReviewLiveData = movieRepository.getMovieReviews(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movieLiveData;
    }

    public LiveData<Resource<List<MovieVideo>>> getMovieVideoLiveData() {
        return movieVideoLiveData;
    }

    public LiveData<Resource<List<MovieReview>>> getMovieReviewLiveData() {
        return movieReviewLiveData;
    }

    @SuppressWarnings("ConstantConditions")
    public void toggleFavourite(boolean favourite) {
        movieLiveData.getValue().setFavourite(favourite);
        movieRepository.updateMovie(movieLiveData.getValue());
    }

    @SuppressWarnings({"unchecked"})
    static class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long movieId;

        MovieDetailViewModelFactory(long movieId) {
            this.movieId = movieId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MovieDetailViewModel(movieId);
        }
    }
}
