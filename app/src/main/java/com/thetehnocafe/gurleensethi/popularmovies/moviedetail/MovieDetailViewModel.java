package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.thetehnocafe.gurleensethi.popularmovies.data.db.AppDatabase;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.repository.MovieRepository;
import com.thetehnocafe.gurleensethi.popularmovies.network.NetworkService;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository movieRepository = new MovieRepository(NetworkService.getInstance().getTMDBApi(), AppDatabase.getInstance().getMovieDAO());
    private long movieId;
    private LiveData<Movie> movieLiveData;

    public MovieDetailViewModel(long movieId) {
        this.movieId = movieId;
        movieLiveData = movieRepository.getMovie(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movieLiveData;
    }

    @SuppressWarnings({"unchecked"})
    static class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long movieId;

        public MovieDetailViewModelFactory(long movieId) {
            this.movieId = movieId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MovieDetailViewModel(movieId);
        }
    }
}
