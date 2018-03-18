package com.amallya.twittermvvm;

import android.app.Application;
import android.content.Context;

import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;



/**
 * Created by anmallya on 3/10/2018.
 */

public class RestApplication extends Application {

    private static Context context;

    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        RestApplication.context = this;
    }

    public static TwitterClient getRestClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        RestApplication.user = user;
    }

}