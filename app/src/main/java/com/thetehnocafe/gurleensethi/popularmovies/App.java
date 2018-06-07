package com.thetehnocafe.gurleensethi.popularmovies;

import android.app.Application;

import com.thetehnocafe.gurleensethi.popularmovies.data.db.AppDatabase;

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.buildDatabase(getApplicationContext());
    }
}
