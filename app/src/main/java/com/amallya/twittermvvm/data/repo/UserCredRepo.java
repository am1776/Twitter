package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.remote.TwitterClient;
import com.amallya.twittermvvm.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/12/2018.
 */

public class UserCredRepo {

    private TwitterClient client;

    public UserCredRepo(){
        client = RestApplication.getRestClient();
    }

    public LiveData<User> getUserCredObservable(){
        final MutableLiveData<User> data = new MutableLiveData<>();
        client.getCurrentUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement tweetElement = parser.parse(json.toString());
                JsonObject jObject = tweetElement.getAsJsonObject();
                User user = gson.fromJson(jObject, User.class);
                data.setValue(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject j) {
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
                if(throwable instanceof  java.io.IOException){
                }
            }
        });
        return data;
    }
}
