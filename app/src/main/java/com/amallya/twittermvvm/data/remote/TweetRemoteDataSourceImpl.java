package com.amallya.twittermvvm.data.remote;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.DataSource;
import com.amallya.twittermvvm.data.TweetRemoteDataSource;
import com.amallya.twittermvvm.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/14/2018.
 */
public class TweetRemoteDataSourceImpl implements TweetRemoteDataSource {

    private TwitterClient client;

    public TweetRemoteDataSourceImpl(){
        client = RestApplication.getRestClient();
    }

    @Override
    public void getTweets(long maxTweetId, final GetTweetsCallBack callBack) {
        client.getTweetTimelineList(maxTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                callBack.onTweetsObtained(processTweetJson(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
            }
        });
    }

    private ArrayList<Tweet> processTweetJson(JSONArray json){
        ArrayList<Tweet> tweetListNew = Tweet.getTweetList(json.toString());
        return tweetListNew;
    }

}
