package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/12/2018.
 */

public class TweetListRepo {

    private TwitterClient client;
    final MutableLiveData<List<Tweet>> tweetListObservable;
    private static final int TWEET_ID_MAX_DEFAULT = -1;
    private long maxTweetId = TWEET_ID_MAX_DEFAULT;
    private List<Tweet> tweetList;

    public TweetListRepo(){
        client = RestApplication.getRestClient();
        tweetListObservable = new MutableLiveData<>();;
        tweetList = new ArrayList<>();
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweetListObservable;
    }

    private void fetchTweets(){
        client.getTweetTimelineList(maxTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweetList.addAll(processTweetJson(json));
                tweetListObservable.setValue(tweetList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
            }
        });
    }

    public void refreshTweets(){
        tweetList.clear();
        maxTweetId = TWEET_ID_MAX_DEFAULT;
        fetchTweets();
    }

    public void loadMoreTweets(){
        fetchTweets();
    }

    private ArrayList<Tweet> processTweetJson(JSONArray json){
        ArrayList<Tweet> tweetListNew = Tweet.getTweetList(json.toString());
        updateMaxTweetId(tweetListNew);
        return tweetListNew;
    }

    private void updateMaxTweetId(ArrayList<Tweet> tweetListNew){
        if (tweetListNew.size() > 0){
            maxTweetId = tweetListNew.get(tweetListNew.size() - 1).getId();
        }
    }

    public Tweet fetchSelectedTweet(int position){
        return tweetList.get(position);
    }
}
