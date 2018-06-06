package com.thetehnocafe.gurleensethi.popularmovies;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.thetehnocafe.gurleensethi.popularmovies.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setUpTransparentStatusBar(getWindow());
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, HomeFragment.getInstance())
                .commit();
    }
}
