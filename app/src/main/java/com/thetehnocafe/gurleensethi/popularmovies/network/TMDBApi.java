package com.thetehnocafe.gurleensethi.popularmovies.network;

import com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels.MovieRequest;
import com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels.MovieVideosRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {
    @GET("movie/popular")
    Call<MovieRequest> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieRequest> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieVideosRequest> getMovieVideos(@Path("id") long id, @Query("api_key") String apiKey);
}
