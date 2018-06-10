package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public abstract class MovieDAO {
    @Insert
    public abstract void insert(Movie movie);

    @Insert
    public abstract void insert(List<Movie> movies);

    @Query("SELECT * FROM movie")
    public abstract LiveData<List<Movie>> getMovies();


    @Query("SELECT * FROM movie WHERE id = :id")
    public abstract LiveData<Movie> getMovie(long id);

    @Query("DELETE FROM movie")
    public abstract void deleteAllMovies();

    @Query("DELETE FROM movie WHERE isFavourite = 0")
    public abstract void deleteAllNonFavouriteMovies();

    @Update
    public abstract void update(Movie movie);

    @Transaction
    public void updateMovies(List<Movie> movies) {
        deleteAllNonFavouriteMovies();

        /*
         * Try inserting each movie,
         * if exception occurs then movie already exists i.e. already favourite,
         * update the data.
         * */
        for (Movie movie : movies) {
            try {
                insert(movie);
            } catch (SQLiteConstraintException e) {
                movie.setFavourite(true);
                update(movie);
            }
        }
    }
}
