package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import java.util.List;

/**
 * Created by anmallya on 3/12/2018.
 */
public class TweetViewModel extends ViewModel{

    private final TweetListRepo tweetListRepo;
    private final TweetActionsRepo tweetActionsRepo;

    public TweetViewModel(TweetListRepo tweetListRepo, TweetActionsRepo tweetActionsRepo) {
        this.tweetListRepo = tweetListRepo;
        this.tweetActionsRepo = tweetActionsRepo;
        loadMoreTweets();
    }

    public LiveData<Response<List<Tweet>>> getTweetsObservable(){
        return tweetListRepo.getTweetsObservable();
    }

    public LiveData<Boolean> getRefreshingObservable(){
        return tweetListRepo.getRefreshingObservable();
    }

    public LiveData<Response<?>> getTweetsActionsObservable(){
        return tweetActionsRepo.getTweetsActionObservable();
    }

    public LiveData<Tweet> getSelectedTweetObservable(){
        return tweetListRepo.getSelectedTweetObservable();
    }

    public void refreshTweets(){
        tweetListRepo.refreshTweets();
    }

    public void loadMoreTweets(){
        tweetListRepo.loadMoreTweets();
    }

    public void userActionOnTweet(TweetUserAction tweetUserAction, long tweetId){
        tweetActionsRepo.userActionOnTweet(tweetUserAction, tweetId);
    }

    public void onTweetClicked(int position){
        tweetListRepo.onTweetClicked(position);
    }

}
