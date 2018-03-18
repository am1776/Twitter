package com.amallya.twittermvvm.data.source.remote;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;



/**
 * Created by anmallya on 3/10/2018.
 */

public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";

    public static final String REST_CALLBACK_URL = "oauth://codepathtweets";
    public static final String REST_CONSUMER_KEY = "ztpNiAun5Ne8h6KW7V7amIwDS";
    public static final String REST_CONSUMER_SECRET = "RCtQ5isUnd66tEwo0cxRSYhcxWCOMqUyiy4FuCzXyyOqD167i4";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getTweetTimelineList(long max, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", 25);
        params.put("since_id", 1);
        if(max != -1){
            params.put("max_id", max-1);
        }
        client.get(apiUrl, params, handler);
    }

    public void postFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void postUnFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void postRetweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/"+tweetId+".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    public void postUnRetweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/unretweet/"+tweetId+".json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void postReply(String tweet, long tweetId, AsyncHttpResponseHandler handler) {
        System.out.println("reply posted "+tweet+" id "+tweetId);
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        params.put("in_reply_to_status_id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }
}