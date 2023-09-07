package com.example.tvshows.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshows.repositories.TvShowDetailsRepository;
import com.example.tvshows.response.TvShowDetailsResponse;

public class TvShowDetailsViewModel extends ViewModel {

    private TvShowDetailsRepository tvShowDetailsRepository;

    public TvShowDetailsViewModel(){
        tvShowDetailsRepository = new TvShowDetailsRepository();
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTvShowDetails(tvShowId);
    }

}
