package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MovieDAO {
    @Insert
    void insert(Movie movie);

    @Insert
    void insert(List<Movie> movies);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> getMovie(long id);

    @Query("DELETE FROM movie")
    void deleteAllMovies();

    @Update
    void update(Movie movie);
}
