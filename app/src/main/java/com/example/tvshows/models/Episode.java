package com.example.tvshows.models;

import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("season")
    private String season;

    @SerializedName("name")
    private String name;

    @SerializedName("episode")
    private String episode;

    @SerializedName("air_date")
    private String airDate;

    public String getSeason() {
        return season;
    }

    public String getName() {
        return name;
    }

    public String getEpisode() {
        return episode;
    }

    public String getAirDate() {
        return airDate;
    }
}
