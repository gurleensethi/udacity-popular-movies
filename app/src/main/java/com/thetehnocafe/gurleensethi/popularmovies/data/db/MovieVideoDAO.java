package com.thetehnocafe.gurleensethi.popularmovies.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

import java.util.List;

import retrofit2.http.GET;

@Dao
public interface MovieVideoDAO {

    @Insert
    public void insert(List<MovieVideo> videos);

    @Query("SELECT * FROM movie_video WHERE movieId = :id")
    public LiveData<List<MovieVideo>> getVideos(long id);

    @Query("DELETE FROM movie_video WHERE movieId = :id")
    void deleteVideosOfMovie(long id);
}
