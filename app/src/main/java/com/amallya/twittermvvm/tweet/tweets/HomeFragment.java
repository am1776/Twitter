package com.amallya.twittermvvm.tweet.tweets;

import android.os.Bundle;

import com.amallya.twittermvvm.models.Entity;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.tweet.tweets.TweetsFragment;
import com.amallya.twittermvvm.utils.Consts;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends TweetsFragment {

    @Override
    protected void getTweets() {
            client.getTweetTimelineList(max, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    processTweetJson(json, false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                    handleNetworkFailure(statusCode, throwable);
                }
            });
    }

    public static HomeFragment newInstance(int page) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    public void postTweet(String newTweet, User loggedInUser){
        Tweet t = new Tweet();
        t.setText(newTweet);
        t.setUser(loggedInUser);
        t.setEntities(new Entity());
        t.setCreatedAt(Consts.JUST_NOW);
        tweetList.add(0,t);
        tweetsAdapter.notifyDataSetChanged();
    }
}


