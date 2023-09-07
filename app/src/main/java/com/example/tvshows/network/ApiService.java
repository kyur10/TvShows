package com.example.tvshows.network;

import com.example.tvshows.response.TvShowDetailsResponse;
import com.example.tvshows.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TvShowDetailsResponse> getTvShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TvShowResponse> searchTVShow(@Query("q") String query, @Query("page") int page);

}
