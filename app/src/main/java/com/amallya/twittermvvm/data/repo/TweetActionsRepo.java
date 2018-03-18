package com.amallya.twittermvvm.data.repo;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amallya.twittermvvm.SingleLiveEvent;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSource;
import com.amallya.twittermvvm.models.Request;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;

import java.util.List;


/**
 * Created by anmallya on 3/13/2018.
 */

public class TweetActionsRepo extends BaseRepo{

    private final DataSource dataSource;
    private final SingleLiveEvent<Response<?>> tweetActionsObservable;

    public TweetActionsRepo(DataSource dataSource){
        this.dataSource = dataSource;
        tweetActionsObservable = new SingleLiveEvent<>();;
    }

    public LiveData<Response<?>> getTweetsActionObservable(){
        return tweetActionsObservable;
    }

    public void userActionOnTweet(final TweetUserAction tweetUserAction, long tweetId){
        Request request = Request.createRequest(tweetId, tweetUserAction);
        ((TweetRemoteDataSource)dataSource).takeActionOnTweet(request, response ->{
            if(response.getErrorCode() == Response.Status.ERROR){
            tweetActionsObservable.setValue(response);
        }});
    }

    public void userReplyOnTweet(TweetUserAction tweetUserAction, String tweetResponse, long tweetId){
        Request request = Request.createRequest(tweetId, tweetUserAction, tweetResponse);
        ((TweetRemoteDataSource)dataSource).takeActionOnTweet(request, response -> {tweetActionsObservable.setValue(response);});
    }
}