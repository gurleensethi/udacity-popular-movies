package com.thetehnocafe.gurleensethi.popularmovies.data.requestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.Movie;

import java.util.List;

public class MovieRequest {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
