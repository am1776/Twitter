package com.amallya.twittermvvm.data.source.remote;

import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.models.Request;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;

/**
 * Created by anmallya on 3/14/2018.
 */
public interface TweetRemoteDataSource<T extends DataSource.ResultCallBack> extends DataSource {

    void getUserCred(T callBack);

    void takeActionOnTweet(TweetUserAction tweetUserAction, Request request, T callBack);

    void clearAccessToken(T callBack);

}
