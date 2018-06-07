package com.thetehnocafe.gurleensethi.popularmovies.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.thetehnocafe.gurleensethi.popularmovies.data.db.converter.IntegerListConverter;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

@Database(entities = {Movie.class}, version = 1)
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
}
