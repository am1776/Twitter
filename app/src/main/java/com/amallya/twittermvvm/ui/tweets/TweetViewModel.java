package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmallya on 3/12/2018.
 */
public class TweetViewModel extends ViewModel{

    private LiveData<Response<List<Tweet>>> tweetListObservable;
    private MutableLiveData<Boolean> isRefreshingObservable;
    private MutableLiveData<Tweet>  clickedTweetObservable;
    private TweetListRepo tweetListRepo;
    private TweetActionsRepo tweetActionsRepo;

    public TweetViewModel(TweetListRepo tweetListRepo, TweetActionsRepo tweetActionsRepo) {
        super();
        this.tweetListRepo = tweetListRepo;
        this.tweetActionsRepo = tweetActionsRepo;
        tweetListObservable = tweetListRepo.getTweets();
        isRefreshingObservable = new MutableLiveData<>();
        clickedTweetObservable = new MutableLiveData<>();
        loadMoreTweets();
    }

    public LiveData<Response<List<Tweet>>> getTweetsObservable(){
        return tweetListObservable;
    }

    public LiveData<Boolean> getRefreshingObservable(){
        return isRefreshingObservable;
    }

    public LiveData<Tweet> getSelectedTweetObservable(){
        return clickedTweetObservable;
    }

    public void refreshTweets(){
        tweetListRepo.refreshTweets();
        isRefreshingObservable.setValue(true);
    }

    public void loadMoreTweets(){
        tweetListRepo.loadMoreTweets();
    }

    public void userActionOnTweet(TweetUserAction tweetUserAction, long tweetId){
        tweetActionsRepo.userActionOnTweet(tweetUserAction, tweetId);
    }

    public void onTweetClicked(int position){
        clickedTweetObservable.setValue(tweetListRepo.fetchSelectedTweet(position));
    }

    public LiveData<Response<?>> getTweetsActionsObservable(){
        return tweetActionsRepo.getTweetsActionObservable();
    }
}
