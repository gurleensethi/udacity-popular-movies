package com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;

import java.util.List;

public class MovieReviewsRequest {
    @SerializedName("results")
    @Expose
    private List<MovieReview> reviews;

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }
}
