package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ViewGroup;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/13/2018.
 */

public class TweetActionsRepo {

    private TwitterClient client;

    public TweetActionsRepo(){
        client = RestApplication.getRestClient();
    }

    public void favorited(long tweetId){
        client.postFavorite(tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("Like success "+statusCode);
                System.out.println("success j "+json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println("Like failure "+statusCode);
                System.out.println("failure j "+throwable);
            }
        });
    }

    public void unFavorited(long tweetId){
        client.postUnFavorite(tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("unlike success "+statusCode);
                System.out.println("success j "+json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println("unlike failure "+statusCode);
                System.out.println("failure j "+throwable);
            }
        });
    }

    public void retweet(long tweetId){
        client.postRetweet(tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("retweet success "+statusCode);
                System.out.println("success j "+json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println("retweet failure "+statusCode);
                System.out.println("failure j "+throwable);
            }
        });
    }

    public void unRetweet(long tweetId){
        client.postUnRetweet(tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("unretweet success "+statusCode);
                System.out.println("success j "+json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println("unretweet failure "+statusCode);
                System.out.println("failure j "+throwable);
            }
        });
    }

    public void reply(Tweet tweet, String tweetResponse, final ViewGroup relativeLayout){
        client.postReply(tweetResponse, tweet.getId(),new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Reply Posted Successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Reply posting failure", Snackbar.LENGTH_LONG);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }


    public void postTweets(String tweet, final ViewGroup relativeLayout){
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Tweet Posted Successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Tweet posting failure", Snackbar.LENGTH_LONG);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }
}
