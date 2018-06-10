package com.thetehnocafe.gurleensethi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
