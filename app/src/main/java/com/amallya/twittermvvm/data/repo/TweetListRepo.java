package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.Entity;
import com.amallya.twittermvvm.models.Media;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private int max= -1;

    public TweetListRepo(){
        client = RestApplication.getRestClient();
    }

    public LiveData<List<Tweet>> getTweets(){
        final MutableLiveData<List<Tweet>> tweetList = new MutableLiveData<>();
        client.getTweetTimelineList(max, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweetList.setValue(processTweetJson(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                //handleNetworkFailure(statusCode, throwable);
            }
        });
        return tweetList;
    }

    protected ArrayList<Tweet> processTweetJson(JSONArray json){
        ArrayList<Tweet> tweetListNew = Tweet.getTweetList(json.toString());
        if (tweetListNew.size() > 0){
            max = (int)tweetListNew.get(tweetListNew.size() - 1).getId();
        }
        return tweetListNew;
    }
}
