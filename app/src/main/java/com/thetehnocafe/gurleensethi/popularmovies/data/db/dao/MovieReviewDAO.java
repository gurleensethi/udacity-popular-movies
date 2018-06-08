package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;

import java.util.List;

@Dao
public interface MovieReviewDAO {
    @Insert
    void insert(List<MovieReview> reviews);

    @Insert
    void insert(MovieReview review);

    @Query("SELECT * FROM movie_review WHERE movieId = :id")
    LiveData<List<MovieReview>> getMovieReviews(long id);

    @Query("SELECT * FROM movie_review WHERE id = :id")
    LiveData<MovieReview> getMovieReview(long id);

    @Query("DELETE FROM movie_review WHERE movieId = :id")
    void deleteAllReviews(long id);
}
