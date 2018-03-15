package com.amallya.twittermvvm.data.repo;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ViewGroup;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/13/2018.
 */

public class TweetActionsRepo extends BaseRepo{

    private TwitterClient client;

    public TweetActionsRepo(TwitterClient client){
        this.client = client;
    }

    public void userActionOnTweet(TweetUserAction tweetUserAction, long tweetId){
        switch (tweetUserAction) {
            case FAVORITE:
                favorited(tweetId);
                 break;
            case UNFAVORITE:
                unFavorited(tweetId);
                break;
            case RETWEET:
                retweet(tweetId);
                break;
            case UNRETWEET:
                unRetweet(tweetId);
                break;
        }
    }

    public void userReplyOnTweet(TweetUserAction tweetUserAction, String tweetResponse, long tweetId){
        reply(tweetId, tweetResponse);
    }


    private void favorited(long tweetId){
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

    private void unFavorited(long tweetId){
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

    private void retweet(long tweetId){
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

    private void unRetweet(long tweetId){
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

    private void reply(long tweetId, String tweetResponse){
        client.postReply(tweetResponse, tweetId,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("reply success "+statusCode);
                System.out.println("success j "+json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println("reply failure "+statusCode);
                System.out.println("failure j "+throwable);
            }
        });
    }


    private void reply(Tweet tweet, String tweetResponse, final ViewGroup relativeLayout){
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


    private void postTweets(String tweet, final ViewGroup relativeLayout){
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
