package com.example.tvshows.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshows.repositories.MostPopularTvShowRepository;
import com.example.tvshows.response.TvShowResponse;

public class MostPopularTvShowsViewModel extends ViewModel {

    private MostPopularTvShowRepository mostPopularTvShowRepository;

    public MostPopularTvShowsViewModel(){
        mostPopularTvShowRepository = new MostPopularTvShowRepository();
    }

    public LiveData<TvShowResponse> getMostPopularTvShows(int page){
        return mostPopularTvShowRepository.getMostPopularTvShows(page);
    }

}
