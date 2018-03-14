package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.utils.NetworkUtils;

import java.util.List;

/**
 * Created by anmallya on 3/12/2018.
 */
public class TweetViewModel extends ViewModel{

    private LiveData<List<Tweet>> tweetList;
    private MutableLiveData<Boolean> isRefreshing;

    TweetListRepo tweetListRepo;
    TweetActionsRepo tweetActionsRepo;

    public TweetViewModel() {
        super();
        tweetListRepo = new TweetListRepo();
        tweetActionsRepo = new TweetActionsRepo();
        this.tweetList = tweetListRepo.getTweets();
        isRefreshing = new MutableLiveData<>();
    }

    public LiveData<List<Tweet>> getTweetsObservable(){
        return tweetList;
    }

    public LiveData<Boolean> getRefreshingObservable(){
        return isRefreshing;
    }

    public void refreshTweets(){
        isRefreshing.setValue(true);
        tweetListRepo.refreshTweets();
    }

    public void loadMoreTweets(){
        tweetListRepo.loadMoreTweets();
    }

    public void userActionOnTweet(TweetUserAction tweetUserAction, long tweetId){
        switch (tweetUserAction) {
            case FAVORITE:
                tweetActionsRepo.favorited(tweetId);
            case UNFAVORITE:
                tweetActionsRepo.unFavorited(tweetId);
            case RETWEET:
                tweetActionsRepo.retweet(tweetId);
            case UNRETWEET:
                tweetActionsRepo.unRetweet(tweetId);
        }
    }
}
