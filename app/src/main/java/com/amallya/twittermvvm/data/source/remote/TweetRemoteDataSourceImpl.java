package com.amallya.twittermvvm.data.source.remote;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.models.Request;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;
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

import static com.amallya.twittermvvm.models.Response.GENERIC_SUCCESS_MSG;
import static com.amallya.twittermvvm.models.Response.Status.ERROR;
import static com.amallya.twittermvvm.models.Response.Status.SUCCESS;

/**
 * Created by anmallya on 3/14/2018.
 */
public class TweetRemoteDataSourceImpl implements TweetRemoteDataSource<DataSource.ResultCallBack> {

    private TwitterClient client;

    public TweetRemoteDataSourceImpl(TwitterClient client){
        this.client = client;
    }

    @Override
    public void getTweets(long maxTweetId, final ResultCallBack callBack) {
        client.getTweetTimelineList(maxTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Response<List<Tweet>> response = new Response(GENERIC_SUCCESS_MSG, SUCCESS);
                response.setData(processTweetJson(json));
                callBack.onResultObtained(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                Response<List<Tweet>> response = new Response(throwable.getMessage(), ERROR);
                callBack.onResultObtained(response);
            }
        });
    }

    @Override
    public void getUserCred(final ResultCallBack callBack) {
        client.getCurrentUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Response<User> response = new Response(GENERIC_SUCCESS_MSG, SUCCESS);
                response.setData(parseUser(json));
                callBack.onResultObtained(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                Response<User> response = new Response(throwable.getMessage(), ERROR);
                callBack.onResultObtained(response);
            }
        });
    }

    @Override
    public void takeActionOnTweet(final TweetUserAction tweetUserAction, Request request, final ResultCallBack callBack) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println(tweetUserAction.toString() + " success "+statusCode);
                System.out.println("success j "+json);
                Response<User> response = new Response(GENERIC_SUCCESS_MSG, SUCCESS);
                response.setData(parseUser(json));
                callBack.onResultObtained(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                System.out.println(tweetUserAction.toString() + " failure "+statusCode);
                System.out.println("failure j "+throwable);
                Response<User> response = new Response(throwable.getMessage(), ERROR);
                callBack.onResultObtained(response);
            }
        };
        switch (tweetUserAction) {
            case FAVORITE:
                client.postFavorite(request.getId(), handler);
                break;
            case UNFAVORITE:
                client.postUnFavorite(request.getId(), handler);
                break;
            case RETWEET:
                client.postRetweet(request.getId(), handler);
                break;
            case UNRETWEET:
                client.postUnRetweet(request.getId(), handler);
                break;
            case REPLY:
                client.postReply(request.getMessage(), request.getId(), handler);
        }
    }

    @Override
    public void clearAccessToken(ResultCallBack callBack){
        client.clearAccessToken();
    }

    private ArrayList<Tweet> processTweetJson(JSONArray json){
        ArrayList<Tweet> tweetListNew = Tweet.getTweetList(json.toString());
        return tweetListNew;
    }

    private User parseUser(JSONObject json){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement tweetElement = parser.parse(json.toString());
        JsonObject jObject = tweetElement.getAsJsonObject();
        return gson.fromJson(jObject, User.class);
    }
}
