package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.models.Tweet;

import java.util.List;

/**
 * Created by anmallya on 3/12/2018.
 */
public class TweetViewModel extends ViewModel{

    private LiveData<List<Tweet>> tweetList;
    TweetListRepo tweetListRepo;

    public TweetViewModel() {
        super();
        tweetListRepo = new TweetListRepo();
        this.tweetList = tweetListRepo.getTweets();
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweetList;
    }
}
