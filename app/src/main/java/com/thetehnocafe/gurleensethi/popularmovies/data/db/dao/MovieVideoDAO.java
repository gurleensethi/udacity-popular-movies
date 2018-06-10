package com.thetehnocafe.gurleensethi.popularmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

import java.util.List;

import retrofit2.http.GET;

@Dao
public abstract class MovieVideoDAO {

    @Insert
    public abstract void insert(List<MovieVideo> videos);

    @Query("SELECT * FROM movie_video WHERE movieId = :id")
    public abstract LiveData<List<MovieVideo>> getVideos(long id);

    @Query("DELETE FROM movie_video WHERE movieId = :id")
    public abstract void deleteVideosOfMovie(long id);

    @Transaction
    public void updateData(List<MovieVideo> videos, long id) {
        for (MovieVideo movieVideo : videos) {
            movieVideo.setMovieId(id);
        }
        deleteVideosOfMovie(id);
        insert(videos);
    }
}
