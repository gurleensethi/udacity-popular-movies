package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;

import java.util.List;

@Dao
public abstract class MovieReviewDAO {
    @Insert
    public abstract void insert(List<MovieReview> reviews);

    @Insert
    public abstract void insert(MovieReview review);

    @Query("SELECT * FROM movie_review WHERE movieId = :id")
    public abstract LiveData<List<MovieReview>> getMovieReviews(long id);

    @Query("SELECT * FROM movie_review WHERE id = :id")
    public abstract LiveData<MovieReview> getMovieReview(long id);

    @Query("DELETE FROM movie_review WHERE movieId = :id")
    public abstract void deleteAllReviews(long id);

    @Transaction
    public void updateData(List<MovieReview> reviews, long id) {
        for (MovieReview review : reviews) {
            review.setMovieId(id);
        }
        deleteAllReviews(id);
        insert(reviews);
    }
}
