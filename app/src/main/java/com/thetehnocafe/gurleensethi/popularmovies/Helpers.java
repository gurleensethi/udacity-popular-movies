package com.thetehnocafe.gurleensethi.popularmovies;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {
    private static final int DEFAULT_ANIMATION_DURATION = 250;

    public static String buildImageUrl(String path) {
        return "http://image.tmdb.org/t/p/w185" + path;
    }

    public static String buildBackdropImageUrl(String path) {
        return "http://image.tmdb.org/t/p/w780" + path;
    }

    public static String buildYouTubeThumbnailURL(String key) {
        return  "https://img.youtube.com/vi/" + key + "/0.jpg";
    }

    public static View.OnTouchListener buildAnimatedTouchListener() {
        return buildAnimatedTouchListener(DEFAULT_ANIMATION_DURATION);
    }

    public static View.OnTouchListener buildAnimatedTouchListener(final long animationDuration) {
        return new View.OnTouchListener() {
            private int lastAction;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v, "scaleX", 0.9f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v, "scaleY", 0.9f);
                        scaleDownX.setDuration(animationDuration);
                        scaleDownY.setDuration(animationDuration);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.play(scaleDownX).with(scaleDownY);

                        animatorSet.start();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            v.performClick();
                        }
                    }
                    default: {
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v, "scaleX", 1f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v, "scaleY", 1f);
                        scaleDownX.setDuration(animationDuration);
                        scaleDownY.setDuration(animationDuration);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.play(scaleDownX).with(scaleDownY);

                        animatorSet.start();
                        break;
                    }
                }
                lastAction = event.getAction();
                return true;
            }
        };
    }

    public static void handleViewHideOnScroll(View view, int dy, float maxTranslation) {
        float translationY = view.getTranslationY();
        if (dy > 0) {
            if (translationY < maxTranslation) {
                if (translationY + dy < maxTranslation) {
                    view.setTranslationY(translationY + dy);
                } else {
                    view.setTranslationY(maxTranslation);
                }
            }
        } else {
            if (translationY > 0) {
                if (translationY + dy > 0) {
                    view.setTranslationY(translationY + dy);
                } else {
                    view.setTranslationY(0);
                }
            }
        }
    }

    public static void setUpTransparentStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertReleaseDate(String releaseDate) {
        if (releaseDate == null) {
            return "N/A";
        }
        SimpleDateFormat parserFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat converterFormat = new SimpleDateFormat("MMMM dd, yyyy");

        try {
            Date date = parserFormat.parse(releaseDate);
            return converterFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}
