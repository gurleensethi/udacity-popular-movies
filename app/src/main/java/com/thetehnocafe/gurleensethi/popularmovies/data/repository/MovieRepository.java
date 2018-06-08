package com.thetehnocafe.gurleensethi.popularmovies.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thetehnocafe.gurleensethi.popularmovies.AppSecret;
import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;
import com.thetehnocafe.gurleensethi.popularmovies.data.ApiResponse;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.AppDatabase;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieReviewDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieVideoDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.NetworkBoundResource;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;
import com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels.MovieRequest;
import com.thetehnocafe.gurleensethi.popularmovies.data.Resource;
import com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels.MovieReviewsRequest;
import com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels.MovieVideosRequest;
import com.thetehnocafe.gurleensethi.popularmovies.network.TMDBApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("NullableProblems")
public class MovieRepository {
    private final TMDBApi tmdbApi;
    private final MovieDAO movieDAO;
    private final MovieVideoDAO movieVideoDAO;
    private final MovieReviewDAO movieReviewDAO;

    public MovieRepository(TMDBApi tmdbApi, AppDatabase appDatabase) {
        this.tmdbApi = tmdbApi;
        this.movieDAO = appDatabase.getMovieDAO();
        movieVideoDAO = appDatabase.getMovieVideoDAO();
        movieReviewDAO = appDatabase.getMovieReviewDAO();
    }

    public LiveData<Resource<List<Movie>>> getMovies(final SortOption sortOption) {
        return new NetworkBoundResource<List<Movie>, MovieRequest>() {

            @Override
            protected boolean shouldFetch(List<Movie> movies) {
                return true;
            }

            @Override
            protected LiveData<List<Movie>> loadFromDB() {
                return movieDAO.getMovies();
            }

            @Override
            protected void saveCallResponse(MovieRequest item) {
                movieDAO.deleteAllMovies();
                movieDAO.insert(item.getResults());
            }

            @Override
            public LiveData<ApiResponse<MovieRequest>> createCall() {
                final MutableLiveData<ApiResponse<MovieRequest>> apiResult = new MutableLiveData<>();

                Callback<MovieRequest> callback = new Callback<MovieRequest>() {
                    @Override
                    public void onResponse(Call<MovieRequest> call, Response<MovieRequest> response) {
                        ApiResponse<MovieRequest> apiResponse = new ApiResponse<>(true, response.body(), null);
                        apiResult.setValue(apiResponse);
                    }

                    @Override
                    public void onFailure(Call<MovieRequest> call, Throwable t) {
                        ApiResponse<MovieRequest> apiResponse = new ApiResponse<>(false, null, null);
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

    public LiveData<Movie> getMovie(final long id) {
        return movieDAO.getMovie(id);
    }

    public LiveData<Resource<List<MovieVideo>>> getMovieVideos(final long id) {
        return new NetworkBoundResource<List<MovieVideo>, MovieVideosRequest>() {

            @Override
            protected boolean shouldFetch(List<MovieVideo> movieVideos) {
                return true;
            }

            @Override
            protected LiveData<List<MovieVideo>> loadFromDB() {
                return movieVideoDAO.getVideos(id);
            }

            @Override
            protected void saveCallResponse(MovieVideosRequest item) {
                for (MovieVideo movieVideo : item.getVideos()) {
                    movieVideo.setMovieId(id);
                }
                movieVideoDAO.deleteVideosOfMovie(id);
                movieVideoDAO.insert(item.getVideos());
            }

            @Override
            protected LiveData<ApiResponse<MovieVideosRequest>> createCall() {
                final MutableLiveData<ApiResponse<MovieVideosRequest>> apiResult = new MutableLiveData<>();

                tmdbApi.getMovieVideos(id, AppSecret.API_KEY)
                        .enqueue(new Callback<MovieVideosRequest>() {
                            @Override
                            public void onResponse(Call<MovieVideosRequest> call, Response<MovieVideosRequest> response) {
                                ApiResponse<MovieVideosRequest> apiResponse = new ApiResponse<>(true, response.body(), null);
                                apiResult.setValue(apiResponse);
                            }

                            @Override
                            public void onFailure(Call<MovieVideosRequest> call, Throwable t) {
                                ApiResponse<MovieVideosRequest> apiResponse = new ApiResponse<>(false, null, null);
                                apiResult.setValue(apiResponse);
                            }
                        });

                return apiResult;
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<MovieReview>>> getMovieReviews(final long movieId) {
        return new NetworkBoundResource<List<MovieReview>, MovieReviewsRequest>() {

            @Override
            protected boolean shouldFetch(List<MovieReview> reviews) {
                return true;
            }

            @Override
            protected LiveData<List<MovieReview>> loadFromDB() {
                return movieReviewDAO.getMovieReviews(movieId);
            }

            @Override
            protected void saveCallResponse(MovieReviewsRequest item) {
                movieReviewDAO.deleteAllReviews(movieId);
                movieReviewDAO.insert(item.getReviews());
            }

            @Override
            protected LiveData<ApiResponse<MovieReviewsRequest>> createCall() {
                final MutableLiveData<ApiResponse<MovieReviewsRequest>> apiResult = new MutableLiveData<>();

                tmdbApi.getMovieReviews(movieId, AppSecret.API_KEY)
                        .enqueue(new Callback<MovieReviewsRequest>() {
                            @Override
                            public void onResponse(Call<MovieReviewsRequest> call, Response<MovieReviewsRequest> response) {
                                apiResult.setValue(new ApiResponse<MovieReviewsRequest>(true, response.body(), null));
                            }

                            @Override
                            public void onFailure(Call<MovieReviewsRequest> call, Throwable t) {
                                apiResult.setValue(new ApiResponse<MovieReviewsRequest>(false, null, null));
                            }
                        });

                return apiResult;
            }
        }.getAsLiveData();
    }
}
