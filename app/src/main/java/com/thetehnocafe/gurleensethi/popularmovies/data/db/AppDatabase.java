package com.thetehnocafe.gurleensethi.popularmovies.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.thetehnocafe.gurleensethi.popularmovies.data.db.converter.IntegerListConverter;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieReviewDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.db.dao.MovieVideoDAO;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

@Database(entities = {Movie.class, MovieVideo.class, MovieReview.class}, version = 1)
@TypeConverters({IntegerListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public static void buildDatabase(Context context) {
        sInstance = Room.databaseBuilder(context, AppDatabase.class, "movie-db")
                .build();
    }

    public static AppDatabase getInstance() {
        return sInstance;
    }

    public abstract MovieDAO getMovieDAO();

    public abstract MovieVideoDAO getMovieVideoDAO();

    public abstract MovieReviewDAO getMovieReviewDAO();
}
