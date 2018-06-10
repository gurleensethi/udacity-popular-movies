package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public abstract class MovieDAO {
    @Insert
    public abstract void insert(Movie movie);

    @Insert
    public abstract void insert(List<Movie> movies);

    @Query("SELECT * FROM movie WHERE sortOption = :sortOption")
    public abstract LiveData<List<Movie>> getMovies(SortOption sortOption);

    @Query("SELECT * FROM movie WHERE id = :id")
    public abstract LiveData<Movie> getMovie(long id);

    @Query("SELECT isFavourite FROM movie WHERE id = :id")
    protected abstract boolean isMovieFavourite(long id);

    @Query("DELETE FROM movie")
    public abstract void deleteAllMovies();

    @Query("SELECT * FROM movie WHERE isFavourite = 1 ORDER BY id")
    public abstract LiveData<List<Movie>> getFavouriteMovies();

    @Query("DELETE FROM movie WHERE isFavourite = 0")
    public abstract void deleteAllNonFavouriteMovies();

    @Update
    public abstract void update(Movie movie);

    @Transaction
    public void updateMovies(List<Movie> movies, SortOption sortOption) {
        deleteAllNonFavouriteMovies();

        for (Movie movie : movies) {
            movie.setSortOption(sortOption);

            if (isMovieFavourite(movie.getId())) {
                movie.setFavourite(true);
                update(movie);
            } else {
                insert(movie);
            }
        }
    }
}
