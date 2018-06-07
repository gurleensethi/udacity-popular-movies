package com.thetehnocafe.gurleensethi.popularmovies.home;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.repository.MovieRepository;
import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.network.NetworkService;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<SortOption> sortOptionLiveData = new MutableLiveData<>();

    private final MovieRepository movieRepository = new MovieRepository(NetworkService.getInstance().getTMDBApi());

    private final LiveData<Resource<List<Movie>>> movies = Transformations.switchMap(sortOptionLiveData, new Function<SortOption, LiveData<Resource<List<Movie>>>>() {
        @Override
        public LiveData<Resource<List<Movie>>> apply(SortOption input) {
            return movieRepository.getMovies(input);
        }
    });

    public LiveData<Resource<List<Movie>>> getMovies() {
        return movies;
    }

    public void setSortOption(SortOption sortOption) {
        sortOptionLiveData.setValue(sortOption);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("onCleared", "View Model cleared");
    }
}
