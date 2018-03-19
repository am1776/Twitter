package com.amallya.twittermvvm.ui.tweet_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;

import java.util.List;

/**
 * Created by anmallya on 3/12/2018.
 */
public class TweetDetailViewModel extends ViewModel{

    private TweetActionsRepo tweetActionsRepo;

    public TweetDetailViewModel(TweetActionsRepo tweetActionsRepo) {
        this.tweetActionsRepo = tweetActionsRepo;
    }

    public void userActionOnTweet(TweetUserAction tweetUserAction, long tweetId){
        tweetActionsRepo.userActionOnTweet(tweetUserAction, tweetId);
    }

    public void userReplyOnTweet(TweetUserAction tweetUserAction, long tweetId, String tweetResponse){
        tweetActionsRepo.userReplyOnTweet(tweetUserAction, tweetId, tweetResponse);
    }

    public LiveData<Response<?>> getTweetsActionsObservable(){
        return tweetActionsRepo.getTweetsActionObservable();
    }
}
