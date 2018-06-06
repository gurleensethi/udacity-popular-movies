package com.thetehnocafe.gurleensethi.popularmovies.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thetehnocafe.gurleensethi.popularmovies.AppSecret;
import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;
import com.thetehnocafe.gurleensethi.popularmovies.data.ApiResponse;
import com.thetehnocafe.gurleensethi.popularmovies.data.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.NetworkBoundResource;
import com.thetehnocafe.gurleensethi.popularmovies.data.MovieResult;
import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.network.TMDBApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final TMDBApi tmdbApi;

    public MovieRepository(TMDBApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    public LiveData<Resource<List<Movie>>> getMovies(final SortOption sortOption) {
        return new NetworkBoundResource<List<Movie>, MovieResult>() {

            @Override
            public List<Movie> convert(MovieResult movieResult) {
                return movieResult.getResults();
            }

            @Override
            public LiveData<ApiResponse<MovieResult>> createCall() {
                final MutableLiveData<ApiResponse<MovieResult>> apiResult = new MutableLiveData<>();

                Callback<MovieResult> callback = new Callback<MovieResult>() {
                    @Override
                    public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                        ApiResponse<MovieResult> apiResponse = new ApiResponse<>(true, response.body(), null);
                        apiResult.setValue(apiResponse);
                    }

                    @Override
                    public void onFailure(Call<MovieResult> call, Throwable t) {
                        ApiResponse<MovieResult> apiResponse = new ApiResponse<>(false, null, null);
                        apiResult.setValue(apiResponse);
                    }
                };

                switch (sortOption) {
                    case POPULAR: {
                        tmdbApi.getPopularMovies(AppSecret.API_KEY)
                                .enqueue(callback);
                        break;
                    }
                    case TOP_RATED: {
                        tmdbApi.getTopRatedMovies(AppSecret.API_KEY)
                                .enqueue(callback);
                        break;
                    }
                }

                return apiResult;
            }
        }.getAsLiveData();
    }
}
