package com.amallya.twittermvvm.models;

import com.amallya.twittermvvm.ui.tweets.TweetUserAction;

/**
 * Created by anmallya on 3/15/2018.
 */

public class Request{
    private long id;
    private String message;
    private TweetUserAction tweetUserAction;

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public TweetUserAction getTweetUserAction() {
        return tweetUserAction;
    }

    public static Request createRequest(long tweetId, TweetUserAction tweetUserAction){
        Request request = new Request();
        request.id = tweetId;
        request.tweetUserAction = tweetUserAction;
        return request;
    }

    public static Request createRequest(long tweetId, TweetUserAction tweetUserAction, String msg){
        Request request = new Request();
        request.id = tweetId;
        request.tweetUserAction = tweetUserAction;
        request.message = msg;
        return  request;
    }
}
