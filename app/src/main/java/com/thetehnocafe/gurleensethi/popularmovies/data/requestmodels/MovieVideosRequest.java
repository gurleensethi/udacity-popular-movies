package com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieVideo;

import java.util.List;

public class MovieVideosRequest {
    @SerializedName("results")
    @Expose
    private List<MovieVideo> videos;

    public List<MovieVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<MovieVideo> videos) {
        this.videos = videos;
    }
}
